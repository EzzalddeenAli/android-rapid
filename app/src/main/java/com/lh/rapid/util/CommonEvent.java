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

    public class LocationEvent {

        public double latitude;
        public double longitude;
        public String addrStr;

        public LocationEvent(double latitude, double longitude, String addrStr) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.addrStr = addrStr;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAddrStr() {
            return addrStr;
        }

        public void setAddrStr(String addrStr) {
            this.addrStr = addrStr;
        }
    }

    public class ProductCartCountEvent {

        public int count;

        public ProductCartCountEvent(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

    }


}