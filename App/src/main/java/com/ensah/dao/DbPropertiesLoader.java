package com.ensah.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbPropertiesLoader {
    public DbPropertiesLoader() {
    }

    public static Properties loadPoperties(String pName) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream propInputStream = loader.getResourceAsStream(pName);
        Properties properties = new Properties();
        properties.load(propInputStream);
        return properties;
    }

    public static void main(String[] args) throws IOException {
        Properties p = loadPoperties("conf.properties");
        System.out.println(p.get("db.url"));
        System.out.println(p.get("db.login"));
    }
}
