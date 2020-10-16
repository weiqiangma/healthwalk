package cn.pertech.healthwalk.base.data.constant;

//系统常量
public interface Constant {
    /**
     * 积分未领取
     */
    int INTEGRAL_NOT_TAKE = 0;
    /**
     * 积分已领取
     */
    int INTEGRAL_TAKED = 1;
    /**
     * 积分未同步
     */
    int INTEGRAL_NOT_SYNCHRONIZED = 0;
    /**
     * 积分已同步
     */
    int INTEGRAL_IS_SYNCHRONIZED = 1;

    /**
     * 用户已认证
     */
    int USER_STATUS_ACTIVE = 1;
    /**
     * 游客
     */
    int USER_STATUS_LOCK = 0;

    /**
     * 积分增加类型
     */
    int OPTION_TYPE_ADD = 1;
    /**
     * 积分回滚
     */
    int OPTION_TYPE_ROLLBACK = 2;

    /**
     * 区县:001-010
     */
    int TEAM_TYPE_COUNTRY_TOWN = 1;
    /**
     * 园区:011-015
     */
    int TEAM_TYPE_AREA = 2;
    /**
     * 产业：016以后
     */
    int TEAM_TYPE_INDUSTRIAL = 3;
    /**
     * 基层工会（团队编码后四位不为0）
     */
    int TEAM_TYPE_BASIC = 4;


    //=============================系统用户状态=============================
    //用户登录和请求token权限返回状态
    int LOGIN_NOTFIND = 40000;
    //用户被锁定
    int LOGIN_LOCKED = 40001;
    //token失效
    int LOGIN_TIME_OUT = 40002;
    //没有权限,操作失败
    int LOGIN_AUTHORITY = 40003;
    //没有token授权码
    int LOGIN_TOKEN_EMPTY = 40005;

}
