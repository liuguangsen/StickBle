package com.liugs.stble.gatt;

public class GattOerationUtil {

    public static String getSateDescraption(@GattOperation.State int state) {
        String descraption;
        switch (state) {
            case GattOperation.STATE_FIRST_CONNECT_START_FAILED:
                descraption = "第一次链接失败了";
                break;
            default:
                descraption = "STATE_UNKNOWN";
                break;
        }
        return descraption;
    }
}
