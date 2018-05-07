package com.lh.rapid.api.common;

import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 * @des ${写接口}
 */
public interface CommonService {

    // 日志上传
    @FormUrlEncoded
    @POST("http://appex.we-win.com.cn/UploadErrorFiles.aspx")
    Observable<ResponseBody> uploadErrorFiles(@Field("appId") String appId, @Field("deviceType") String deviceType,
                                              @Field("osVersion") String osVersion,
                                              @Field("deviceModel") String deviceModel, @Field("log") String log);

    @GET("https://slb.api.myhaving.com/static/app/maiyiAppVer.xml")
    Observable<ResponseBody> maiyiAppVer();

    //登录
    @FormUrlEncoded
    @POST("account/login/normal")
    Observable<HttpResult<LoginEntity>> loginNormal(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                    @FieldMap Map<String, Object> params);

    //手机号登录
    @FormUrlEncoded
    @POST("account/login/mobile")
    Observable<HttpResult<LoginEntity>> loginMobile(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                    @FieldMap Map<String, Object> params);

    //注册
    @FormUrlEncoded
    @POST("account/register")
    Observable<HttpResult<LoginEntity>> register(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @FieldMap Map<String, Object> params);

    //发送短信验证码
    @FormUrlEncoded
    @POST("common/smscode/send")
    Observable<HttpResult<String>> smsCodeSend(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params);

    //重置密码
    @FormUrlEncoded
    @POST("account/password/reset")
    Observable<HttpResult<String>> accountPasswordReset(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @Header("token") String token, @FieldMap Map<String, Object> params);


}