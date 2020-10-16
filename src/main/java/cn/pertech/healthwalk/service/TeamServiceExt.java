package cn.pertech.healthwalk.service;

import cn.pertech.common.cache.CacheService;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.service.TeamService;
import cn.pertech.healthwalk.dao.TeamDaoExt;
import cn.pertech.healthwalk.dao.WalkLogDaoExt;
import cn.pertech.healthwalk.utils.TimeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Date 2020/10/9 15:40
 * @Author mawkun
 */
@Service
public class TeamServiceExt extends TeamService {

    @Resource
    WalkLogDaoExt walkLogDaoExt;
    @Resource
    TeamDaoExt teamDaoExt;
    @Resource
    UserServiceExt userServiceExt;

    public List<TeamStatVo> getTeamRank(StatQuery query) {
        return userServiceExt.countTeamRankList(query);
    }

    public Team getTeamByTeamNo(String teamNo) {
        Team query = new Team();
        query.setTeamNo(teamNo);
        return teamDaoExt.getByEntity(query);
    }
}
