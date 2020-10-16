package cn.pertech.healthwalk.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (User)实体类
 *
 * @author mawkun
 * @since 2020-10-16 11:57:14
 */
@Data
public class User {
    /**
     * ID
     */
    private Long id;
    /**
     * team_id
     */
    private Long teamId;
    /**
     * open_id
     */
    private String openId;
    /**
     * union_id
     */
    private String unionId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 微信头像
     */
    private String avatarUrl;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 工会组织编码
     */
    private String teamNo;
    /**
     * 今日步数
     */
    private Integer todayStep;
    /**
     * 今日排名
     */
    private Integer todayRank;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 用户状态
     */
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}