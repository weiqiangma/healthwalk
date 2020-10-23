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
        String data = "FkRa2zA8wnixkLCxMURV9Qqi7TAbsAdxEVh96SOd322++2fCCKA0BMQwpJaf9n5WQRxbOt1WUM3rVx3lHAWoWHNjlLwGoZe+FIhVWWXUNpTDEATjzr98kFaq5FBkvPYmKHx1lL7NDZ/GfVpghpIokDCWovD7yMMdjkxGsf9Oa8+dIrqALAXj7LG29DmhXCY+YO5BWasZSIq3LaveMaOLGsUV6qbqbEa6be/xnbp/kniroiJ2ZlB7VE7ehT/9HGqNKJubF2j3EsSlAOVyJvfAN9//LO2nkuIWjmD3dioiY1UFcc7Zu63r7g8wgxctflxzvxTOJW9X34geiLXESChyzb0NW91MWBprohyYcDBc2iLo9ipSguwc1UvSnaWPqIV/2t1ZjoGKuUQLZZcQOV2AibcQ07OwcUIKIszLfbQ9LJpgpqnryEwd5jbmIQ++G/Vq8WbN8PeSTZ3c4Ee7NS6+xdHYYAwhSBb1B8m1o9UKxOBKkDXcULFYYlH6xOgBcqO7kRD4MdwqfsgHABnioeqlqoLHsXYcIRBeYgGeP5qNW4sdT4yvSTyElBjPRxEIZ09CRXehN2DJQvFQug3tF2vL5+Y2nqiHJ//zf5EFwDnxvKNVKqha5zaiQg4VwS/z4kzDN5DKOOZUW0VmFjpZlnLUj7KPlS8qEXheFj66E9HlcOU9vR+WNdqBJ/GgvWWALJ640GcwBA2lt5ZEZnFMUK+TLwD8hgt6Qfl9tlHilLBeDB8YBEUPxd+IpdVE3uJvt4l/GVUXwxrBVvFHshteMozVKCHhGONgZ/fakG9hwZBlgESQ+iEZvjdfofYJcn15viYhBxM2oeF3SYVtCYNGXCvxym6s3hbY/IXbBNqqVX7n0t54W8/o1fuE+enc2omMRUqReuWvoow89jplLI3eJ/dgTslUA6IBEk098b/xil+PNkIgLjsQFpkskVqx6QC0FaRfP4qRODRVpyXYu+5bbBpj9bQqDzYXOmKf2/9mnTx060rG4eB8EToukzxDHaNo0qpl26RjswmMnl12gmTHlXvRnCz28IqYoVAl3soxUgHOlZlbnNashj6UWtVC3C7tGxZDpt5O1em2culnuaxpwm0O41DQWSxXi7fq/wi8xcQsGChib+vYbGxQtjONtCVyBoc/XPOdvSBQs8Hp+DVJ0jENHIzoV/2o0fDrcqUtlOY8Cyi7DPMfYiXABYbCt7GKEeI0RR6iRL4RVP2EiQpkNwwNr+2Jrk1Jm6+w+dPglgA9n4pLme13EA79R9GwQsWk7zXZMrkafOirfZM0vcJJHCRGSIB2Wkc1RzEf0ZzKBDZfcyfU7wqR382YmLxKQDEGqmGK8m1pkeMZgjBGCj51tx+M1/YPOd15vdgcd3z5eAuj7keD+l0mmeravRUlYwfbkSC7gCv89KlHiDE53FpdCoLhoYgaXJlXNkykzub0fRxUT1ju43IJ4e5T3orFD+SDEWOX7OKB9EYMd9h+ISmSH5Hkrj+mUZk2UNtpprbbNY8yc5iL3pz2Z3tXdoM4D5Jg6vwM6fgjNBf4Wv4Xkr4StrpLx1wb/30wC4FigC/W9uz9R7h+IK6QYdHkbYcVS9Dit9KNDzgbqlDpoK9jNWIkVO1Ozm7HC503VNl95Jxx+6oZK04YTVy9q+/qNHvW8HwL/po";
        String iv = "i7SSw5Jnbn7tGthhUwZT2g==";
        String sessionley="VRCVKj12WAylcnxKLhqqvw==";
        String result = WechatDecryptDataUtil.decrypt(appId, data, sessionley, iv);

        String data2="rIIRTQ76d84BNubon5E4gl65bDq866tYfq6UQJs60MQD99ffg+D+6OaIwC5wKFIhPF8kz/Xa+X/hHM/zqhBnqCHQcCGrKTCg9TXQVDxJjiyCVtYiAc2ffFqYpoJrQ9hYWAcaURu2rEpeSBv+WVPOGtyB+8TZaiB+hErkx7Kw+kw54z+kmwAbE8/mQkS74rOCyrhJriPUnhWfVzCluLcesrvDe0R7ppea/HCmH60h5NniXr+mEFS+8O2HvKpzoK/LhM+i+muB1yy4BQqBPKirwHgMdn3cApXB8C0zYm4KQrnKdf1V5nEwWVxWKWjhx4q9CyYPdBQpv//4oXtkCijyItgRg18M1Oo037flTgXyhUNKUp5B9GLbyX2mU/O6EbLB2lbdNXkBiEpwPidqGiKasHk7fXsSPUNc6/ZVQGKkmtt8RgKEhNRnxUL6FWF54jt5UcHCqrz/YN4aOS1zR7WoMMZh0fwepCeqdYqzZiDnyxXvOzOKnJEgI6xXVnD8yowayFaKcWiKSRCIfEAtWS5qlTcPrF3k7DwVe7WL+hj0MajYdrEXhDlj7Vgd+zypxtmNOpth/VQ2EjimzxSqIOqkmVdESkw1felm1mabZMY7lCys+ivvrodnTBfKgpP2J6DN9+KgSSZ7v457u16ouIuHnO8lII3Nir0jA7k2e3VAlBZ5E8b3wScJruvhwhhgUcZjO0+m2FKXWhODPvRWL6YyMFMlRqjd2F3aEYcmo5oxS6Lr9EJWy3SyzYCJBowSLtOWWUCVpdsp106pzDm7Ik8BPrRqyLYSn/PZrS2eeXfq1U/8I5sl/TirVjGhhB7b2UX9u0QsykAIIkqAV0jj5PGekigx6JAUTO3srpiqDzA4/10ujD4c81NbIfZ2NM88YDpQO33olfTC8TppzlTfQkibAAHw9Ztix+Xh5AL0fDN9dew3SjBAeMz90YkO2nyCLXx7pZIR3hOkSa/L/JRGB4/faLOhyjPIQu0fNkZFu2EyqbZ393SEjH1e3clqLEBMwNajyQ564TaqLlkUAQRZXOcrc8nNhmunvwIaHTvBH3tLk2nuhstm066RQM/3CpqWbSeKConCyt+W8Qd+SrQH8Ty3SGRg80QGLDicxsc5y6JndrR41y+WkgroFhRcHf5iXzLZxcCEQaOg5e2+QaqHAXIwbYJgaA7SOflJXX701eyYM7mx0Z5YqV+j7rmw3j4k0r0Yg98tWMYcVU9RZA9TRAmCU1PH4GI5x5wsYKFky3ax0a0PyZ7lmsuBpqsQBW8tO1sqN2TGwsNGsezva8XRjaJ2LXS3cC5Gy7NheWiwoqMay15GyNMU+KFdHgBjyXE0PIhSQIvCjKcdPD9g3E95KaKDKs+kFwNLCByJUGaQhqTL+RwVOl8KbQFw2P5GikqPhhj7J0/yuzfwpZK7NKV73GBEG+rojQoqn1Hc0XBWhPUVFqCGd1dtccNuUBf6jr2HyDesXYM2svXfIktEbXAtrN5qxwAE8qY9bOGmzTqSPrfHJ9XuSmpV1zE6RnMG5I0uhzXoynumBNNrUM15Ig7jZ9eUGd/3b0FmyfvgHLgjCFBeSkSx3lBs0tX6Bz0I7hm26boZdx1LPeq5ub7T6c1LyDjD2ElVJE5FIXZgMeD0ML7jrlwVMDcAhWDuy4XgozffE4wI";
        String iv2="fqhScVTc/axCvmzI8/UAZg==";
        String sessionley2="VRCVKj12WAylcnxKLhqqvw==";
        String result2=WechatDecryptDataUtil.decrypt(appId, data2, sessionley2, iv2);
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

}
