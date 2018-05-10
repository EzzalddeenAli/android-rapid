package com.lh.rapid.api.common;

import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
import com.lh.rapid.bean.ProductListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
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

    //首页
    @FormUrlEncoded
    @POST("home/page")
    Observable<HttpResult<HomePageBean>> homePage(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                  @Header("token") String token, @FieldMap Map<String, Object> params);

    //商品详情
    @FormUrlEncoded
    @POST("goods/detail")
    Observable<HttpResult<GoodsDetailBean>> goodsDetail(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @Header("token") String token, @FieldMap Map<String, Object> params);


    //商品分类
    @POST("category/oneLevel")
    Observable<HttpResult<List<CategoryOneLevelBean>>> categoryOneLevel(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                        @Header("token") String token);

    //商品分类详情
    @FormUrlEncoded
    @POST("category/details")
    Observable<HttpResult<List<CategoryDetailsBean>>> categoryDetails(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                      @Header("token") String token, @FieldMap Map<String, Object> params);

    //提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
    @GET("http://api.map.baidu.com/geocoder/v2/?")
    Observable<ResponseBody> geocoderApi(@QueryMap Map<String, Object> params);

    //首页圈主信息
    @FormUrlEncoded
    @POST("home/circle")
    Observable<HttpResult<List<HomeCircleBean>>> homeCircle(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @Header("token") String token, @FieldMap Map<String, Object> params);

    //商品列表
    @FormUrlEncoded
    @POST("goods/list")
    Observable<HttpResult<List<ProductListBean>>> goodsList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @Header("token") String token, @FieldMap Map<String, Object> params);

    //收货地址列表
    @POST("address/list")
    Observable<HttpResult<List<AddressListBean>>> addressList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                              @Header("token") String token);

    //更新收货地址
    @FormUrlEncoded
    @POST("address/update")
    Observable<HttpResult<String>> addressUpdate(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @Header("token") String token, @FieldMap Map<String, Object> params);

}