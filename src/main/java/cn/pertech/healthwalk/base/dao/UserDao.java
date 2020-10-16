package cn.pertech.healthwalk.base.dao;

import cn.pertech.healthwalk.base.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * (Team)实体类
 *
 * @author mawkun
 * @since 2020-10-09 13:21:48
 */
@Mapper
public interface UserDao {

    User getById(@NotNull Long id);

    List<User> listByEntity(User user);

    User getByEntity(User user);

    List<User> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull User user);

    int insertBatch(@NotEmpty List<User> list);

    int update(@NotNull User user);

    int updateByField(@NotNull @Param("where") User where, @NotNull @Param("set") User set);

    int updateBatch(@NotEmpty List<User> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull User user);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(User user);
    
}