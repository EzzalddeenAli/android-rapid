package com.lh.rapid.util;

/**
 * Created by we-win on 2017/3/14.
 */

public class CommonEvent {

    public class PaySuccessEvent {

        public PaySuccessEvent() {

        }

    }

    public class TabSelectedEvent {
        public int position;

        public TabSelectedEvent(int position) {
            this.position = position;
        }
    }

}