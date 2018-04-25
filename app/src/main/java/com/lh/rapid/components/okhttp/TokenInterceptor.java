package com.lh.rapid.components.okhttp;

import android.content.Context;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.lh.rapid.Constants;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.bean.HttpResult;
import com.lh.rapid.bean.LoginEntity;
import com.lh.rapid.components.retrofit.RequestHelper;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.util.SPUtil;
import org.json.JSONObject;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

import static android.provider.MediaStore.getVersion;

/**
 * 处理Token的类
 */

public class TokenInterceptor implements Interceptor {

    private Context context;

    public TokenInterceptor(Context context) {
        super();
        this.context = context;

    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (isTokenExpired(response)) {//根据和服务端的约定判断token过期
            //同步请求方式，获取最新的Token
            String newToken = getNewToken(chain);
            //使用新的Token，创建新的请求
            SPUtil spUtil = new SPUtil(context);
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("token", spUtil.getTOKNE())
                    .build();
            //重新请求
            if (KeyM) {
                return chain.proceed(newRequest);
            } else {
                return response;
            }
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {


        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        //注意 >>>>>>>>> okhttp3.4.1这里变成了 !HttpHeader.hasBody(response)
        if (!HttpHeaders.hasBody(response)) {
            //END HTTP
        } else {
            BufferedSource source = responseBody.source();
            try {
                source.request(Long.MAX_VALUE); // Buffer the entire body.
            } catch (IOException e) {
                e.printStackTrace();
            }
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                }
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);

                //                Type listType = new TypeToken<ArrayList<HttpResult<Object>>>() {
                //                }.getType();
                try {
                    if (!TextUtils.isEmpty(result)) {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject resultStatus = jsonObject.getJSONObject("resultStatus");
                        int code = resultStatus.getInt("code");
                        if (code == Constants.TOKEN_EXPIRED) {
                            return true;
                        }
                        //                    HttpResultToken objectHttpResult= new Gson().fromJson(result, listType);
                        //
                        //                    HttpResultToken.ResultStatusBean resultStatus = objectHttpResult.getResultStatus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //获取到response的body的string字符串
                //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
        }

        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     * @param chain
     */
    private boolean keyV = false;
    private boolean KeyM = true;

    private String getNewToken(Chain chain) throws IOException {

        keyV = false;
        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
        SPUtil spUtil = new SPUtil(context);
        UserStorage userStorage = new UserStorage(context, spUtil);
        RequestHelper requestHelper = new RequestHelper(context, userStorage);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder1 = request.newBuilder();
                        builder1.addHeader("key", Constants.app_key);
                        builder1.addHeader("app_type", "2");
                        builder1.addHeader("env", "prd-v1");
                        builder1.addHeader("OS_type", "os_type");
                        String deviceId = ((TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
                        builder1.addHeader("device_id", deviceId);
                        //我的deviceId：860076031766214
                        builder1.addHeader("app_version", getVersion(context));
                        Request build = builder1.build();
                        return chain.proceed(build);
                    }
                });
        CommonApi commonApi = new CommonApi(context, builder.build(), requestHelper, userStorage, spUtil);
       /* commonApi.accountRefreshToken(spUtil.getTOKNE())
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<LoginEntity>, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        SPUtil spUtil = new SPUtil(context);
                        spUtil.setTOKNE(loginEntity.getToken());
                        keyV = true;
                        KeyM = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        keyV = true;
                        KeyM = false;
                    }
                });*/
        for (int i = 0; i < 1000; i++) {
            SystemClock.sleep(100);
            if (keyV) {
                return "";
            }
        }

        return "-1";
    }


    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}