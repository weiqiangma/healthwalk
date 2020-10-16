package cn.pertech.healthwalk.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (IntegralLog)实体类
 *
 * @author mawkun
 * @since 2020-10-16 15:00:00
 */
@Data
public class IntegralLog {

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 奖励积分
     */
    private Integer integral;
    /**
     * 剩余积分
     */
    private Integer residueIntegral;
    /**
     * 领取时步数
     */
    private Integer walkSteps;
    /**
     * 步数档次
     */
    private String walkLevel;
    /**
     * 领取时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    /**
     * 是否同步
     */
    private Integer isSynchronized;

}