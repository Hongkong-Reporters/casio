package com.report.casio.config.parser;

import com.report.casio.common.annotation.SPI;
import com.report.casio.common.exception.ContextException;

// 解析config配置项
@SPI("yaml")
public interface ConfigParser {

    void parse(String path) throws ContextException;

}
