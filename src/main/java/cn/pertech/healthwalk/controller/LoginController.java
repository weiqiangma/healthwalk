package cn.pertech.healthwalk.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.CryptUtils;
import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.base.data.WxLoginResultData;
import cn.pertech.healthwalk.base.data.constant.Constant;
import cn.pertech.healthwalk.base.data.query.UserQuery;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.base.service.UserCacheService;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import cn.pertech.healthwalk.utils.WechatDecryptDataUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Date 2020/9/8 16:23
 * @Author mawkun
 */
@RestController
public class LoginController extends BaseController {

    @Value("${wx.AppId}")
    private String appId;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private UserCacheService userCacheService;

    @RequestMapping(value = "/login")
    public JsonResult login(String code) {
        JSONObject object = new JSONObject();
        WxLoginResultData resultData = wxApiServiceExt.getOpenIdByCode(code);
        //根据openID查询数据库中是否存在该用户，没有则添加
        UserQuery query = new UserQuery();
        query.setOpenId(resultData.getOpenId());
        //TODO 测试用
        User user = userServiceExt.getByEntity(query);
        if (user == null) {
            User addUser = new User();
            addUser.setOpenId(resultData.getOpenId());
            addUser.setCreateTime(new Date());
            addUser.setUpdateTime(new Date());
            userServiceExt.insert(addUser);
            user = userServiceExt.getByEntity(query);
        }
        /**
         * 1.判断用户是否有unionId,无unionId,直接返回去认证
         * 有unionId,根据unionId调用app接口查询用户信息，若查到且是已认证用户则是已认证，若查询不到，提示用户去认证
         */
        String unionId = user.getUnionId();
        int isAuth = 0;
        if(unionId != null) {
            User appUser = wxApiServiceExt.getUserByUnionId(unionId,user);
            if(appUser != null && appUser.getStatus() == Constant.USER_STATUS_ACTIVE) {
                try {
                    isAuth = 1;
                    user.setUserName(appUser.getUserName());
                    user.setAvatarUrl(appUser.getAvatarUrl());
                    user.setTeamName(appUser.getTeamName());
                    user.setTeamNo(appUser.getTeamNo());
                    user.setStatus(appUser.getStatus());
                    user.setIntegral(appUser.getIntegral());
                    user.setUnionId(appUser.getUnionId());
                    userServiceExt.update(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        if(appUser != null && appUser.getTeamId() == null) {
            isAuth = 2;
        }
        }
        String token = CryptUtils.md5Safe(resultData.getOpenId() + resultData.getSessionKey() + System.currentTimeMillis());
        UserSession session = new UserSession(token, user, resultData.getSessionKey());
        userCacheService.putUserSession(token, session);
        object.put("token", token);
        object.put("isAuth", isAuth);
        return sendSuccess(object);
    }
}
