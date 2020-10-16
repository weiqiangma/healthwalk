package cn.pertech.healthwalk;

import cn.pertech.healthwalk.base.entity.User;
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

    @Test
    void contextLoads() {

    }

    @Test
    void testOperateUserIntegral() {
        ///wxApiServiceExt.operateUserIntegral("ocjQ55h071yHM4XJRt2uXw8NrRD4");
    }

}
