package cn.pertech.healthwalk.dao;

import cn.pertech.healthwalk.base.dao.WalkLogDao;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.query.WalkLogQuery;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.WalkLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2020/10/9 15:12
 * @Author mawkun
 */
@Mapper
public interface WalkLogDaoExt extends WalkLogDao {

    /**
     * 统计用户步数和排名
     * @param userId
     * @return
     */
    List<WalkLogVo> statsRandAndSteps(@Param("userId") Long userId);

    /**
     * 获取用户今日待领取积分
     * @param userId
     * @param createTime
     * @return
     */
    WalkLog getUserTodayIntegral(@Param("userId") Long userId, @Param("createTime") String createTime);

    /**
     * 获取指定时间段内用户步数记录
     * @param query
     * @return
     */
    List<WalkLogVo> getWalkSteps(WalkLogQuery query);

    /**
     * 统计团队人员排名平均步数
     * @param teamNo
     * @return
     */
    UserStatVo statsTeamRankAndSteps(@Param("teamNo") String teamNo, @Param("createTime") String createTime);

    UserStatVo getUserRank(@Param("userId") Long userId, @Param("createTime") String createTime, @Param("teamId") String teamNo);

    /**
     * 计算用户排名及步数
     * @param userId
     * @param createTime
     * @param teamNo
     * @return
     */
    List<UserStatVo> countUserRank(@Param("userId") Long userId, @Param("createTime") String createTime, @Param("teamNo") String teamNo);

    /**
     * 团队排行榜
     * @param query
     * @return
     */
    List<TeamStatVo> getTeamRankList(StatQuery query);
}
