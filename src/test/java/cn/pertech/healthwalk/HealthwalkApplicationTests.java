package cn.pertech.healthwalk;

import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.service.TeamServiceExt;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HealthwalkApplicationTests {

    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private TeamServiceExt teamServiceExt;

    @Test
    void contextLoads() {

    }

    @Test
    void testOperateUserIntegral() {
       Team team = wxApiServiceExt.getParentTeam("0040330017");
       System.out.println(team.toString());
    }

}
