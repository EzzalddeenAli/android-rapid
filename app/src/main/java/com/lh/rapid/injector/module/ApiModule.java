package com.lh.rapid.injector.module;

import android.content.Context;

import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.retrofit.RequestHelper;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.injector.PerApp;
import com.lh.rapid.util.SPUtil;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class ApiModule {

    @Provides
    @PerApp
    public CommonApi providesCookieApi(Context context, @Named("api") OkHttpClient okHttpClient, RequestHelper requestHelper,
                                       UserStorage userStorage, SPUtil spUtil) {
        return new CommonApi(context,okHttpClient,requestHelper,userStorage,spUtil);
    }

}
