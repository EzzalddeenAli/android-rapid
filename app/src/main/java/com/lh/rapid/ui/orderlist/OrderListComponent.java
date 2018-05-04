package com.lh.rapid.ui.orderlist;

import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;
import com.lh.rapid.ui.orderlist.all.OrderAllFragment;

import dagger.Component;

/**
 * Created by lh on 2018/5/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {
        ActivityModule.class
})
public interface OrderListComponent {

    void inject(OrderListActivity activity);

    void inject(OrderAllFragment orderAllFragment);

}
