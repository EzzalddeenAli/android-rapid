package com.lh.rapid.ui.orderdetail;

import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 * @des ${TODO}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {
        ActivityModule.class
})
public interface OrderDetailComponent {

    void inject(OrderDetailActivity activity);

}
