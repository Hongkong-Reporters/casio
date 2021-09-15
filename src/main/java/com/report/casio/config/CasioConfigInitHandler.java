package com.report.casio.config;

import com.report.casio.common.Constants;
import com.report.casio.common.extension.ExtensionLoader;
import com.report.casio.config.context.RpcContextFactory;
import com.report.casio.config.parser.ConfigParser;
import lombok.SneakyThrows;

public class CasioConfigInitHandler {

    @SneakyThrows
    public void init() {
        ConfigParser extension = ExtensionLoader.getExtensionLoader(ConfigParser.class).getDefaultExtension();
        extension.parse(Constants.DEFAULT_CONFIG_PATH);
        RpcContextFactory.getBeanContext().init();
    }

}
