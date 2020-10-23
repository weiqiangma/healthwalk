package cn.pertech.healthwalk.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.base.data.constant.Constant;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.query.WalkLogQuery;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.IntegralLog;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.base.entity.WalkLog;
import cn.pertech.healthwalk.service.IntegralLogServiceExt;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.service.WalkLogServiceExt;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import cn.pertech.healthwalk.spring.annotation.LoginedAuth;
import cn.pertech.healthwalk.utils.*;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.lang.Assert;
import jodd.util.URLDecoder;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Value("${wx.AppId}")
    private String appId;
    @Value("${activity.notice}")
    private String notice;
    @Value("${first.org}")
    private String firstOrg;
    @Value("${second.org}")
    private String secondOrg;
    @Value("${activity.time}")
    private String activityTime;
    @Value("${INTEGRAL_LEVEL01}")
    private String integral_level01;
    @Value("${INTEGRAL_LEVEL02}")
    private String integral_level02;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private IntegralLogServiceExt integralLogServiceExt;
    @Autowired
    private WalkLogServiceExt walkLogServiceExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;

    @GetMapping("/get/{id}")
    public User getById(@PathVariable Long id) {
        User user = userServiceExt.getById(id);
        return user!=null?user:new User();
    }

    @GetMapping("/get")
    public User getByEntity(User user) {
        return userServiceExt.getByEntity(user);
    }

    @GetMapping("/list")
    public List<User> list(User user) {
        List<User> userList = userServiceExt.listByEntity(user);
        return userList;
    }

    @PostMapping("/insert")
    public User insert(@RequestBody User user){
        userServiceExt.insert(user);
        return user;
    }

    @PutMapping("/update")
    public int update(@RequestBody User user){
        return userServiceExt.update(user);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteOne(@PathVariable Long id){
        return userServiceExt.deleteById(id);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) {
            result = userServiceExt.deleteByIds(ids);
        }
        return result;
    }

    /**
     * 获取用户unionId
     * @param session
     * @param encryptedData
     * @param iv
     * @return
     */
    @RequestMapping(value = "/getUserUnionId")
    public JsonResult getUserUnionId(@LoginedAuth UserSession session, String encryptedData, String iv) {
        JSONObject object = new JSONObject();
        int isAuth = 0;
        User user = userServiceExt.getById(session.getId());
        if (user == null) {
            return sendArgsError("查询不到该用户");
        }
        String result = WechatDecryptDataUtil.decrypt(appId, encryptedData, session.getSessionKey(), iv);
        JSONObject resultObj = JsonUtils.asJSONObject(result);
        String unionId = resultObj.getString("unionId");
        if (unionId == null) {
            return sendArgsError("查询unionId失败");
        }
        User wxUser = wxApiServiceExt.getUserByUnionId(unionId, user);
        if (wxUser.getStatus() == Constant.USER_STATUS_ACTIVE) {
            isAuth = 1;
            user.setUserName(wxUser.getUserName());
            user.setAvatarUrl(wxUser.getAvatarUrl());
            user.setTeamName(wxUser.getTeamName());
            user.setTeamNo(wxUser.getTeamNo());
            user.setStatus(wxUser.getStatus());
            user.setIntegral(wxUser.getIntegral());
            user.setUnionId(wxUser.getUnionId());
            userServiceExt.update(user);
            object.put("isAuth", isAuth);
        } else if (wxUser.getStatus() == Constant.USER_STATUS_ACTIVE && wxUser.getTeamId() == null) {
            isAuth = 2;
            object.put("isAuth", isAuth);
        } else {
            object.put("isAuth", isAuth);
        }
        return sendSuccess(object);
    }

    /**
     * 开始计算步数
     * @param session
     * @return
     */
    @RequestMapping("/startCountUserSteps")
    public JsonResult startCountUserSteps(@LoginedAuth UserSession session, String encryptedData, String iv) {
        Assert.notNull(session.getId());
        JSONObject resultObj = new JSONObject();
        String sessionKey = session.getSessionKey();
        logger.info("sessionKey:" + sessionKey + "encryptedData:" + encryptedData + "iv:" + iv);
        String startSteps = WechatDecryptDataUtil.decrypt(appId, encryptedData, sessionKey, iv);
        JSONObject object = JSON.parseObject(startSteps);
        JSONArray array = object.getJSONArray("stepInfoList");
        if(array == null || array.size() == 0) {
            return sendArgsError("解析微信运动步数失败");
        }
        JSONObject lastObject = array.getJSONObject(array.size() - 1);
        int step = lastObject.getInteger("step");
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("查询不到该用户");
        }
        WalkLog walkLog = walkLogServiceExt.getUserTodayIntegral(user.getId(), DateUtils.getCurrDate("yyyy-MM-dd"));
        if(walkLog == null) {
            WalkLog insertWalkLog = new WalkLog();
            insertWalkLog.setUserId(user.getId());
            insertWalkLog.setUserName(user.getUserName());
            if(user.getTeamId() != null){
                insertWalkLog.setTeamId(user.getTeamId());
                insertWalkLog.setTeamName(user.getTeamName());
                insertWalkLog.setTeamNo(user.getTeamNo());
            }
            insertWalkLog.setTotalTime(0);
            //根据运动步数计算用户对应积分
            int integral = countUserStepBelogLevel(step);
            insertWalkLog.setStartSteps(step);
            insertWalkLog.setTotalSteps(step);
            insertWalkLog.setStartSteps(step);
            insertWalkLog.setIntegral(integral);
            insertWalkLog.setStartTime(new Date());
            insertWalkLog.setUpdateTime(new Date());
            insertWalkLog.setCreateTime(new Date());
            walkLogServiceExt.insert(insertWalkLog);
            if(StringUtils.isNotEmpty(user.getTeamNo())) {
                UserStatVo userStatVo = userServiceExt.countUserRank(user.getId(), user.getTeamNo());
                user.setTodayRank(userStatVo.getTodayRank());
                user.setTodayStep(userStatVo.getTodayStep());
                userServiceExt.update(user);
            }
            resultObj.put("totalSteps", insertWalkLog.getTotalSteps());
            return sendSuccess(resultObj);
        } else {
            walkLog.setTotalSteps(step);
            //根据运动步数计算用户对应积分
            int integral = countUserStepBelogLevel(step);
            walkLog.setIntegral(integral);
            walkLog.setUpdateTime(new Date());
            walkLogServiceExt.update(walkLog);
            if(StringUtils.isNotEmpty(user.getTeamNo())) {
                UserStatVo userStatVo = userServiceExt.countUserRank(user.getId(), user.getTeamNo());
                user.setTodayRank(userStatVo.getTodayRank());
                user.setTodayStep(userStatVo.getTodayStep());
                userServiceExt.update(user);
            }
            resultObj.put("totalSteps", walkLog.getTotalSteps());
            return sendSuccess(walkLog);
        }
    }


    /**
     * 获取我的及团队步数和排名
     *
     * @return
     */
    @RequestMapping("/getRankAndSteps")
    public JsonResult getRankAndSteps(@LoginedAuth UserSession session) {
        User user = userServiceExt.getById(session.getId());
        if (user == null) {
            return sendArgsError("未查询到该用户");
        }
        if(StringUtils.isEmpty(user.getAvatarUrl())) {
            //设置默认头像
            user.setAvatarUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3491929439,4106440758&fm=26&gp=0.jpg");
        }
        UserStatVo statVo = userServiceExt.countUserRank(user.getId(), user.getTeamNo());
        List<String> integralList = Arrays.asList(integral_level01, integral_level02);
        JSONArray integralTakeLog = integralLogServiceExt.getUserTodayIntegralLog(session.getId(), DateUtils.getCurrDate("yyyy-MM-dd"), integralList);
        statVo.setIntegralLog(integralTakeLog);
        statVo.setAvatarUrl(user.getAvatarUrl());
        return sendSuccess(statVo);
    }

    /**
     * 用户每日步数记录
     * @param session
     * @return
     */
    @RequestMapping("/getUserWalkLog")
    public JsonResult getUserWalkLog(@LoginedAuth UserSession session, WalkLogQuery query) {
        if(session.getId() > 0) {
            query.setUserId(session.getId());
        }
        JSONArray array = userServiceExt.getUserWalkLog(query);
        return sendSuccess(array);
    }

    /**
     * 获取用户当日待领取积分
     * @param session
     * @return
     */
    @RequestMapping("/getWaitTakeIntegral")
    public JsonResult getWaitTakeIntegral(@LoginedAuth UserSession session) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("查询不到该用户");
        }
        WalkLog walkLog = walkLogServiceExt.getUserTodayIntegral(user.getId(), DateUtils.getCurrDate("yyyy-MM-dd"));
        if(walkLog == null) {
            return sendArgsError("未查询到今日运动步数记录");
        }
        return sendSuccess(walkLog);
    }

    /**
     * 用户领取积分
     * @return
     */
    @RequestMapping("/getUserIntegral")
    public JsonResult getUserIntegral(@LoginedAuth UserSession session) {
        User user = userServiceExt.getById(session.getId());
        if(user == null || user.getUnionId() == null) {
            return sendArgsError("用户信息未同步");
        }
        userServiceExt.operateUserIntegral(user);
        return sendSuccess();
    }

    /**
     * 首页活动规则时间配置
     * @return
     */
    @RequestMapping("/getActivitiNotice")
    public JsonResult getActivitiNotice() {
        JSONObject object = new JSONObject();
        try {
            object.put("activityNotice", notice);
            object.put("activityTime", activityTime);
            object.put("firstOrg", firstOrg);
            object.put("secondOrg", secondOrg);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return sendSuccess(object);
    }

    /**
     * 根据用户步数计算积分
     * @param currentSteps
     * @return
     */
    public int countUserStepBelogLevel(Integer currentSteps) {
        List<String> integralList = Arrays.asList(integral_level01, integral_level02);
        for(int i = 0; i < integralList.size()-1; i++) {
            String last = integralList.get(i);
            String next = integralList.get(i+1);
            String[] lastResultStr = last.split("-");
            String[] nextResultStr = next.split("-");
            String lastStepsStr = lastResultStr[0];
            String lastIntegralStr = lastResultStr[1];
            String nextStepStr = nextResultStr[0];
            String nextIntegralStr = nextResultStr[1];
            int lastSteps = NumberUtils.str2Int(lastStepsStr);
            int lastIntegral = NumberUtils.str2Int(lastIntegralStr);
            int nextSteps = NumberUtils.str2Int(nextStepStr);
            int nextIntegral = NumberUtils.str2Int(nextIntegralStr);
            if(i == 0) {
                if(currentSteps < lastSteps) {
                    return 0;
                }
            }
            if(i + 1 == integralList.size() - 1) {
                if(currentSteps > nextIntegral) {
                    return nextIntegral;
                }
            }
            if(currentSteps > lastSteps && currentSteps <= nextSteps) {
                return lastIntegral;
            }

        }
        return 0;
    }
}