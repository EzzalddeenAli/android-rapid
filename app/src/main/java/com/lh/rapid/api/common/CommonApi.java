package com.lh.rapid.api.common;

import android.content.Context;

import com.lh.rapid.Constants;
import com.lh.rapid.bean.CategoryDetailsBean;
import com.lh.rapid.bean.CategoryOneLevelBean;
import com.lh.rapid.bean.GoodsDetailBean;
import com.lh.rapid.bean.HomePageBean;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
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
        params.put("authCode", authCode);
        params.put("newPassword", newPassword);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.accountPasswordReset(currentTimeMillis, sign, mUserStorage.getToken(), params).subscribeOn(Schedulers.io());
    }

    /**
     * 首页
     */
    public Observable<HttpResult<HomePageBean>> homePage(String circleId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("circleId", circleId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.homePage(currentTimeMillis, sign, mUserStorage.getToken(), params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品详情
     */
    public Observable<HttpResult<GoodsDetailBean>> goodsDetail(String goodsId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("goodsId", goodsId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.goodsDetail(currentTimeMillis, sign, mUserStorage.getToken(), params).subscribeOn(Schedulers.io());
    }

    /**
     * 商品分类
     */
    public Observable<HttpResult<List<CategoryOneLevelBean>>> categoryOneLevel() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.categoryOneLevel(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 商品分类详情
     */
    public Observable<HttpResult<List<CategoryDetailsBean>>> categoryDetails(int parentId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("parentId", parentId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.categoryDetails(currentTimeMillis, sign, mUserStorage.getToken(), params).subscribeOn(Schedulers.io());
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

}