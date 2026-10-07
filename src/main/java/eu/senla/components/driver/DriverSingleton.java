package eu.senla.components.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;

public final class DriverSingleton {

    private static volatile ChromeDriver instance;

    private DriverSingleton() {
    }

    public static ChromeDriver getInstance() {
        if (instance == null) {
            synchronized (DriverSingleton.class) {
                if (instance == null) {
                    instance = new ChromeDriver();
                    DevTools devTools = instance.getDevTools();
                    devTools.createSession();
                }
            }
        }
        return instance;
    }

    public static void quit() {
        if (instance != null) {
            instance.quit();
            instance = null;
        }
    }
}