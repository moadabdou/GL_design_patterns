package com.glproject.util;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private static volatile Configuration instance;
    private final Properties properties = new Properties();
    private static final String FILE_NAME = "config.properties";

    private Configuration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getInstance() {
        if (instance == null) {
            synchronized (Configuration.class) {
                if (instance == null) {
                    instance = new Configuration();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(FILE_NAME)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
