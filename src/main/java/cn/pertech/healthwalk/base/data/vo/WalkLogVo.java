package cn.pertech.healthwalk.base.data.vo;

import cn.pertech.healthwalk.base.entity.WalkLog;
import lombok.Data;

/**
 * @Date 2020/10/9 15:29
 * @Author mawkun
 */
@Data
public class WalkLogVo extends WalkLog {
    private String type;
}
