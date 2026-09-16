package eu.senla.components;


import org.openqa.selenium.By;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Main {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "senlatest";
    public static final String TAGET_URL = "https://regoffice.senla.eu/";
    public static final String USER_LOGIN_BUTTON = "button:has(svg[data-icon='person'])";
    public static final String SURNAME_SELECTOR = "input[placeholder*='Введите фамилию']";
    public static final String FIRSTNAME_SELECTOR = "input[placeholder*='Введите имя']";
    public static final String MIDDLENAME_SELECTOR = "input[placeholder*='Введите отчество']";
    public static final String PHONE_SELECTOR = "input[placeholder*='Введите номер телефона']";
    public static final String PASSPORT_SELECTOR = "input[placeholder*='Введите номер паспорта']";
    public static final String ADDRESS_SELECTOR = "input[placeholder*='Введите адрес']";
    public static final String VALID_SURNAME = "Иванов";
    public static final String VALID_FIRSTNAME = "Иван";
    public static final String VALID_MIDDLENAME = "Иванович";
    public static final String VALID_PHONE_NUMBER = "1723727";
    public static final String VALID_DATE = "15092026";
    public static final String VALID_GENDER = "Муж";
    public static final String VALID_PASSPORT = "PS1234";
    public static final String VALID_ADDRESS = "ул. Иванович д. 3 кв. 7";
    public static final String SUBMIT_SELECTOR = "button:has(svg[data-icon='arrow-right'])";
    public static final String UPDATE_SELECTOR = "button:has(svg[data-icon='refresh'])";
    public static final String XPATH_MARRIAGE_SELECTOR = "//button[text()='Регистрация брака']";
    public static final String MARRIAGE_SURNAME_SELECTOR = "#TextInputField-7";
    public static final String MARRIAGE_FIRSTNAME_SELECTOR = "#TextInputField-8";
    public static final String MARRIAGE_MIDDLENAME_SELECTOR = "#TextInputField-9";
    public static final String MARRIAGE_DATE_SELECTOR = "#TextInputField-10";
    public static final String MARRIAGE_PASSPORT_SELECTOR = "#TextInputField-11";
    public static final String MARRIAGE_GENDER_SELECTOR = "#TextInputField-12";
    public static final String MARRIAGE_ADDRESS_SELECTOR = "#TextInputField-13";
    public static final String REGISTRATION_DATE_SELECTOR = "#TextInputField-14";
    public static final String NEW_SURNAME_SELECTOR = "#TextInputField-15";
    public static final String PARTNER_SURNAME_SELECTOR = "#TextInputField-16";
    public static final String PARTNER_FIRSTNAME_SELECTOR = "#TextInputField-17";
    public static final String PARTNER_MIDDLENAME_SELECTOR = "#TextInputField-18";
    public static final String PARTNER_DATE_SELECTOR = "#TextInputField-19";
    public static final String PARTNER_PASSPORT_SELECTOR = "#TextInputField-20";

    static void main() {
        ChromeDriver driver = new ChromeDriver();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        driver.register(UsernameAndPassword.of(USERNAME, PASSWORD));

        driver.get(TAGET_URL);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.findElement(By.cssSelector(USER_LOGIN_BUTTON)).click();

        WebElement surnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(SURNAME_SELECTOR)));
        WebElement firstnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(FIRSTNAME_SELECTOR)));
        WebElement middlenameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MIDDLENAME_SELECTOR)));
        WebElement phoneField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PHONE_SELECTOR)));
        WebElement passportField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PASSPORT_SELECTOR)));
        WebElement addressField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(ADDRESS_SELECTOR)));
        WebElement submitButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(SUBMIT_SELECTOR)));


        surnameField.sendKeys(VALID_SURNAME);
        firstnameField.sendKeys(VALID_FIRSTNAME);
        middlenameField.sendKeys(VALID_MIDDLENAME);
        phoneField.sendKeys(VALID_PHONE_NUMBER);
        passportField.sendKeys(VALID_PASSPORT);
        addressField.sendKeys(VALID_ADDRESS);

        submitButton.click();

        WebElement marinageRegistrationButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_MARRIAGE_SELECTOR)));

        marinageRegistrationButton.click();

        surnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_SURNAME_SELECTOR)));
        firstnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_FIRSTNAME_SELECTOR)));
        middlenameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_MIDDLENAME_SELECTOR)));
        WebElement dateField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_DATE_SELECTOR)));
        passportField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_PASSPORT_SELECTOR)));
        WebElement genderField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_GENDER_SELECTOR)));
        addressField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(MARRIAGE_ADDRESS_SELECTOR)));
        submitButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(SUBMIT_SELECTOR)));

        surnameField.sendKeys(VALID_SURNAME);
        firstnameField.sendKeys(VALID_FIRSTNAME);
        middlenameField.sendKeys(VALID_MIDDLENAME);
        dateField.sendKeys(VALID_DATE);
        passportField.sendKeys(VALID_PASSPORT);
        genderField.sendKeys(VALID_GENDER);
        addressField.sendKeys(VALID_ADDRESS);

        submitButton.click();

        WebElement registrationDateField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(REGISTRATION_DATE_SELECTOR)));
        WebElement newSurnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(NEW_SURNAME_SELECTOR)));
        WebElement partnerSurnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PARTNER_SURNAME_SELECTOR)));
        WebElement partnerFirstnameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PARTNER_FIRSTNAME_SELECTOR)));
        WebElement partnerDateField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PARTNER_DATE_SELECTOR)));
        WebElement partnerMiddlenameField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PARTNER_MIDDLENAME_SELECTOR)));
        WebElement partnerPassportField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(PARTNER_PASSPORT_SELECTOR)));
        submitButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(SUBMIT_SELECTOR)));

        registrationDateField.sendKeys(VALID_DATE);
        newSurnameField.sendKeys(VALID_SURNAME);
        partnerSurnameField.sendKeys(VALID_SURNAME);
        partnerFirstnameField.sendKeys(VALID_FIRSTNAME);
        partnerDateField.sendKeys(VALID_DATE);
        partnerMiddlenameField.sendKeys(VALID_MIDDLENAME);
        partnerPassportField.sendKeys(VALID_PASSPORT);

        submitButton.click();

        WebElement updateField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(UPDATE_SELECTOR)));

        assertTrue(updateField.isDisplayed());
        assertTrue(updateField.isEnabled());

        driver.quit();
    }
}
