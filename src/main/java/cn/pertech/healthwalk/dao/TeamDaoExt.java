package cn.pertech.healthwalk.dao;

import cn.pertech.healthwalk.base.dao.TeamDao;
import cn.pertech.healthwalk.base.entity.Team;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2020/10/9 15:44
 * @Author mawkun
 */
public interface TeamDaoExt extends TeamDao {

    List<Team> getTeamListByPid(@Param("pid") Long pid);

    Team getTeamByFirst3Str(@Param("teamNo") String teamNo );

    List<Team> getTeamListByTeamType(@Param("list") List<Integer> list);
}
