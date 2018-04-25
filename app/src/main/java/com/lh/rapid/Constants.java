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

    public static String LH_LOG_PATH = "/LH/Log/";// 日志默认保存目录
    //    正式
    public static final String BASE_URL = "https://slb.api.myhaving.com/";
    public static final String DOWNLOAD_APK_URL = BASE_URL + "static/app/maiyiAppVer.xml";
    public static final int APPTYPE = 2;
    // app类型：1:ios,2:andriod

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

    public static final int REQUEST_REGISTER_CODE = 108;
    public static final int RESULT_REGISTER_CODE = 109;

}