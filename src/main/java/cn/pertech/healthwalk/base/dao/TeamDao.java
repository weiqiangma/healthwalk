package cn.pertech.healthwalk.base.dao;

import cn.pertech.healthwalk.base.entity.Team;
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
public interface TeamDao {

    Team getById(@NotNull Long id);

    List<Team> listByEntity(Team team);

    Team getByEntity(Team team);

    List<Team> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull Team team);

    int insertBatch(@NotEmpty List<Team> list);

    int update(@NotNull Team team);

    int updateByField(@NotNull @Param("where") Team where, @NotNull @Param("set") Team set);

    int updateBatch(@NotEmpty List<Team> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull Team team);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(Team team);
    
}