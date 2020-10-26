package cn.pertech.healthwalk;

import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.service.TeamServiceExt;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import cn.pertech.healthwalk.utils.JsonUtils;
import cn.pertech.healthwalk.utils.StringUtils;
import cn.pertech.healthwalk.utils.WechatDecryptDataUtil;
import cn.pertech.healthwalk.utils.WxPKCS7Encoder;
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

import java.util.Arrays;
import java.util.List;

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
    void testWalkLog() {
        String appId = "wxe4aef37bfbed6290";
        String data = "A0x4LsiP1zim8MIUhXboXIV8HdjJEm8i7Q6bVnqYn6zp2ljp8OJqVGqyFlLdTD4SE1q4eNmgd74ejHuP1BC/PEJPVsOnjIs7g/Z6AZZLHM3VQwQZl0NmdFHw/08oeDWHzLd+LOtAj9ZHhDrC3BXHzVyT2nsECgBuHMdVf90rqil+3hEtYFxE5pJxqMW+xk3G2n35GkFHs8oLYn98AKcEWbnqSMxL9slElgE/UnJAItQeUHG+zw3Sx/FbIRJWoK6mtGYvpB9pWWhYrs/txBGItLJ/FKphsKoqOW11Fr87ljTxwE9Tl+SDYQLRowjBlrVzc+AoVmHCNWr7uaUN42HLOi6O/iV6copiJ0VP28zs/wY00etdNjJ7Hu+i80pc9482fyEwJ1y+S/210B6oXRQFVHYgihWeTwx5nASJ2KfRbPg0OJEiIq/pYi72/nICeJnOHWucwQOiOtJKPotWtQWe56hs+ypnUcMsXVwlYY6rueNpNBSl8UuPJsLukPiYQQx7xFcD5+KPXDM46/3KU5a82aO3yzYtC8SthZd7inD8i4RlFL1yODgIRIFJWV1XBdF1hjWsmyeM3coOgQco3qPbgR7qk+PYEOd6o4MNgAMqU3c+D/cQTPIoCdN1hGybAuqDfKRONIECJ9nyeMVZV3CnCSNME1zbsb14crD7Gu6VBAkksjX8qXcYXPbk9zUIOoVQVDwY7GMUIuzOIJDwwOjD+DvzcOip+bWYb1xpy9AYmD7J2wQrkqAxYEQ/3KiIdeU9jEwVS8qQbk27MrVRrgw5RKyMM6ibOQXEgZkZd2L6ZWKdygPclOk8vs19GUFLMkGaOfsgQfc9uw8Rdt0sc7UbyriOkoZKyCv8p769aqVllf0RQugN8GzX8tAxiZgSLvzvti0D06VwQ/H9hm0n17B5qyW+VObbY8CVRNy7E+9JlmGSt50tYTA9W+nn/BbqFKsu74THrdSY7NQWRFRE+Ie11opNwe6vWZKXcQ5yS/KvYA/L7YkF+tVS6mG2qQ88j/Gzw1ZtEVlIvWnJxmkBclSuUjeuUajzKeHoLi93s4iAk9rDSSnCCcJBwUw5CCSjVbR2qrN2Xbnzvzx4+fekixkChFcAWPKYmh5xDw4V2C7uEDqWKY77mIZRhx7P6F9IvSWOHe7juw8Hk+tshikbH+w374mGfCqEIrDpnmbQG/i2THEPMiNfiBAO6s0tgWExXrysfMKPGRsOAja3gB6BJVoycVU7i2jbmEUhWYGCvGrJj4zFZ4NweF3xMCaR5tnj0DM9Ujci/iR6YezAnt0NPzV6q7MM2OTa+Qnv4FVbyCE4b5yJ24nYmTMrXXrIGtWpp9Ow1tSYHiXIiNVANM586hBhgDyfhnL11t05o4szNZCQjpilP1d5nY2b/FuZcHSWrGIyrX17MBo13vkqVZFehSzb3iNFjjDAN7MXMQOaqYVu5sj9WsxcavQvFWGwKulggz+M19jL54MkHNu3/8aY1/DcMwtP022UfegKCtQWav0wp6ALstTux6OIMVpZRx7kF6C3TgS01LJ4jYJIKY334LitpZBqNgUwJlEU+rLupuINtUZM9/k9unFPF7Tlupvi1KyS472Cr+cpMEC/nIKiidOzXgtrM7kh6TTXFKkJa4sTJML0cJEESQRFWEFrUy/5oi42";
        String iv = "ni3RkG2T4IctwLhWGLcgTQ==";
        String sessionley="QtQFheR7zjMiEVLoVsAScQ==";
        String result = WechatDecryptDataUtil.decrypt(appId, data, sessionley, iv);
        System.out.println(result);
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

    @Test
    void testGetUserRank() {
        int  result = countUserStepBelogLevel(10001);
        System.out.println(result);
    }

    public int countUserStepBelogLevel(Integer currentSteps) {
        List<String> integralList = Arrays.asList("5000-5", "10000-5");
        for(int i = 0; i < integralList.size()-1; i++) {
            String last = integralList.get(i);
            String next = integralList.get(i+1);
            String[] lastResultStr = last.split("-");
            String[] nextResultStr = next.split("-");
            String lastStepsStr = lastResultStr[0];
            String lastIntegralStr = lastResultStr[1];
            String nextStepStr = nextResultStr[0];
            String nextIntegralStr = nextResultStr[1];
            int lastSteps = NumberUtils.str2Int(lastStepsStr);
            int lastIntegral = NumberUtils.str2Int(lastIntegralStr);
            int nextSteps = NumberUtils.str2Int(nextStepStr);
            int nextIntegral = NumberUtils.str2Int(nextIntegralStr);
            if(i == 0) {
                if(currentSteps < lastSteps) {
                    return 0;
                }
            }
            if(i + 1 == integralList.size() - 1) {
                if(currentSteps > nextIntegral) {
                    return nextIntegral;
                }
            }
            if(currentSteps > lastSteps && currentSteps <= nextSteps) {
                return lastIntegral;
            }

        }
        return 0;
    }

}
