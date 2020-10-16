package cn.pertech.healthwalk.service;

import cn.pertech.healthwalk.base.dao.IntegralLogDao;
import cn.pertech.healthwalk.base.data.query.IntegralLogQuery;
import cn.pertech.healthwalk.base.data.vo.WalkLogVo;
import cn.pertech.healthwalk.base.entity.IntegralLog;
import cn.pertech.healthwalk.base.service.IntegralLogService;
import cn.pertech.healthwalk.dao.IntegralLogDaoExt;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Date 2020/10/13 13:31
 * @Author mawkun
 */
@Service
public class IntegralLogServiceExt extends IntegralLogService {

    @Resource
    private IntegralLogDaoExt integralLogDaoEx;

    public PageInfo<IntegralLog> list(IntegralLogQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<IntegralLog> list = integralLogDaoEx.listByEntity(query);
        return new PageInfo<IntegralLog>(list);
    }

    public JSONArray getUserTodayIntegralLog(Long userId, String createTime, List<String> walkLevels) {
        JSONArray array = new JSONArray();
        List<IntegralLog> list = integralLogDaoEx.getUserTodayIntegralLog(userId, createTime);
        Map<String, IntegralLog> map = list.stream().collect(Collectors.toMap(IntegralLog::getWalkLevel, a -> a, (k1, k2) -> k1));
        for(String level : walkLevels) {
            JSONObject object = new JSONObject();
            IntegralLog integralLog = map.get(level);
            if(integralLog == null) {
                //该档位积分未领取
                object.put("integralLevel", level);
                object.put("isDraw", 0);
            } else {
                object.put("integralLevel", level);
                object.put("isDraw", 1);
            }
            array.add(object);
        }
        return array;
    }
}
