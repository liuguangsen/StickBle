package com.liugs.stble.scan;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import java.util.List;

/**
 * Created by liuguangsen on 2019/5/8.
 */

public class BleScanConfig {
    public static final int DEFAULT_SCAN_TOME = 8000;
    private int time;

    private List<ScanFilter> bleScanFilters;

    private ScanSettings scanSettings;

    public void setBleScanFilters(List<ScanFilter> bleScanFilters) {
        this.bleScanFilters = bleScanFilters;
    }

    public void setScanSettings(ScanSettings scanSettings) {
        this.scanSettings = scanSettings;
    }

    public List<ScanFilter> getBleScanFilters() {
        return bleScanFilters;
    }

    public ScanSettings getScanSettings() {
        return scanSettings;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public static class Builder {
        private final BleScanConfig config;

        public Builder() {
            this.config = new BleScanConfig();
            this.config.setTime(DEFAULT_SCAN_TOME);
        }

        public Builder setTime(int time) {
            this.config.setTime(time);
            return this;
        }

        public Builder setBleScanFilters(List<ScanFilter> bleScanFilters) {
            this.config.setBleScanFilters(bleScanFilters);
            return this;
        }

        public Builder setScanSettings(ScanSettings scanSettings) {
            this.config.setScanSettings(scanSettings);
            return this;
        }

        public BleScanConfig build() {
            return config;
        }
    }
}
