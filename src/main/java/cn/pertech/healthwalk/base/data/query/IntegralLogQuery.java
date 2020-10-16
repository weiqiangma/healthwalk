package cn.pertech.healthwalk.base.data.query;

import cn.pertech.healthwalk.base.entity.IntegralLog;
import lombok.Data;

/**
 * @Date 2020/10/13 13:27
 * @Author mawkun
 */
@Data
public class IntegralLogQuery extends IntegralLog {
    private Integer pageNum;
    private Integer pageSize;

    public void init() {
        if(this.pageNum == null) {
            this.pageNum = 1;
        }
        if(this.pageSize == null) {
            this.pageSize = 10;
        }
    }
}
