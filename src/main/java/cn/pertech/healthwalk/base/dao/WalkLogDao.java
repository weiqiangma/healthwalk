package cn.pertech.healthwalk.base.dao;

import cn.pertech.healthwalk.base.entity.WalkLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

@Mapper
public interface WalkLogDao {

    WalkLog getById(@NotNull Long id);

    List<WalkLog> listByEntity(WalkLog walkLog);

    WalkLog getByEntity(WalkLog walkLog);

    List<WalkLog> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull WalkLog walkLog);

    int insertBatch(@NotEmpty List<WalkLog> list);

    int update(@NotNull WalkLog walkLog);

    int updateByField(@NotNull @Param("where") WalkLog where, @NotNull @Param("set") WalkLog set);

    int updateBatch(@NotEmpty List<WalkLog> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull WalkLog walkLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(WalkLog walkLog);
    
}