package com.report.casio.config;

import com.report.casio.common.Constants;

// 后续通过配置文件配置读取
public class ConsumerConfig {
    // rpc调用超时时间
    private int timeout = Constants.DEFAULT_TIMEOUT;
    // 调用超时，重试次数
    private int retries = Constants.DEFAULT_RETIES;
    // 启动时是否检查服务
    private boolean check = false;

    protected void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    protected void setRetries(int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    protected void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }
}
