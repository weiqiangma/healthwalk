package cn.pertech.healthwalk.base.service;

import cn.pertech.healthwalk.base.dao.IntegralLogDao;
import cn.pertech.healthwalk.base.entity.IntegralLog;
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
public class IntegralLogService {

    @Resource
    private IntegralLogDao integralLogDao;

    public IntegralLog getById(Long id) {
        return integralLogDao.getById(id);
    }

    public IntegralLog getByEntity(IntegralLog integralLog) {
        return integralLogDao.getByEntity(integralLog);
    }

    public List<IntegralLog> listByEntity(IntegralLog integralLog) {
        return integralLogDao.listByEntity(integralLog);
    }

    public List<IntegralLog> listByIds(List<Long> ids) {
        return integralLogDao.listByIds(ids);
    }

    public int insert(IntegralLog integralLog) {
        Date date = new Date();
        integralLog.setCreateTime(date);
        return integralLogDao.insert(integralLog);
    }

    public int insertBatch(List<IntegralLog> list) {
        return integralLogDao.insertBatch(list);
    }

    public int update(IntegralLog integralLog) {
        return integralLogDao.update(integralLog);
    }

    public int updateBatch(List<IntegralLog> list) {
        return integralLogDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return integralLogDao.deleteById(id);
    }

    public int deleteByEntity(IntegralLog integralLog) {
        return integralLogDao.deleteByEntity(integralLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return integralLogDao.deleteByIds(list);
    }

    public int countAll() {
        return integralLogDao.countAll();
    }
    
    public int countByEntity(IntegralLog integralLog) {
        return integralLogDao.countByEntity(integralLog);
    }

}