package eu.senla.components.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static final String USERNAME = System.getenv("APP_USERNAME");
    public static final String PASSWORD = System.getenv("APP_PASSWORD");
    public static final String TARGET_URL = System.getenv("TARGET_URL");

}