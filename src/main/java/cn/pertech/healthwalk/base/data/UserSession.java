package cn.pertech.healthwalk.base.data;

import cn.pertech.healthwalk.base.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSession {

    private Long id;

    private String userName;

    private String mobile;

    private Integer integral;

    private String token;

    private String openId;

    private String sessionKey;

    private List<Long> shopIdList;

    public UserSession(){}

    public UserSession(String token,User user, String sessionKey) {
        this.id = user.getId();
        this.openId = user.getOpenId();
        this.sessionKey = sessionKey;
        this.integral = user.getIntegral();
        this.token = token;
        this.userName = user.getUserName();
        this.mobile = user.getMobile();
        this.integral = user.getIntegral();
    }
}
