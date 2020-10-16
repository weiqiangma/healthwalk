package cn.pertech.healthwalk.service;

import cn.pertech.common.utils.DateUtils;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.WalkLog;
import cn.pertech.healthwalk.base.service.WalkLogService;
import cn.pertech.healthwalk.dao.WalkLogDaoExt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Date 2020/10/10 13:16
 * @Author mawkun
 */
@Service
public class WalkLogServiceExt extends WalkLogService {

    @Resource
    private WalkLogDaoExt walkLogDaoExt;

    public UserStatVo statsTeamRankAndSteps(String teamNo){
        return walkLogDaoExt.statsTeamRankAndSteps(teamNo, DateUtils.getCurrDate("yyyy-MM-dd"));
    }

    public WalkLog getUserTodayIntegral(Long userId, String createTime) {
        return walkLogDaoExt.getUserTodayIntegral(userId, createTime);
    }

    /**
     * 获取用户某天的步数和排名
     * @param userId
     * @param createTime
     * @return
     */
    public UserStatVo getUserRank(Long userId, String createTime, String teamNo) {
        return walkLogDaoExt.getUserRank(userId, createTime, null);
    }
}
