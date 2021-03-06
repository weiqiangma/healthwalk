package cn.pertech.healthwalk.base.data.query;

import cn.pertech.healthwalk.base.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserQuery extends User {
    private Integer pageNo;
    private Integer pageSize;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
