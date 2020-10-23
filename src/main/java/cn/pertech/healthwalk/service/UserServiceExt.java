package cn.pertech.healthwalk.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.healthwalk.base.data.constant.Constant;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.query.WalkLogQuery;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.IntegralLog;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.base.entity.WalkLog;
import cn.pertech.healthwalk.base.service.UserService;
import cn.pertech.healthwalk.dao.UserDaoExt;
import cn.pertech.healthwalk.dao.WalkLogDaoExt;
import cn.pertech.healthwalk.utils.StringUtils;
import cn.pertech.healthwalk.utils.TimeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2020/10/9 14:09
 * @Author mawkun
 */
@Service
public class UserServiceExt extends UserService {

    @Value("${INTEGRAL_LEVEL01}")
    private String integral_level01;
    @Value("${INTEGRAL_LEVEL02}")
    private String integral_level02;
    @Resource
    private UserDaoExt userDaoExt;
    @Resource
    private WalkLogDaoExt walkLogDaoExt;
    @Resource
    private WalkLogServiceExt walkLogServiceExt;
    @Resource
    private WxApiServiceExt wxApiServiceExt;
    @Resource
    private TeamServiceExt teamServiceExt;
    @Resource
    private IntegralLogServiceExt integralLogServiceExt;

    /**
     * 统计用户步数和排名
     * @return
     */
    public UserStatVo statsRankAndSteps(Long userId) {
        UserStatVo statVo = new UserStatVo();
        User user = userDaoExt.getById(userId);
        if(user != null) {
            int todayStep = 0;
            int todayRank = 0;
            int yesterdayStep = 0;
            int yesterdayRank = 0;
            String today = DateUtils.getCurrDate("yyyy-MM-dd");
            String yesterday = DateUtils.formatTime(TimeUtils.getLastDay());
            UserStatVo todayStatVo = walkLogDaoExt.getUserRank(userId, today, null);
            UserStatVo yesterdayStatVo = walkLogDaoExt.getUserRank(userId, yesterday, null);
            todayStep = (todayStatVo == null) ? 0 : todayStatVo.getUserSteps();
            todayRank = (todayStatVo == null) ? 0 : todayStatVo.getUserRank();
            yesterdayStep = (yesterdayStatVo == null) ? 0 : yesterdayStatVo.getUserSteps();
            yesterdayRank = (yesterdayStatVo == null) ? 0 : yesterdayStatVo.getUserRank();
            statVo.setTodayStep(todayStep);
            statVo.setTodayRank(todayRank);
            statVo.setYestardayStep(yesterdayStep);
            statVo.setYestardayRank(yesterdayRank);
            //团队排名统计
            if(user.getTeamId() != null) {
                UserStatVo userStatVo = walkLogDaoExt.statsTeamRankAndSteps(user.getTeamNo(), today);
                if(userStatVo != null) {
                    statVo.setAvgSteps(userStatVo.getAvgSteps());
                    statVo.setPeopleAmount(userStatVo.getPeopleAmount());
                    statVo.setTeamRank(userStatVo.getTeamRank());
                }
            }
        }
        //累计参与天数
        WalkLog query = new WalkLog();
        query.setUserId(userId);
        List<WalkLogVo> list = walkLogDaoExt.statsRandAndSteps(userId);
        if(list.size() > 0) {
            statVo.setTotalDays(list.size());
        }
        return statVo;
    }

    public JSONArray getUserWalkLog(WalkLogQuery query) {
        Date sDate = new Date();
        JSONArray array = new JSONArray();
        String eTime = DateUtils.format("yyyy-MM-dd", sDate);
        String sTime = TimeUtils.getPastDate(7, sDate);
        query.setCreateTimeStart(sTime);
        query.setCreateTimeEnd(eTime);
        List<WalkLogVo> walkLogList = walkLogDaoExt.getWalkSteps(query);
        Map<String, WalkLogVo> map = walkLogList.stream().collect(Collectors.toMap(WalkLogVo::getType, a -> a, (k1, k2) -> k1));

        List<String> dateList = TimeUtils.pastDay(eTime);
        for(String date : dateList) {
            JSONObject object = new JSONObject();
            object.put("date", date);
            WalkLogVo walkLogVo = map.get(date);
            if(walkLogVo != null) {
                object.put("walkSteps", walkLogVo.getTotalSteps());
            } else {
                object.put("walkSteps", 0);
            }
            array.add(object);
        }
        return array;
    }

