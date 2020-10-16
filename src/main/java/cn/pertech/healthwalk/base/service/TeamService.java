package cn.pertech.healthwalk.base.service;

import cn.pertech.healthwalk.base.dao.TeamDao;
import cn.pertech.healthwalk.base.entity.Team;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (Team)实体类
 *
 * @author mawkun
 * @since 2020-10-09 13:21:48
 */
@Service
public class TeamService {

    @Resource(type = TeamDao.class)
    private TeamDao teamDao;

    public Team getById(Long id) {
        return teamDao.getById(id);
    }

    public Team getByEntity(Team team) {
        return teamDao.getByEntity(team);
    }

    public List<Team> listByEntity(Team team) {
        return teamDao.listByEntity(team);
    }

    public List<Team> listByIds(List<Long> ids) {
        return teamDao.listByIds(ids);
    }

    public int insert(Team team) {
        Date date = new Date();
        return teamDao.insert(team);
    }

    public int insertBatch(List<Team> list) {
        return teamDao.insertBatch(list);
    }

    public int update(Team team) {
        return teamDao.update(team);
    }

    public int updateBatch(List<Team> list) {
        return teamDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return teamDao.deleteById(id);
    }

    public int deleteByEntity(Team team) {
        return teamDao.deleteByEntity(team);
    }
  
    public int deleteByIds(List<Long> list) {
        return teamDao.deleteByIds(list);
    }

    public int countAll() {
        return teamDao.countAll();
    }
    
    public int countByEntity(Team team) {
        return teamDao.countByEntity(team);
    }

}