package com.report.casio.test.service;

import com.report.casio.common.annotation.Register;

@Register
public class DemoServiceImpl implements IDemoService {
    @Override
    public String sayHello() {
        return "hello";
    }
}
