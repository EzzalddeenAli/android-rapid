package com.lh.rapid;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.log.CrashlyticsTree;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.util.log.Settings;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.component.DaggerApplicationComponent;
import com.lh.rapid.injector.module.ApplicationModule;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class MyApplication extends MultiDexApplication {

    private static Context mContext;

    private ApplicationComponent mApplicationComponent;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
                return new ClassicsHeader(context)
                        .setLastUpdateTime(new Date(System.currentTimeMillis() - delta))
                        .setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        mContext = getApplicationContext();
        new AppError().initUncaught();
        ToastUtil.register(this);
        initLogger();
        initFragmentation();
    }

    private void initFragmentation() {
        // 栈视图等功能，建议在Application里初始化
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();
    }

    private void initLogger() {
        // 初始化日志功能
        Logger.initialize(
                new Settings()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
        );
        if (!BuildConfig.DEBUG) {
            Logger.plant(new CrashlyticsTree());
        }
    }

    /**
     * 需要保证ApplicationComponent只有一个实例
     */
    private void initComponent() {
        mApplicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this)) // 如果Module只有有参构造器，则必须显式传入Module实例。
                        .build();
        mApplicationComponent.inject(this);//现在没有需要在MyApplication注入的对象，所以这句代码可写可不写
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }

}