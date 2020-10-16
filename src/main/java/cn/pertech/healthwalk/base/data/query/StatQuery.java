package cn.pertech.healthwalk.base.data.query;

import lombok.Data;

import java.util.Date;

/**
 * @Date 2020/10/12 10:43
 * @Author mawkun
 */
@Data
public class StatQuery {
    private String teamNo;
    /**
     * 1：今日，2：月度，3：季度，4：年度
     */
    private Integer type;
    /**
     * 1：团队榜，2：区县产业榜
     */
    private Integer kind;
    private Date timeStart;
    private Date timeEnd;
    private String formatCode;
    private Integer dataCount;
}
