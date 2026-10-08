package eu.senla.components.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminRegistrationPage extends BasePage {

    private static final By SURNAME = Locators.byLabel("Фамилия");
    private static final By FIRSTNAME = Locators.byLabel("Имя");
    private static final By MIDDLENAME = Locators.byLabel("Отчество");
    private static final By PHONE = Locators.byLabel("Телефон");
    private static final By PASSPORT = Locators.byLabel("Номер паспорта");
    private static final By BIRTHDATE = Locators.byLabel("Дата рождения");
    private static final By NEXT = Locators.nextButton();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public AdminRegistrationPage(WebDriver driver) {
        super(driver);
    }

    public AdminRegistrationPage fillForm(
            String surname,
            String firstname,
            String middlename,
            String phone,
            String passport,
            String birthDate) {
        type(SURNAME, surname);
        type(FIRSTNAME, firstname);
        type(MIDDLENAME, middlename);
        type(PHONE, phone);
        type(PASSPORT, passport);
        type(BIRTHDATE, birthDate);
        return this;
    }

    public ApplicationAdministrationPage submit() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(NEXT))
                .click();
        return new ApplicationAdministrationPage(driver);
    }
}