    public void operateUserIntegral(User user) {
        WalkLog walkLog = walkLogServiceExt.getUserTodayIntegral(user.getId(), DateUtils.getCurrDate("yyyy-MM-dd"));
        Integer totalSteps = walkLog.getTotalSteps();
        //获取用户积分领取记录
        List<String> integralList = Arrays.asList(integral_level01, integral_level02);
        JSONArray array = integralLogServiceExt.getUserTodayIntegralLog(user.getId(), DateUtils.getCurrDate("yyyy-MM-dd"), integralList);
        for(int i = 0; i <array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String lastIntegralLevel = object.getString("integralLevel");
            String[] lastResultStr = lastIntegralLevel.split("-");
            //领取该档积分所需步数
            int lastType = NumberUtils.str2Int(lastResultStr[0]);
            int lastIntegral = NumberUtils.str2Int(lastResultStr[1]);
            Integer lastIsDraw = object.getInteger("isDraw");
            //该档积分未领取
            if(lastIsDraw == Constant.INTEGRAL_NOT_TAKE) {
                if(totalSteps > lastType) {
                    //生成用户领取记录
                    IntegralLog integralLog = new IntegralLog();
                    integralLog.setUserId(user.getId());
                    integralLog.setIntegral(lastIntegral);
                    integralLog.setUserName(user.getUserName());
                    integralLog.setWalkSteps(totalSteps);
                    integralLog.setWalkLevel(lastIntegralLevel);
                    integralLog.setCreateTime(new Date());
                    if (user.getUserName() != null) {
                        integralLog.setUserName(user.getUserName());
                    }
                    if (user.getIntegral() != null) {
                        integralLog.setResidueIntegral(user.getIntegral() + lastIntegral);
                    }
                    integralLogServiceExt.insert(integralLog);
                    //调用api增加用户积分接口，如果操作成功更新本地用户积分,否则删除领取的积分记录
                    int flag = wxApiServiceExt.operateUserIntegral(user.getUnionId(), Constant.OPTION_TYPE_ADD, lastIntegral, integralLog.getId());
                    if(flag > 0) {
                        //更新积分记录为已同步
                        integralLog.setIsSynchronized(Constant.INTEGRAL_IS_SYNCHRONIZED);
                        integralLogServiceExt.update(integralLog);
                        if (user.getIntegral() != null) {
                            user.setIntegral(user.getIntegral() + lastIntegral);
                        } else {
                            user.setIntegral(lastIntegral);
                        }
                        userDaoExt.update(user);
                    } else {
                        integralLogServiceExt.deleteById(integralLog.getId());
                    }
                }
            }
        }
    }

    /**
     * 计算用户排名
     * @param userId
     * @param teamNo
     */
    public UserStatVo countUserRank(Long userId, String teamNo) {
        UserStatVo resultStatVo = new UserStatVo();
        String today = DateUtils.getCurrDate("yyyy-MM-dd");
        String yesterday = DateUtils.formatTime(TimeUtils.getLastDay());
        List<UserStatVo> todayStatVos = walkLogDaoExt.countUserRank(userId, today, teamNo);
        List<UserStatVo> yesterdayStatVos = walkLogDaoExt.countUserRank(userId, yesterday, teamNo);
        //今日排名、今日步数
        for(int i = 0; i < todayStatVos.size(); i++) {
            UserStatVo userStatVo = todayStatVos.get(i);
            if(userId.equals(userStatVo.getUserId())) {
                resultStatVo.setTodayStep(userStatVo.getUserSteps());
                resultStatVo.setTodayRank(i+1);
            }
        }
        //昨日排名、昨日步数
        for(int i = 0; i < yesterdayStatVos.size(); i++) {
            UserStatVo userStatVo = yesterdayStatVos.get(i);
            if(userId.equals(userStatVo.getUserId())) {
                resultStatVo.setYestardayStep(userStatVo.getUserSteps());
                resultStatVo.setYestardayRank(i+1);
            }
        }
        //累计参与天数
        WalkLog query = new WalkLog();
        query.setUserId(userId);
        List<WalkLogVo> list = walkLogDaoExt.statsRandAndSteps(userId);
        if(list.size() > 0) {
            resultStatVo.setTotalDays(list.size());
        }
        //团队排名统计
        if(teamNo != null) {
            Team team = teamServiceExt.getTeamByTeamNo(teamNo);
            StatQuery teamQuery = new StatQuery();
            teamQuery.setTimeStart(new Date());
            teamQuery.setTimeEnd(DateUtils.addDay(new Date(),1));
            teamQuery.setTeamNo(teamNo);
            if(team.getTeamType() == Constant.TEAM_TYPE_BASIC) {
                teamQuery.setKind(1);
            } else {
                teamQuery.setKind(2);
            }
            TeamStatVo teamStatVo = countTeamRank(teamQuery);
            if(teamStatVo != null) {
                resultStatVo.setAvgSteps(teamStatVo.getAvgSteps());
                resultStatVo.setPeopleAmount(teamStatVo.getPeopleAmount());
                resultStatVo.setTeamRank(teamStatVo.getTeamRank());
            }
        }
        return resultStatVo;
    }

