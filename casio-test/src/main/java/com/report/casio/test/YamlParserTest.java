package com.report.casio.test;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlParserTest {
    
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlParserTest.class
                .getClassLoader()
                .getResourceAsStream("casio.yml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }

}
