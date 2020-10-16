package cn.pertech.healthwalk.dao;

import cn.pertech.healthwalk.base.dao.IntegralLogDao;
import cn.pertech.healthwalk.base.dao.WalkLogDao;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.query.WalkLogQuery;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.IntegralLog;
import cn.pertech.healthwalk.base.entity.WalkLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Date 2020/10/9 15:12
 * @Author mawkun
 */
@Mapper
public interface IntegralLogDaoExt extends IntegralLogDao {

    /**
     * 获取用户今日积分领取记录
     * @param userId
     * @param createTime
     * @return
     */
    List<IntegralLog> getUserTodayIntegralLog(@Param("userId") Long userId, @Param("createTime") String createTime);

}
