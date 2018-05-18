package com.lh.rapid.ui.recharge;


import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;
import com.lh.rapid.ui.login.LoginActivity;

import dagger.Component;

/**
 * Created by we-win on 2017/7/21.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface LoginComponent {

    void inject(LoginActivity activity);

}
