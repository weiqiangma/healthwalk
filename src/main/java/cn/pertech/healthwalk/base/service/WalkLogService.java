package cn.pertech.healthwalk.base.service;

import cn.pertech.healthwalk.base.dao.WalkLogDao;
import cn.pertech.healthwalk.base.entity.WalkLog;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * (WalkLog)实体类
 *
 * @author mawkun
 * @since 2020-10-09 13:34:20
 */
@Service
public class WalkLogService {

    @Resource(type = WalkLogDao.class)
    private WalkLogDao walkLogDao;

    public WalkLog getById(Long id) {
        return walkLogDao.getById(id);
    }

    public WalkLog getByEntity(WalkLog walkLog) {
        return walkLogDao.getByEntity(walkLog);
    }

    public List<WalkLog> listByEntity(WalkLog walkLog) {
        return walkLogDao.listByEntity(walkLog);
    }

    public List<WalkLog> listByIds(List<Long> ids) {
        return walkLogDao.listByIds(ids);
    }

    public int insert(WalkLog walkLog) {
        Date date = new Date();
        walkLog.setCreateTime(date);
        walkLog.setUpdateTime(date);
        return walkLogDao.insert(walkLog);
    }

    public int insertBatch(List<WalkLog> list) {
        return walkLogDao.insertBatch(list);
    }

    public int update(WalkLog walkLog) {
        walkLog.setUpdateTime(new Date());
        return walkLogDao.update(walkLog);
    }

    public int updateBatch(List<WalkLog> list) {
        return walkLogDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return walkLogDao.deleteById(id);
    }

    public int deleteByEntity(WalkLog walkLog) {
        return walkLogDao.deleteByEntity(walkLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return walkLogDao.deleteByIds(list);
    }

    public int countAll() {
        return walkLogDao.countAll();
    }
    
    public int countByEntity(WalkLog walkLog) {
        return walkLogDao.countByEntity(walkLog);
    }

}