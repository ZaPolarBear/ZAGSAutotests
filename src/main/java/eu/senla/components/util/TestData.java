package eu.senla.components.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestData {

    public static final String USERNAME = env("APP_USERNAME");
    public static final String PASSWORD = env("APP_PASSWORD");
    public static final String TARGET_URL = env("TARGET_URL");

    public static String env(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Не задана переменная окружения " + name + ".\n" +
                            "Задайте её через -D" + name + "=... , env, или Gradle-проброс.");
        }
        return value;
    }

    public static String envOr(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
