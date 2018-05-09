package com.lh.rapid.ui.main;

import com.lh.rapid.injector.PerActivity;
import com.lh.rapid.injector.component.ApplicationComponent;
import com.lh.rapid.injector.module.ActivityModule;
import com.lh.rapid.ui.fragment1.Fragment1;
import com.lh.rapid.ui.fragment2.Fragment2;
import com.lh.rapid.ui.fragment3.Fragment3;
import com.lh.rapid.ui.fragment4.Fragment4;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {
        ActivityModule.class,MainModule.class
})
public interface MainComponent {

    void inject(MainActivity activity);

    void inject(Fragment1 fragment1);

    void inject(Fragment2 fragment2);

    void inject(Fragment3 fragment3);

    void inject(Fragment4 fragment4);

}