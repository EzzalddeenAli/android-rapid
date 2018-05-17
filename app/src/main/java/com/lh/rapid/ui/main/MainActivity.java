package com.lh.rapid.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.android.frameproj.library.interf.CallbackChangeFragment;
import com.android.frameproj.library.util.NetWorkUtils;
import com.android.frameproj.library.util.UpdateAppHttpUtil;
import com.android.frameproj.library.widget.BottomBar;
import com.android.frameproj.library.widget.BottomBarTab;
import com.gyf.barlibrary.ImmersionBar;
import com.lh.rapid.Constants;
import com.lh.rapid.MyApplication;
import com.lh.rapid.R;
import com.lh.rapid.api.common.CommonApi;
import com.lh.rapid.components.storage.UserStorage;
import com.lh.rapid.injector.HasComponent;
import com.lh.rapid.ui.BaseActivity;
import com.lh.rapid.ui.BaseFragment;
import com.lh.rapid.ui.BaseMainFragment;
import com.lh.rapid.ui.fragment1.FirstFragment;
import com.lh.rapid.ui.fragment1.Fragment1;
import com.lh.rapid.ui.fragment2.Fragment2;
import com.lh.rapid.ui.fragment2.SecondFragment;
import com.lh.rapid.ui.fragment3.Fragment3;
import com.lh.rapid.ui.fragment3.ThirdFragment;
import com.lh.rapid.ui.fragment4.FourthFragment;
import com.lh.rapid.ui.fragment4.Fragment4;
import com.lh.rapid.ui.login.LoginActivity;
import com.lh.rapid.util.CommonEvent;
import com.lh.rapid.util.SPUtil;
import com.luck.picture.lib.config.PictureConfig;
import com.squareup.otto.Bus;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements MainContract.View,
        HasComponent<MainComponent>, CallbackChangeFragment, BaseMainFragment.OnBackToFirstListener {

    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;
    @Inject
    CommonApi mCommonApi;
    @Inject
    Bus mBus;
    @Inject
    SPUtil mSPUtil;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @Inject
    UserStorage mUserStorage;

    private MainComponent mMainComponent;
    private long firstTime = 0;
    private static final int BACK_TIME = 2000;


    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    public static final int FIFTH = 4;

    private SupportFragment[] mFragments = new SupportFragment[5];

    @Inject
    MainPresenter mPresenter;

    public void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //友盟测试
        //        String deviceInfo = getDeviceInfo(MainActivity.this);
        //        Logger.i("友盟deviceInfo：" + deviceInfo);
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this))
                .build();
        mMainComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        mSPUtil.setFIRST_LOGIN(1);

        mPresenter.attachView(this);
        ButterKnife.bind(this);
        mBus.register(this);

        SupportFragment firstFragment = findFragment(FirstFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();
            mFragments[THIRD] = ThirdFragment.newInstance();
            mFragments[FOURTH] = FourthFragment.newInstance();

            loadMultipleRootFragment(R.id.frame_layout, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.findFragmentByTag()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(SecondFragment.class);
            mFragments[THIRD] = findFragment(ThirdFragment.class);
            mFragments[FOURTH] = findFragment(FourthFragment.class);
        }

        initView();

        // 默认为上海市中心点
        mSPUtil.setLONGITUDE(121.5060657907);
        mSPUtil.setLATITUDE(31.2434075787);

        handlerIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handlerIntent(intent);
    }

    private void handlerIntent(Intent intent) {
        if (intent != null) {
            int cometo = getIntent().getIntExtra("cometo", -1);
            if (cometo != -1 && mBottomBar != null) {
                mBottomBar.setCurrentItem(cometo);
            }
        }
    }

    private void initView() {
        mBottomBar.addItem(new BottomBarTab(this, R.mipmap.bottom_icon_1_r, R.mipmap.bottom_icon_1_g, "首页"))
                .addItem(new BottomBarTab(this, R.mipmap.bottom_icon_2_r, R.mipmap.bottom_icon_2_g, "分类"))
                .addItem(new BottomBarTab(this, R.mipmap.bottom_icon_4_r, R.mipmap.bottom_icon_4_g, "购物车"))
                .addItem(new BottomBarTab(this, R.mipmap.bottom_icon_5_r, R.mipmap.bottom_icon_5_g, "我的"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                if ((position == 2 || position == 3) && TextUtils.isEmpty(mUserStorage.getToken())) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_LOGIN_CODE);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                if ((position == 2 || position == 3) && TextUtils.isEmpty(mUserStorage.getToken())) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_LOGIN_CODE);
                }
                final SupportFragment currentFragment = mFragments[position];
                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

                // 如果不在该类别Fragment的主页,则回到主页;
                if (count > 1) {
                    if (currentFragment instanceof FirstFragment) {
                        currentFragment.popToChild(Fragment1.class, false);
                    } else if (currentFragment instanceof SecondFragment) {
                        currentFragment.popToChild(Fragment2.class, false);
                    } else if (currentFragment instanceof ThirdFragment) {
                        currentFragment.popToChild(Fragment3.class, false);
                    } else if (currentFragment instanceof FourthFragment) {
                        currentFragment.popToChild(Fragment4.class, false);
                    }
                    return;
                }

                // 这里推荐使用EventBus来实现 -> 解耦
                if (count == 1) {
                    // 在FirstPagerFragment中接收, 因为是嵌套的孙子Fragment 所以用EventBus比较方便
                    // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                    mBus.post(new CommonEvent().new TabSelectedEvent(position));
                }

            }
        });
    }

    /**
     * 筛选条件
     */

    @Override
    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    @Override
    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

    @Override
    public void changeFragment(int which) {
        mBottomBar.setCurrentItem(which);
    }

    private boolean isCheckUpdate = true;

    @Override
    protected void onResume() {
        super.onResume();
        // 上传错误日志
        uploadLog();
        // 检查更新
        if (isCheckUpdate) {
            //            checkUpdate();
            isCheckUpdate = false;
        }
    }

    /**
     * 检查版本更新
     */
    private void checkUpdate() {
        //版本更新
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(MainActivity.this)
                //更新地址
                .setUpdateUrl(Constants.DOWNLOAD_APK_URL)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected UpdateAppBean parseJson(String json) {

                        SAXReader saxReader = new SAXReader();

                        Document document = null;
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            document = DocumentHelper.parseText(json);

                            // 获取根元素
                            Element root = document.getRootElement();
                            System.out.println("Root: " + root.getName());
                            // 获取所有子元素
                            List<Element> childList = root.elements();
                            System.out.println("total child count: " + childList.size());

                            Element verElement = root.element("ver");
                            Element urlElement = root.element("url");
                            Element newVersionElement = root.element("new_version");
                            Element updateLogElement = root.element("update_log");
                            Element targetSizeElement = root.element("target_size");
                            updateAppBean
                                    .setUpdate((getVersion() >= Integer.parseInt(verElement.getTextTrim())) ? "No" : "Yes")
                                    .setNewVersion(newVersionElement.getTextTrim())
                                    .setApkFileUrl(urlElement.getTextTrim())
                                    .setUpdateLog(updateLogElement.getTextTrim())
                                    .setTargetSize(targetSizeElement.getTextTrim());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                });
    }

    /**
     * 上传错误日志
     * TODO 有空吧这个放到Presenter里面去
     */
    private void uploadLog() {
        //检查是否有日志文件 要是有的话上传
        String savePath = "";
        File logDir = null;
        if (NetWorkUtils.isNetworkConnected(MainActivity.this)) {
            try {
                //判断是否挂载了SD卡
                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    savePath = MyApplication.getContext().getExternalCacheDir() + Constants.LH_LOG_PATH;
                    logDir = new File(savePath);
                    if (!logDir.exists()) {
                        logDir.mkdirs();
                    }
                    if (logDir.list().length > 0) {
                        File[] files = logDir.listFiles();
                        if (null == files) {
                            return;
                        } else {
                            for (final File file :
                                    files) {

                                FileInputStream fin = new FileInputStream(file);
                                int length = fin.available();
                                byte[] buffer = new byte[length];
                                fin.read(buffer);
                                final String res = new String(buffer, "UTF-8");
                                fin.close();

                                mCommonApi.uploadErrorFiles(MainActivity.this.getPackageName(), "android",
                                        Build.VERSION.RELEASE, Build.MODEL, res)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ResponseBody>() {
                                            @Override
                                            public void accept(@NonNull ResponseBody responseBody) throws Exception {
                                                if (file.exists()) {
                                                    if (file.isFile()) {
                                                        file.delete();
                                                    }
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {
                                                throwable.printStackTrace();
                                            }
                                        });
                            }

                        }
                    } else {

                    }
                    //没有挂载SD卡，无法写文件
                    if (logDir == null || !logDir.exists() || !logDir.canWrite()) {
                        return;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
        mPresenter.detachView();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            ISupportFragment topFragment = getTopFragment();
            ((BaseFragment) ((FourthFragment) topFragment).getTopChildFragment()).onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == Constants.RESULT_LOGIN_CODE) {
            mBottomBar.setCurrentItem(0);
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }

    // =============================友盟测试代码
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}