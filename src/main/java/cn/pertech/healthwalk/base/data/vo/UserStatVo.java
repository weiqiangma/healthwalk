package cn.pertech.healthwalk.base.data.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Date 2020/10/9 14:42
 * @Author mawkun
 */
@Data
@Accessors(chain=true)
public class UserStatVo {
    /**
     * 今日步数
     */
    private Integer todayStep;
    /**
     * 昨日步数
     */
    private Integer yestardayStep;
    /**
     * 今日排名
     */
    private Integer todayRank;
    /**
     * 昨日步数
     */
    private Integer yestardayRank;
    /**
     * 累计天数
     */
    private Integer totalDays;
    /**
     * 团队人数
     */
    private Integer peopleAmount;
    /**
     * 平均步数
     */
    private Integer avgSteps;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 团队编码
     */
    private String teamNO;
    /**
     * 团队排名
     */
    private Integer teamRank;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * unionId
     */
    private String unionId;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 用户排名
     */
    private Integer userRank;
    /**
     * 用户步数
     */
    private Integer userSteps;

    private List<String> integralList;

    private JSONArray integralLog;

    public UserStatVo() {}

    public UserStatVo init() {
        if(this.todayStep == null) this.todayStep = 0;
        if(this.yestardayStep == null) this.yestardayStep = 0;
        if(this.todayRank == null) this.todayRank = 0;
        if(this.yestardayRank == null) this.yestardayRank = 0;
        if(this.totalDays == null) this.totalDays = 0;
        return this;
    }
}
