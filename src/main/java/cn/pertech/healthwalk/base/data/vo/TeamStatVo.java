package cn.pertech.healthwalk.base.data.vo;

import lombok.Data;

/**
 * @Date 2020/10/9 16:32
 * @Author mawkun
 */
@Data
public class TeamStatVo {

    private Long id;
    private Long teamId;
    private String teamName;
    private String teamNo;
    private Integer teamRank;
    private Integer peopleAmount;
    private Integer avgSteps;
    private Integer rank;
}
