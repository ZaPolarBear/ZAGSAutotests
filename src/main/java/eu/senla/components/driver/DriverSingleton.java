package eu.senla.components.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public final class DriverSingleton {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private static final String  SELENOID_URL =
            System.getProperty("SELENOID_URL", "http://selenoid:4444/wd/hub");
    private static final boolean REMOTE =
            Boolean.parseBoolean(System.getProperty("browser.remote", System.getenv("SELENOID_MODE")));

    private DriverSingleton() {}

    public static WebDriver getInstance() {
        WebDriver current = DRIVER.get();
        if (current == null) {
            current = REMOTE ? createRemote() : createLocal();
            current.manage().timeouts().implicitlyWait(Duration.ZERO);
            DRIVER.set(current);
        }
        return current;
    }

    public static void quit() {
        WebDriver current = DRIVER.get();
        if (current != null) {
            try {
                current.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    private static WebDriver createRemote() {
        try {
            return new RemoteWebDriver(URI.create(SELENOID_URL).toURL(), chromeOptions());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Некорректный URL Selenoid: " + SELENOID_URL, e);
        }
    }

    private static WebDriver createLocal() {
        return new ChromeDriver(chromeOptions());
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (REMOTE) {
            options.setBrowserVersion(System.getProperty("browser.version", "128.0"));
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        return options;
    }
}