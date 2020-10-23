package cn.pertech.healthwalk.service;

import cn.pertech.common.cache.CacheService;
import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.common.utils.XmlUtils;
import cn.pertech.healthwalk.base.data.WxLoginResultData;
import cn.pertech.healthwalk.base.data.constant.Constant;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.base.service.UserService;
import cn.pertech.healthwalk.utils.JsonUtils;
import cn.pertech.healthwalk.utils.StringUtils;
import cn.pertech.healthwalk.utils.WechatDecryptDataUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2020/9/8 16:42
 * @Author mawkun
 */
@Slf4j
@Service
public class WxApiServiceExt {
    @Value("${wx.AppId}")
    private String AppId;
    @Value("${wx.AppSecret}")
    private String AppSecret;
    @Value("${wx.appKey}")
    private String appKey;
    @Value("${wx.secretKey}")
    private String secretKey;

    private static String profilesActive;

    @Resource
    TeamServiceExt teamServiceExt;
    @Resource
    UserServiceExt userServiceExt;

    /**
     * 根据code获取用户openId
     *
     * @param code
     * @return
     */
    public WxLoginResultData getOpenIdByCode(String code) {
        WxLoginResultData loginResult = new WxLoginResultData();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + AppId + "&secret=" + AppSecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            String openId = object.getString("openid");
            String sessionKey = object.getString("session_key");
            loginResult.setOpenId(openId);
            loginResult.setSessionKey(sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResult;
    }

    public User getUserByUnionId(String unionId, User user) {
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String nonce = StringUtils.createRandomStr(32);
            String param = "appkey=" + appKey.toLowerCase() +"&nonce=" + nonce + "&timestamp=" + timeStamp +"&sign=" + secretKey.toLowerCase();
            String sign = SecureUtil.md5(param);

            //String url = "https://hyxt.nbgh.gov.cn/wechat-console-test/api/public/union/info?unionId=" + unionId;
            String url = "https://hyxt.nbgh.gov.cn/wechat-console/api/public/union/info?unionId=" + unionId;
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = HttpClients.createDefault();
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("timeStamp", timeStamp);
            httpGet.setHeader("nonce", nonce);
            httpGet.setHeader("sign", sign);
            httpGet.setHeader("appKey", appKey);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = new String(HttpUtils.entityToByte(entity),"UTF-8");
            JSONObject object = JsonUtils.parseString(result);
            if("200".equals(object.getString("code"))) {
                JSONObject resultObj = object.getJSONObject("data");
                String nickName = resultObj.getString("nickName");
                String headImg = resultObj.getString("headImg");
                String memberUnionCode = resultObj.getString("memberUnionCode");
                String memberUnionName = resultObj.getString("memberUnionName");
                Integer activeIntegral = resultObj.getInteger("activeIntegral");
                Integer bindStatus = resultObj.getInteger("bindStatus");
                log.info("nickName:" + nickName +",memberUnionCode:" + memberUnionCode + ",Integeral:" + activeIntegral.toString() + ",status:" + bindStatus);
                //TODO 测试代码
                if(!"pro".equalsIgnoreCase(WxApiServiceExt.profilesActive)) {
                    /**
                     * 根据unionId如取不到用户生成随机用户，
                     * 能取到用户，如果用户信息为空，创建随机信息
                     */
                    User userQuery = new User();
                    userQuery.setUnionId(unionId);
                    User resultUser = userServiceExt.getByEntity(userQuery);
                    if (resultUser == null || resultUser.getStatus() == Constant.USER_STATUS_LOCK) {
                        if (nickName == null) {
                            nickName = getName();
                        }
                        Team team = createRandomTeam();
                        if (memberUnionCode == null) {
                            memberUnionCode = team.getTeamNo();
                        }
                        if (memberUnionName == null) {
                            memberUnionName = team.getTeamName();
                        }
                        if (activeIntegral == null) {
                            activeIntegral = createRandomIntegral();
                        }
                        if (headImg == null) {
                            headImg = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3491929439,4106440758&fm=26&gp=0.jpg";
                        }
                    } else {
                        nickName = user.getUserName();
                        memberUnionCode = user.getTeamNo();
                        memberUnionName = user.getTeamName();
                        activeIntegral = user.getIntegral();
                    }
                    bindStatus = 1;
                }

                if(unionId != null) {
                    user.setUnionId(unionId);
                }
                if(headImg != null) {
                    user.setAvatarUrl(headImg);
                } else {
                    user.setAvatarUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3491929439,4106440758&fm=26&gp=0.jpg");
                }
                if(nickName != null) {
                    user.setUserName(nickName);
                }
                if(memberUnionCode != null) {
                    user.setTeamNo(memberUnionCode);
                }
                if(memberUnionName != null) {
                    user.setTeamName(memberUnionName);
                }
                if(activeIntegral != null) {
                    user.setIntegral(activeIntegral);
                }
                if(bindStatus != null) {
                    user.setStatus(bindStatus);
                }
                if(StringUtils.isNotEmpty(memberUnionCode)) {
                    if(memberUnionCode.length() == 10) {
                        Team query = new Team();
                        query.setTeamNo(memberUnionCode);
                        Team resultTeam = teamServiceExt.getByEntity(query);
                        if (resultTeam == null) {
                            Team insertTeam = new Team();
                            String last4Str = memberUnionCode.substring(memberUnionCode.length() -4, memberUnionCode.length());
                            int last4Number = NumberUtils.str2Int(last4Str);
                            if(last4Number > 0) {
                                //基层工会有上层组织，pid为上层组织id,无上层组织，pid设为-1
                                Team parentTeam = getParentTeam(memberUnionCode);
                                if(parentTeam != null) {
                                    insertTeam.setPid(parentTeam.getId());
                                } else {
                                    insertTeam.setPid((long) -1);
                                }
                            }
                            insertTeam.setTeamName(memberUnionName);
                            insertTeam.setTeamNo(memberUnionCode);
                            int teamType = judgeTeamType(memberUnionCode);
                            insertTeam.setTeamType(teamType);
                            insertTeam.setCreateTime(new Date());
                            insertTeam.setUpdateTime(new Date());
                            teamServiceExt.insert(insertTeam);
                            user.setTeamId(insertTeam.getId());
                        } else {
                            user.setTeamId(resultTeam.getId());
                        }
                    } else {
                        //TODO 测试代码
                        if(!"pro".equalsIgnoreCase(WxApiServiceExt.profilesActive)) {
                            Team team = createRandomTeam();
                            user.setTeamId(team.getId());
                            user.setTeamNo(team.getTeamNo());
                        }
                    }
                }
            } else {
                log.info("根据unionId查询用户信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public int operateUserIntegral(String unionId, Integer optionType, Integer integral, Long tripartiteDetailId) {
        int flag = 0;
        try {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String nonce = StringUtils.createRandomStr(32);
            String param = "appkey=" + appKey.toLowerCase() +"&nonce=" + nonce + "&timestamp=" + timeStamp +"&sign=" + secretKey.toLowerCase();
            String sign = SecureUtil.md5(param);

            //String url = "https://hyxt.nbgh.gov.cn/wechat-console-test/api/public/union/add/intergral?unionId=" + unionId;
            String url = "https://hyxt.nbgh.gov.cn/wechat-console/api/public/union/add/intergral?unionId=" + unionId;
            HttpPost httpPost = new HttpPost(url);
            HttpClient httpClient = HttpClients.createDefault();
            JSONObject queryObject = new JSONObject();;
            queryObject.put("unionId", unionId);
            queryObject.put("optionType", optionType);
            queryObject.put("integral", integral);
            queryObject.put("tripartiteDetailId", tripartiteDetailId);
            StringEntity queryEntity = new StringEntity(queryObject.toJSONString());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("timeStamp", timeStamp);
            httpPost.setHeader("nonce", nonce);
            httpPost.setHeader("sign", sign);
            httpPost.setHeader("appKey", appKey);
            httpPost.setEntity(queryEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = new String(HttpUtils.entityToByte(entity),"UTF-8");
            JSONObject object = JsonUtils.parseString(result);
            if("200".equals(object.getString("code")) || "30103".equals(object.getString("code"))) {
                flag = 1;
            } else {
                log.info("操作用户积分失败！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据团队编码判断团队类型
     * @param teamNo
     * @return
     */
    public int judgeTeamType(String teamNo) {
        int resultTeamType = -1;
        String lasg4Str = teamNo.substring(teamNo.length() - 4, teamNo.length());
        int teamType = NumberUtils.str2Int(lasg4Str);
        if(teamType > 0) {
            resultTeamType = Constant.TEAM_TYPE_BASIC;
            return resultTeamType;
        }
        String newTeamNo = teamNo.substring(0,3);
        int teamNumber = NumberUtils.str2Int(newTeamNo);
        if(teamNumber > 0 && teamNumber < 11) {
            resultTeamType = Constant.TEAM_TYPE_COUNTRY_TOWN;
            return resultTeamType;
        }
        if(teamNumber > 10 && teamNumber < 16) {
            resultTeamType = Constant.TEAM_TYPE_AREA;
            return resultTeamType;
        }
        if(teamNumber > 15) {
            resultTeamType = Constant.TEAM_TYPE_INDUSTRIAL;
            return resultTeamType;
        }
        return resultTeamType;
    }

    public Team createRandomTeam() {
        Team query = new Team();
        query.setTeamType(4);
        List<Team> list = teamServiceExt.listByEntity(query);
        Random random = new Random();
        int number = random.nextInt(list.size() - 1);
        return list.get(number);
    }

    public Team getParentTeam(String teamNo) {
        Assert.notEmpty(teamNo);
        Team resultTeam = new Team();
        String last4Str = teamNo.substring(teamNo.length() -4, teamNo.length());
        int last4Number = NumberUtils.str2Int(last4Str);
        if(last4Number > 0) {
            String first3Str = teamNo.substring(0,3);
            String parentStr = first3Str + "0000000";
            resultTeam = teamServiceExt.getTeamByTeamNo(parentStr);
        }
        return resultTeam;
    }


    public String getName() {
        Random random = new Random();
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
        String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
        String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int index = random.nextInt(Surname.length - 1);
        //获得一个随机的姓氏
        String name = Surname[index];
        //可以根据这个数设置产生的男女比例
        int i = random.nextInt(3);
        if(i==2){
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                name = name + girl.substring(j, j + 2);
            } else {
                name = name + girl.substring(j, j + 1);
            }

        }
        else{
            int j = random.nextInt(girl.length()-2);
            if (j % 2 == 0) {
                name = name + boy.substring(j, j + 2);
            } else {
                name = name + boy.substring(j, j + 1);
            }

        }

        return name;
    }

    public int createRandomIntegral() {
        Random random = new Random();
        return random.nextInt(100);
    }


    @Value("${spring.profiles.active}")
    public void setProfilesActive(String profilesActive) {
        WxApiServiceExt.profilesActive = profilesActive;
    }
}