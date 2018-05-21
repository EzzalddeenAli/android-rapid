package com.lh.rapid.api.common;

import android.content.Context;
import android.text.TextUtils;

import com.lh.rapid.Constants;
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
import com.lh.rapid.components.retrofit.RequestHelper;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.util.SPUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${实现接口的调用}
 */
public class CommonApi {

    private CommonService mCommonService;
    private Context mContext;
    private RequestHelper mRequestHelper;
    private UserStorage mUserStorage;
    private SPUtil mSpUtil;

    public CommonApi(Context context, OkHttpClient mOkHttpClient, RequestHelper requestHelper,
                     UserStorage userStorage, SPUtil spUtil) {
        mContext = context;
        this.mRequestHelper = requestHelper;
        mUserStorage = userStorage;
        mSpUtil = spUtil;
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
        mCommonService = retrofit.create(CommonService.class);
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> Observable<T> flatResponse(final HttpResult<T> response) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                if (!e.isDisposed()) {
                    if (response.isSuccess()) {
                        e.onNext(response.getResultValue());
                        e.onComplete();
                    } else {
                        e.onError(new APIException(response.getResultStatus().getCode(),
                                response.getResultStatus().getMessage()));
                    }
                }
            }
        });
    }

    /**
     * 自定义异常，当接口返回的code不为Constants.OK时，需要跑出此异常
     * eg：登陆时验证码错误；参数为传递等
     */
    public static class APIException extends Exception {
        public int code;
        public String message;

        public APIException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @android.support.annotation.NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @android.support.annotation.NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @android.support.annotation.NonNull
    public static MultipartBody.Part createPartString(String name, String value) {
        //        RequestBody requestFile =
        //                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), value);
        return MultipartBody.Part.createFormData(name, value);
    }

    // =======================================  API

    /**
     * 日志上传
     */
    public Observable<ResponseBody> uploadErrorFiles(String appId, String deviceType,
                                                     String osVersion, String deviceModel, String log) {
        return mCommonService.uploadErrorFiles(appId, deviceType, osVersion, deviceModel, log).subscribeOn(Schedulers.io());
    }

    /**
     * 版本更新
     */
    public Observable<ResponseBody> maiyiAppVer() {
        return mCommonService.maiyiAppVer().subscribeOn(Schedulers.io());
    }


    /**
     * 登录
     */
    public Observable<HttpResult<LoginEntity>> loginNormal(String username, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("username", username);
        params.put("password", password);
        params.put("appType", Constants.APPTYPE);
        params.put("pushId", mSpUtil.getREGISTRATIONID());
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.loginNormal(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 手机号登录
     */
    public Observable<HttpResult<LoginEntity>> loginMobile(String username, String smsCode) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", username);
        params.put("smsCode", smsCode);
        params.put("appType", Constants.APPTYPE);
        params.put("pushId", mSpUtil.getREGISTRATIONID());
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.loginMobile(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 注册
     */
    public Observable<HttpResult<LoginEntity>> register(String phone, String validate, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", phone);
        params.put("password", password);
        params.put("smsCode", validate);
        params.put("source", Constants.APPTYPE);
        params.put("pushId", mSpUtil.getREGISTRATIONID());
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.register(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 发送短信验证码
     */
    public Observable<HttpResult<String>> smsCodeSend(String mobile, String type) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", mobile);
        params.put("type", type); // 1.注册，2.登录，3.修改密码
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.smsCodeSend(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 重置密码
     */
    public Observable<HttpResult<String>> accountPasswordReset(String mobile, String authCode, String newPassword) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", mobile);
        params.put("smsCode", authCode);
        params.put("newPassword", newPassword);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountPasswordReset(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 首页
     */
    public Observable<HttpResult<HomePageBean>> homePage(String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.homePage(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品详情
     */
    public Observable<HttpResult<GoodsDetailBean>> goodsDetail(String circleId, String goodsId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("circleId", circleId);
        params.put("goodsId", goodsId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.goodsDetail(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品分类
     */
    public Observable<HttpResult<List<CategoryOneLevelBean>>> categoryOneLevel(String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.categoryOneLevel(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品分类详情
     */
    public Observable<HttpResult<List<CategoryDetailsBean>>> categoryDetails(int parentId, String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("parentId", parentId);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.categoryDetails(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
     */
    public Observable<ResponseBody> geocoderApi(String latLng) {
        Map<String, Object> params = new HashMap<>();
        // ak:百度地图api key
        params.put("ak", "mFPjNn9HWii1KiKWLTFdgvb3KI7LQVoF");
        params.put("callback", "renderReverse");
        params.put("location", latLng);
        params.put("output", "json");
        params.put("pois", "1");
        params.put("mcode", "CC:DE:0D:85:1D:4A:71:BF:9B:E3:53:F4:7F:37:4D:B3:72:DF:07:D7;com.xjgj.mall");
        return mCommonService.geocoderApi(params).subscribeOn(Schedulers.io());
    }

    /**
     * 首页圈主信息
     */
    public Observable<HttpResult<List<HomeCircleBean>>> homeCircle(double longitude, double latitude) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.homeCircle(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品列表
     * <p>
     * type		int	1.通常商品；2.积分商品	FALSE
     * categoryId		int	分类ID	FALSE
     * page		int	页码 default=1	FALSE
     * pageSize		int	每页大小	FALSE
     * sortStr		String	排序字段:focus-人气，sale-销量，price-价格	FALSE
     * sortType		int	排序方向：asc-升序，desc-降序	FALSE
     *
     * @return
     */
    public Observable<HttpResult<List<ProductListBean>>> goodsList(String type, String goodsName, String sortType,
                                                                   String sortStr, String categoryId, String circleId,
                                                                   int page, int pageSize) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        if (type != null && !TextUtils.isEmpty(type)) {
            params.put("type", type);
        }
        if (goodsName != null && !TextUtils.isEmpty(goodsName)) {
            params.put("goodsName", goodsName);
        }
        if (sortType != null && !TextUtils.isEmpty(sortType)) {
            params.put("sortType", sortType);
        }
        if (sortStr != null && !TextUtils.isEmpty(sortStr)) {
            params.put("sortStr", sortStr);
        }
        if (categoryId != null && !TextUtils.isEmpty(categoryId)) {
            params.put("categoryId", categoryId);
        }
        if (circleId != null && !TextUtils.isEmpty(circleId)) {
            params.put("circleId", circleId);
        }
        params.put("page", page);
        params.put("pageSize", pageSize);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.goodsList(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 收货地址列表
     */
    public Observable<HttpResult<List<AddressListBean>>> addressList() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addressList(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 更新收货地址
     */
    public Observable<HttpResult<String>> addressUpdate(String addressId, String receiveName, String phone, String area,
                                                        String detailAddress, String longitude, String latitude) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        if (addressId != null && !TextUtils.isEmpty(addressId)) {
            params.put("addressId", addressId);
        }
        if (receiveName != null && !TextUtils.isEmpty(receiveName)) {
            params.put("receiveName", receiveName);
        }
        if (phone != null && !TextUtils.isEmpty(phone)) {
            params.put("phone", phone);
        }
        if (area != null && !TextUtils.isEmpty(area)) {
            params.put("area", area);
        }
        if (detailAddress != null && !TextUtils.isEmpty(detailAddress)) {
            params.put("detailAddress", detailAddress);
        }
        if (longitude != null && !TextUtils.isEmpty(longitude)) {
            params.put("longitude", longitude);
        }
        if (latitude != null && !TextUtils.isEmpty(latitude)) {
            params.put("latitude", latitude);
        }
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addressUpdate(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 删除收货地址
     */
    public Observable<HttpResult<String>> addressDelete(String addressId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("addressId", addressId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addressDelete(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 获取默认收货地址
     */
    public Observable<HttpResult<AddressListBean>> addressDefault() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addressDefault(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 购物车列表
     */
    public Observable<HttpResult<List<CartGoodsBean>>> cartGoodsList() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.cartGoodsList(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 添加购物车
     */
    public Observable<HttpResult<String>> cartGoodsAdd(String goodsId, String quantity, String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("goodsId", goodsId);
        params.put("quantity", quantity);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.cartGoodsAdd(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 删除购物车
     */
    public Observable<HttpResult<String>> cartGoodsDelete(String goodsId, String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("goodsId", goodsId);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.cartGoodsDelete(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 查询购物车商品数量
     */
    public Observable<HttpResult<Integer>> cartFindShoppingCartGoodsCount() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.cartFindShoppingCartGoodsCount(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品列表分类
     */
    public Observable<HttpResult<List<HomePageBean.CategoryListsBean.GoodListBean>>> goodsListHome(String categoryId, String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("categoryId", categoryId);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.goodsListHome(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 确认订单
     */
    public Observable<HttpResult<OrderSubmitConfirmBean>> orderSubmitConfirm(String addressId, String circleId, String paramsString) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("addressId", addressId);
        params.put("circleId", circleId);
        params.put("params", paramsString);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderSubmitConfirm(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 提交订单
     */
    public Observable<HttpResult<OrderSubmitBean>> orderSubmit(String addressId, String circleId, String couponId,
                                                               String sendDate, String sendTime, String paramsString) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("addressId", addressId);
        params.put("circleId", circleId);
        if(!couponId.equals("-1")) {
            params.put("couponId", couponId);
        }
        params.put("sendDate", sendDate);
        params.put("sendTime", sendTime);
        params.put("params", paramsString);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderSubmit(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 我的订单
     *
     * @param status 订单状态：1-待付款，2-准备中，3-配送中，4-已完成 5-已取消 0-全部
     * @return
     */
    public Observable<HttpResult<List<OrderBean>>> orderList(String status, int page, int pageSize) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("status", status);
        params.put("page", page);
        params.put("pageSize", pageSize);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderList(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 完成订单
     */
    public Observable<HttpResult<String>> orderFinish(String orderId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderFinish(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 订单取消
     */
    public Observable<HttpResult<String>> orderCancel(String orderId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderCancel(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 订单详情
     */
    public Observable<HttpResult<OrderDetailBean>> orderDetail(String orderId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderDetail(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 个人基本信息
     */
    public Observable<HttpResult<AccountInfoBean>> accountInfo() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountInfo(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 个人主页
     */
    public Observable<HttpResult<AccountUserHomeBean>> accountUserHome() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountUserHome(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 刷新token
     */
    public Observable<HttpResult<LoginEntity>> accountRefreshToken() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountRefreshToken(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 完善用户信息
     */
    public Observable<HttpResult<String>> accountInfoCompleted(String nickName, String gender, String email, String birthday) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("nickName", nickName);
        params.put("gender", gender);
        params.put("email", email);
        params.put("birthday", birthday);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountInfoCompleted(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 修改密码
     */
    public Observable<HttpResult<String>> accountChangePassword(String newPassword, String oldPassword) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("newPassword", newPassword);
        params.put("oldPassword", oldPassword);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountChangePassword(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 关于我们
     */
    public Observable<HttpResult<List<CommonNewsInfoBean>>> commonNewsInfo() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.commonNewsInfo(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 会员优惠券
     */
    public Observable<HttpResult<List<UserCouponsBean>>> userCoupons() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.userCoupons(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 查询字典
     */
    public Observable<HttpResult<List<DictionaryBean>>> commonDictionaryQuery() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.commonDictionaryQuery(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

}