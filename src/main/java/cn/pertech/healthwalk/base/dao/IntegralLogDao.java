package cn.pertech.healthwalk.base.dao;

import cn.pertech.healthwalk.base.entity.IntegralLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

@Mapper
public interface IntegralLogDao {

    IntegralLog getById(@NotNull Long id);

    List<IntegralLog> listByEntity(IntegralLog integralLog);

    IntegralLog getByEntity(IntegralLog integralLog);

    List<IntegralLog> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull IntegralLog integralLog);

    int insertBatch(@NotEmpty List<IntegralLog> list);

    int update(@NotNull IntegralLog integralLog);

    int updateByField(@NotNull @Param("where") IntegralLog where, @NotNull @Param("set") IntegralLog set);

    int updateBatch(@NotEmpty List<IntegralLog> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull IntegralLog integralLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(IntegralLog integralLog);
    
}