package com.lh.rapid.ui.orderlist;

import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;
import com.lh.rapid.ui.orderlist.all.OrderAllFragment;
import com.lh.rapid.ui.orderlist.canc.OrderCancFragment;
import com.lh.rapid.ui.orderlist.distribution.OrderDistributionFragment;
import com.lh.rapid.ui.orderlist.finished.OrderFinishedFragment;
import com.lh.rapid.ui.orderlist.preparing.OrderPreparingFragment;
import com.lh.rapid.ui.orderlist.waitpay.OrderWaitPayFragment;

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

    void inject(OrderCancFragment orderAllFragment);

    void inject(OrderDistributionFragment orderAllFragment);

    void inject(OrderFinishedFragment orderAllFragment);

    void inject(OrderPreparingFragment orderAllFragment);

    void inject(OrderWaitPayFragment orderAllFragment);

}
