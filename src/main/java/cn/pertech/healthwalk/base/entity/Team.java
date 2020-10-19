package cn.pertech.healthwalk.base.entity;

import java.util.Date;
import lombok.Data;
                        import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Team)实体类
 *
 * @author mawkun
 * @since 2020-10-19 15:57:42
 */
@Data 
public class Team {
    
    private Long id;
    
    private Long pid;
    /**
    * 团队编号
    */
    private String teamNo;
    /**
    * 团队名称
    */
    private String teamName;
    /**
    * 团队类型
    */
    private Integer teamType;
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