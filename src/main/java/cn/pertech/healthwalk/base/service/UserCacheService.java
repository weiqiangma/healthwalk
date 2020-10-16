package cn.pertech.healthwalk.base.service;

import cn.pertech.common.abs.BaseService;
import cn.pertech.common.cache.CacheService;
import cn.pertech.common.constants.CacheKeyConstants;
import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.service.UserServiceExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用户缓存对象
 * @author js
 */
@Service
public class UserCacheService extends BaseService {
    private final static String STU_CACHE_KEY = "user:%s";
    //缓存时间2小时
    private final static int STU_CACHE_TIME = 2 * 60 * 60;
    @Resource
    private CacheService cacheService;
    @Resource
    private UserServiceExt userServiceExt;

    /**
     * 获取用户缓存对象
     *
     * @param userId
     * @return
     */
    public User getUser(Long userId) {
        if (userId == null) return null;
        String key = getUserCacheKey(userId);
        User user = cacheService.get(key, User.class);
        if (user == null || StringUtils.isEmpty(user.getMobile())) {
            user = userServiceExt.getById(userId);
            if (user != null) {
                cacheService.put(key, user, STU_CACHE_TIME, TimeUnit.MINUTES);
            }
        }
        return user;
    }

    /**
     * 更新缓存
     * @param user
     */
    public void updateCache(User user){
        String key = getUserCacheKey(user.getId());
        cacheService.put(key, user, STU_CACHE_TIME, TimeUnit.SECONDS);
    }

    private String getUserCacheKey(Long userId) {
        return String.format(STU_CACHE_KEY, String.valueOf(userId));
    }

    //---------------------------------------------------用户session信息-------------------------------------------------------------//
    /**
     * 用户token缓存时间: 默认60秒时间
     */
    @Value("${soft.token.user.time}")
    private int USER_TOKEN_CACHE_TIME = CacheKeyConstants.ADMIN_TOKEN_TIME;

    /**
     * 将后台用户信息放缓存 存放两份信息,一份用户用户token,另外一份是用户ID的
     *
     * @param token
     * @param userSession
     */
    public void putAdminSession(String token, UserSession userSession) {
        String key = String.format(CacheKeyConstants.ADMIN_OPEN_ID_KEY, token);
        cacheService.put(key, userSession, USER_TOKEN_CACHE_TIME, TimeUnit.SECONDS);
        cacheService.put(String.format("TUD_%d", userSession.getId()), userSession, USER_TOKEN_CACHE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 将后台用户信息放缓存 存放两份信息,一份用户用户token,另外一份是用户openId的
     *
     * @param token
     * @param userSession
     */
    public void putUserSession(String token, UserSession userSession) {
        String key = String.format(CacheKeyConstants.USER_OPEN_ID_KEY, token);
        //logger.info("保存缓存信息: " + key + "\r\n" + JsonUtils.toStringNoEx(userSession));
        cacheService.put(key, userSession, USER_TOKEN_CACHE_TIME, TimeUnit.SECONDS);

    }


    /**
     * 从缓存中获取用户信息
     *
     * @param token
     * @return
     */
    public UserSession getUserSession(String token) {
        String key = String.format(CacheKeyConstants.USER_OPEN_ID_KEY, token);
        return cacheService.get(key, UserSession.class);
    }

    public UserSession getAdminSession(String token) {
        String key = String.format(CacheKeyConstants.ADMIN_OPEN_ID_KEY, token);
        return cacheService.get(key, UserSession.class);
    }


    /**
     * 退出，清除rendis缓存
     *
     * @param token
     */
    public void removeUserToken(String token) {
        String key = String.format(CacheKeyConstants.USER_OPEN_ID_KEY, token);
        cacheService.del(key);
    }

    @Autowired
    public void setUserServiceExt(UserServiceExt userServiceExt) {
        this.userServiceExt = userServiceExt;
    }
}
