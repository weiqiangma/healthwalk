package cn.pertech.healthwalk.base.data.query;

import cn.pertech.healthwalk.base.entity.WalkLog;
import lombok.Data;

/**
 * @Date 2020/10/12 15:05
 * @Author mawkun
 */
@Data
public class WalkLogQuery extends WalkLog {

    private String createTimeStart;
    private String createTimeEnd;
}
