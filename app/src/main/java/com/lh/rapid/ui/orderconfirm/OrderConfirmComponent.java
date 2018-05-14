package com.lh.rapid.ui.orderconfirm;

import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by lh on 2018/5/14.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface OrderConfirmComponent {

    void inject(OrderConfirmActivity activity);

}
