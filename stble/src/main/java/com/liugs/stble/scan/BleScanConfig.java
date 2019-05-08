package com.liugs.stble.scan;

/**
 * Created by liuguangsen on 2019/5/8.
 */

public class BleScanConfig {
    private int time;

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    class Builder{
        private BleScanConfig config;
        private int time;

        public Builder setTime(int time) {
            this.time = time;
            this.config.setTime(time);
            return this;
        }

        public BleScanConfig build(){
            return config;
        }
    }
}
