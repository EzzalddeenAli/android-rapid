package com.lh.rapid.ui.addaddress;


import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;

import dagger.Component;

/**
 * Created by lh on 2017/10/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface AddShippingAddressComponent {
    void inject(AddShippingAddressActivity activity);
}
