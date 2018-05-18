package com.lh.rapid;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Constants {

    //接口正常
    public static final int OK = 0;
    //用户未登录
    public static final int NO_USER = -999;
    //token过期
    public static final int TOKEN_EXPIRED = -111;
    //在其他地方登录了
    public static final int TOKEN_EXIST = -112;
    //用户被锁定
    public static final int TOKEN_FREEZE = -113;
    public static final String WECHAT_APPID = "wxe94192df313b992f";
    // 日志默认保存目录
    public static String LH_LOG_PATH = "/LH/Log/";
    // 本地测试地址
    public static final String BASE_URL = "http://192.168.10.127:8080/";
    // APK下载地址
    public static final String DOWNLOAD_APK_URL = BASE_URL + "static/app/ver.xml";
    // app类型：1:ios,2:andriod
    public static final int APPTYPE = 2;

    //服务端自定义API key、value
    public static String app_key = "B272F43387B8504C";
    public static String app_value = "70BAE8B491362AB39042B77C7653199D";

    public static final String PARAMETERS_CHECK_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";//6-16位数字和字母的组合
    public static final String PARAMETERS_CHECK_LOGIN_NAME = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{3,16}$";//3-16位数字和字母的组合
    public static final String PARAMETERS_CHECK_IDENTITY_NO = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";//身份证

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int REQUEST_CHOOSE_LOCATION_CODE = 100;
    public static final int RESULT_CHOOSE_LOCATION_CODE = 101;
    public static final int REQUEST_REGISTER_CODE = 108;
    public static final int RESULT_REGISTER_CODE = 109;
    public static final int REQUEST_LOGIN_CODE = 112;
    public static final int RESULT_LOGIN_CODE = 113;
    public static final int REQUEST_EXIST_CODE = 114;
    public static final int RESULT_EXIST_CODE = 115;
    public static final int REQUEST_CHOOSE_LOCATION_CODE_CUSTOM_MAP = 120;
    public static final int RESULT_CHOOSE_LOCATION_CODE_CUSTOM_MAP = 121;
    public static final int REQUEST_LOCATION_MANAGER_CODE = 122;
    public static final int RESULT_LOCATION_MANAGER_CODE = 123;
    public static final int REQUEST_COUPON_CODE = 134;
    public static final int RESULT_COUPON_CODE = 135;
    public static boolean isAllCheck = true;

    /**
     * 验证码类型 1.注册，2。登录，3.修改密码，4.修改手机号 5.修改个人信息 6.第三方登录注册,7-扫码绑定
     */
    public static final String SMSCODE_TYPE_REGISTER = "1";
    public static final String SMSCODE_TYPE_LOGIN = "2";
    public static final String SMSCODE_TYPE_MODIFY_PASSWORD = "3";

    // 微博授权回调页
    public static final String SINA_REDIRECT_URL = "http://api.myhaving.com/thirdlogin/weibo/auth/callback";
    // 微博取消授权回调页
    public static final String SINA_REDIRECT_URL_CANCEL = "http://api.myhaving.com/thirdlogin/weibo/cancelauth/callback";
    // 帮助中心HTML
    public static final String HelpCenter = "http://www.baidu.com";
    // 隐私协议HTML
    public static final String PrivacyPolicy = "http://www.baidu.com";

    // 微信支付APPID
    public static final String WECHAT_PAY_APP_ID = "";

}