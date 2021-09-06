package com.report.casio.config;

import com.report.casio.common.Constants;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.config.parser.ConfigParser;
import lombok.SneakyThrows;

public class CasioConfigInitHandler {

    @SneakyThrows
    public void init() {
        // 直接定义为项目根目录 | 使用ComponentScan的方式(感觉不太需要)
        RpcContextFactory.getBeanContext().setServiceScanPackage("com.report.casio.test");
        RpcContextFactory.getBeanContext().init();

        ConfigParser extension = ExtensionLoader.getExtensionLoader(ConfigParser.class).getDefaultExtension();
        extension.parse(Constants.DEFAULT_CONFIG_PATH);
    }

}
