package cn.pertech.healthwalk;

import cn.pertech.common.http.HttpUtils;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.service.TeamServiceExt;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import cn.pertech.healthwalk.utils.JsonUtils;
import cn.pertech.healthwalk.utils.StringUtils;
import cn.pertech.healthwalk.utils.WechatDecryptDataUtil;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HealthwalkApplicationTests {

    @Value("${wx.AppId}")
    private String AppId;
    @Value("${wx.AppSecret}")
    private String AppSecret;
    @Value("${wx.appKey}")
    private String appKey;
    @Value("${wx.secretKey}")
    private String secretKey;
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
        System.out.println(team);
    }

    @Test
    public void getUserByUnionId() {
        String unionId = "ocjQ55rK5bvQPAQNuuERbNihaoq4";
        //String appKey="WkdBnKoNu!ocqOdhw92ry#9Ik2DZHI0$";
        //String secretKey="ACSZdgoABuN02THWN8FsQ7AroV9XXSkN";

        String appKey="WkdBnKoNu!ocqOdhw92ry#9Ik2DZHI0$";
        String secretKey = "ACSZdgoABuN02THWN8FsQ7AroV9XXSkN";
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String nonce = StringUtils.createRandomStr(32);
            String param = "appkey=" + appKey.toLowerCase() + "&nonce=" + nonce + "&timestamp=" + timeStamp + "&sign=" + secretKey.toLowerCase();
            String sign = SecureUtil.md5(param);

            //String url = "https://hyxt.nbgh.gov.cn/wechat-console-test/api/public/union/info?unionId=" + unionId;
            String url = "https://hyxt.nbgh.gov.cn/wechat-console/api/public/union/info?unionId=" + unionId;
            //String url = "http://lkj.vaiwan.com/wechat-console-test/api/public/union/info?unionId=" + unionId;
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = HttpClients.createDefault();
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("timeStamp", timeStamp);
            httpGet.setHeader("nonce", nonce);
            httpGet.setHeader("sign", sign);
            httpGet.setHeader("appKey", appKey);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = new String(HttpUtils.entityToByte(entity), "UTF-8");
            JSONObject object = JsonUtils.parseString(result);
            if ("200".equals(object.getString("code"))) {
                JSONObject resultObj = object.getJSONObject("data");
                String nickName = resultObj.getString("nickName");
                String headImg = resultObj.getString("headImg");
                String memberUnionCode = resultObj.getString("memberUnionCode");
                String memberUnionName = resultObj.getString("memberUnionName");
                Integer activeIntegral = resultObj.getInteger("activeIntegral");
                Integer bindStatus = resultObj.getInteger("bindStatus");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
