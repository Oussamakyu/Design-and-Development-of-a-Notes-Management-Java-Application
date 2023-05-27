package com.ensah.dao;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbPropertiesLoader {

    public static Properties loadProperties(String pName) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream propInputStream = loader.getResourceAsStream(pName);
        Properties properties = new Properties();
        properties.load(propInputStream);
        return properties;
    }
}

