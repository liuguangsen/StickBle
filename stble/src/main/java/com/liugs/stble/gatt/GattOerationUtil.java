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

    public static boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = address.charAt(i);
            switch (i % 3) {
                case 0:
                case 1:
                    if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F')) {
                        // hex character, OK
                        break;
                    }
                    return false;
                case 2:
                    if (c == ':') {
                        break;  // OK
                    }
                    return false;
            }
        }
        return true;
    }
}