    /**
     * 团队排行榜
     * @param query
     * @return
     */
    public List<TeamStatVo> countTeamRankList(User user, StatQuery query) {
        /**
         * kind=1，查询区县产业排行榜。kind=2查询团队榜
         * 获取该父级工会下所有子工会步数和参与人数
         */
        List<TeamStatVo> resultStatVos = new ArrayList<>();
        if(query.getKind() != null && query.getKind() == 2) {
            List<Integer> teamStatusList = Arrays.asList(Constant.TEAM_TYPE_COUNTRY_TOWN, Constant.TEAM_TYPE_AREA, Constant.TEAM_TYPE_INDUSTRIAL);
            List<Team> teamList = teamServiceExt.getTeamListByStatus(teamStatusList);
            for(Team team : teamList) {
                int flag = 0;
                TeamStatVo resultTeamVo = new TeamStatVo();
                List<Team> childrenList = teamServiceExt.getTeamListByPid(team.getId());
                if(childrenList.size() > 0) {
                    List<Long> childrenIdList = childrenList.stream().map(Team::getId).collect(Collectors.toList());
                    //把父工会id加入idList一并纳入搜索
                    childrenIdList.add(team.getId());
                    resultTeamVo = walkLogDaoExt.getTopTeamStepAndPeoAmount(childrenIdList, query.getTimeStart(), query.getTimeEnd());
                    resultStatVos.add(resultTeamVo);
                } else {
                    List<Long> childrentList = new ArrayList<>();
                    childrentList.add(team.getId());
                    resultTeamVo = walkLogDaoExt.getTopTeamStepAndPeoAmount(childrentList, query.getTimeStart(), query.getTimeEnd());
                    resultStatVos.add(resultTeamVo);
                }
                if(user.getTeamId() != null) {
                    if (user.getTeamId().equals(team.getId())) {
                        resultTeamVo.setFlag(1);
                    } else {
                        resultTeamVo.setFlag(0);
                    }
                }
                resultTeamVo.setFlag(flag);
                resultTeamVo.setTeamName(team.getTeamName());
                resultTeamVo.setTeamId(team.getId());
            }
        }
        if(query.getKind() != null && query.getKind() == 1) {
            resultStatVos = walkLogDaoExt.getTeamRankList(query);
            Long a = 0L;
            if(user.getTeamId() != null) {
                a = user.getTeamId();
            }

            for(TeamStatVo teamStatVo : resultStatVos) {
                Long b = teamStatVo.getTeamId();
                if(a.equals(b)) {
                    teamStatVo.setFlag(1);
                } else {
                    teamStatVo.setFlag(0);
                }
            }
        }
        //根据平均步数和id排序
        List<TeamStatVo> sortList = resultStatVos.stream().sorted(Comparator.comparing(TeamStatVo::getAvgSteps).reversed()).collect(Collectors.toList());
        return sortList;
    }

    /**
     * 获取团队排名
     * @param query
     * @return
     */
    public TeamStatVo countTeamRank(StatQuery query) {
        TeamStatVo resultStatVo = new TeamStatVo();
        List<TeamStatVo> list = walkLogDaoExt.getTeamRankList(query);
        List<TeamStatVo> sortList = list.stream().sorted(Comparator.comparing(TeamStatVo::getAvgSteps).reversed()).collect(Collectors.toList());
        for(int i = 0; i < sortList.size(); i++) {
            TeamStatVo teamStatVo = sortList.get(i);
            if(StringUtils.equals(teamStatVo.getTeamNo(), query.getTeamNo())) {
                teamStatVo.setTeamRank(i+1);
                resultStatVo = teamStatVo;
            }
        }
        return resultStatVo;
    }

    public User getUserByUnionId(String unionId) {
        User query = new User();
        query.setUnionId(unionId);
        return userDaoExt.getByEntity(query);
    }
}
