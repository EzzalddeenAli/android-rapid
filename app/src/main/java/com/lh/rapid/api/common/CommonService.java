package com.lh.rapid.api.common;

import com.lh.rapid.bean.AccountInfoBean;
import com.lh.rapid.bean.AccountUserHomeBean;
import com.lh.rapid.bean.AddressListBean;
import com.lh.rapid.bean.CartGoodsBean;
import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.bean.CommonNewsInfoBean;
import com.lh.rapid.bean.DictionaryBean;
import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.bean.HomeCircleBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
import com.lh.rapid.bean.OrderBean;
import com.lh.rapid.bean.OrderDetailBean;
import com.lh.rapid.bean.OrderSubmitBean;
import com.lh.rapid.bean.OrderSubmitConfirmBean;
import com.lh.rapid.bean.ProductListBean;
import com.lh.rapid.bean.UserCouponsBean;

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

    //首页
    @FormUrlEncoded
    @POST("home/page")
    Observable<HttpResult<HomePageBean>> homePage(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                  @FieldMap Map<String, Object> params);

    //商品详情
    @FormUrlEncoded
    @POST("goods/detail")
    Observable<HttpResult<GoodsDetailBean>> goodsDetail(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);


    //商品分类
    @FormUrlEncoded
    @POST("category/oneLevel")
    Observable<HttpResult<List<CategoryOneLevelBean>>> categoryOneLevel(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                        @FieldMap Map<String, Object> params);

    //商品分类详情
    @FormUrlEncoded
    @POST("category/details")
    Observable<HttpResult<List<CategoryDetailsBean>>> categoryDetails(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                      @FieldMap Map<String, Object> params);

    //提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
    @GET("http://api.map.baidu.com/geocoder/v2/?")
    Observable<ResponseBody> geocoderApi(@QueryMap Map<String, Object> params);

    //首页圈主信息
    @FormUrlEncoded
    @POST("home/circle")
    Observable<HttpResult<List<HomeCircleBean>>> homeCircle(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @FieldMap Map<String, Object> params);

    //商品列表
    @FormUrlEncoded
    @POST("goods/list")
    Observable<HttpResult<List<ProductListBean>>> goodsList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @FieldMap Map<String, Object> params);

    //收货地址列表
    @POST("address/list")
    Observable<HttpResult<List<AddressListBean>>> addressList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                              @Header("token") String token);

    //更新收货地址
    @FormUrlEncoded
    @POST("address/update")
    Observable<HttpResult<String>> addressUpdate(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @FieldMap Map<String, Object> params);

    //删除收货地址
    @FormUrlEncoded
    @POST("address/delete")
    Observable<HttpResult<String>> addressDelete(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @FieldMap Map<String, Object> params);

    //获取默认收货地址
    @FormUrlEncoded
    @POST("address/default")
    Observable<HttpResult<AddressListBean>> addressDefault(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                           @FieldMap Map<String, Object> params);

    //购物车列表
    @FormUrlEncoded
    @POST("cart/goods/list")
    Observable<HttpResult<List<CartGoodsBean>>> cartGoodsList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                              @FieldMap Map<String, Object> params);

    //添加购物车
    @FormUrlEncoded
    @POST("cart/goods/add")
    Observable<HttpResult<String>> cartGoodsAdd(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                @FieldMap Map<String, Object> params);

    //删除购物车
    @FormUrlEncoded
    @POST("cart/goods/delete")
    Observable<HttpResult<String>> cartGoodsDelete(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                   @FieldMap Map<String, Object> params);

    //查询购物车商品数量
    @FormUrlEncoded
    @POST("cart/findShoppingCartGoodsCount")
    Observable<HttpResult<Integer>> cartFindShoppingCartGoodsCount(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                   @FieldMap Map<String, Object> params);

    //商品列表分类
    @FormUrlEncoded
    @POST("goods/list/home")
    Observable<HttpResult<List<HomePageBean.CategoryListsBean.GoodListBean>>> goodsListHome(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                                            @FieldMap Map<String, Object> params);

    //确认订单
    @FormUrlEncoded
    @POST("order/submit/confirm")
    Observable<HttpResult<OrderSubmitConfirmBean>> orderSubmitConfirm(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                      @FieldMap Map<String, Object> params);

    //提交订单
    @FormUrlEncoded
    @POST("order/submit")
    Observable<HttpResult<OrderSubmitBean>> orderSubmit(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);

    //我的订单
    @FormUrlEncoded
    @POST("order/list")
    Observable<HttpResult<List<OrderBean>>> orderList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                      @FieldMap Map<String, Object> params);

    //完成订单
    @FormUrlEncoded
    @POST("order/finish")
    Observable<HttpResult<String>> orderFinish(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params);

    //订单取消
    @FormUrlEncoded
    @POST("order/cancel")
    Observable<HttpResult<String>> orderCancel(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params);

    //订单详情
    @FormUrlEncoded
    @POST("order/detail")
    Observable<HttpResult<OrderDetailBean>> orderDetail(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);

    //个人基本信息
    @FormUrlEncoded
    @POST("account/info")
    Observable<HttpResult<AccountInfoBean>> accountInfo(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);

    //重置密码
    @FormUrlEncoded
    @POST("account/password/reset")
    Observable<HttpResult<String>> accountPasswordReset(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);

    //个人主页
    @FormUrlEncoded
    @POST("account/userHome")
    Observable<HttpResult<AccountUserHomeBean>> accountUserHome(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                @FieldMap Map<String, Object> params);

    //刷新token
    @FormUrlEncoded
    @POST("account/refreshToken")
    Observable<HttpResult<LoginEntity>> accountRefreshToken(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @FieldMap Map<String, Object> params);

    //完善用户信息
    @FormUrlEncoded
    @POST("account/info/completed")
    Observable<HttpResult<String>> accountInfoCompleted(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @FieldMap Map<String, Object> params);

    //修改密码
    @FormUrlEncoded
    @POST("account/changePassword")
    Observable<HttpResult<String>> accountChangePassword(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                         @FieldMap Map<String, Object> params);

    //关于我们
    @FormUrlEncoded
    @POST("common/news/info")
    Observable<HttpResult<List<CommonNewsInfoBean>>> commonNewsInfo(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                    @FieldMap Map<String, Object> params);

    //会员优惠券
    @FormUrlEncoded
    @POST("user/coupons")
    Observable<HttpResult<List<UserCouponsBean>>> userCoupons(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                              @FieldMap Map<String, Object> params);

    //字典
    @FormUrlEncoded
    @POST("common/dictionary/query")
    Observable<HttpResult<List<DictionaryBean>>> commonDictionaryQuery(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                       @FieldMap Map<String, Object> params);
}