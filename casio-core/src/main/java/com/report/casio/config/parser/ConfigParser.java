package com.report.casio.config.parser;

import com.report.casio.common.annotation.SPI;
import com.report.casio.common.exception.ContextException;
import com.report.casio.config.context.RpcConfigContext;

// 解析config配置项
@SPI("yaml")
public interface ConfigParser {

    RpcConfigContext parse(String path) throws ContextException;

}
