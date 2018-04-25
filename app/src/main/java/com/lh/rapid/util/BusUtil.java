package com.lh.rapid.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by WE-WIN-027 on 2016/9/8.
 *
 * @des ${TODO}
 */
public class BusUtil {

    private static Bus bus = null;

    public static Bus getBus() {
        if (bus == null) {
            synchronized (BusUtil.class) {
                if (bus == null) {
                    bus = new Bus(ThreadEnforcer.ANY);
                }
            }
        }
        return bus;
    }

}
