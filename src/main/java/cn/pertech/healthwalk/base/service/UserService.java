package cn.pertech.healthwalk.base.service;

import cn.pertech.healthwalk.base.dao.UserDao;
import cn.pertech.healthwalk.base.entity.User;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * (User)实体类
 *
 * @author mawkun
 * @since 2020-10-09 13:27:08
 */
@Service
public class UserService {

    @Resource(type = UserDao.class)
    private UserDao userDao;

    public User getById(Long id) {
        return userDao.getById(id);
    }

    public User getByEntity(User user) {
        return userDao.getByEntity(user);
    }

    public List<User> listByEntity(User user) {
        return userDao.listByEntity(user);
    }

    public List<User> listByIds(List<Long> ids) {
        return userDao.listByIds(ids);
    }

    public int insert(User user) {
        Date date = new Date();
        return userDao.insert(user);
    }

    public int insertBatch(List<User> list) {
        return userDao.insertBatch(list);
    }

    public int update(User user) {
        return userDao.update(user);
    }

    public int updateBatch(List<User> list) {
        return userDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return userDao.deleteById(id);
    }

    public int deleteByEntity(User user) {
        return userDao.deleteByEntity(user);
    }
  
    public int deleteByIds(List<Long> list) {
        return userDao.deleteByIds(list);
    }

    public int countAll() {
        return userDao.countAll();
    }
    
    public int countByEntity(User user) {
        return userDao.countByEntity(user);
    }

}