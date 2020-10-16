package cn.pertech.healthwalk.base.entity;

import java.util.Date;
import lombok.Data;
                                                        import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (WalkLog)实体类
 *
 * @author mawkun
 * @since 2020-10-16 15:30:25
 */
@Data 
public class WalkLog {
    
    private Long id;
    /**
    * userId
    */
    private Long userId;
    
    private Long teamId;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 团队名称
    */
    private String teamName;
    /**
    * 团队编码
    */
    private String teamNo;
    /**
    * 总步数
    */
    private Integer totalSteps;
    /**
    * 总距离
    */
    private Integer totalDistance;
    /**
    * 总时长
    */
    private Object totalTime;
    /**
    * 积分
    */
    private Integer integral;
    /**
    * 是否领取
    */
    private String isDraw;
    /**
    * 开始步数
    */
    private Integer startSteps;
    /**
    * 结束步数
    */
    private Integer endSteps;
    /**
    * 开始时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date startTime;
    /**
    * 更新时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date updateTime;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}