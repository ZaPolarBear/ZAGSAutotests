package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import eu.senla.components.pages.Locators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MarriageServiceDataPage extends BasePage implements ServiceDataPage {

    private static final By REGISTRATION_DATE = Locators.byExactLabel("Дата регистрации");
    private static final By NEW_SURNAME = Locators.byExactLabel("Новая фамилия");
    private static final By PARTNER_SURNAME = Locators.byExactLabel("Фамилия супруга/и");
    private static final By PARTNER_FIRSTNAME = Locators.byExactLabel("Имя супруга/и");
    private static final By PARTNER_MIDDLENAME = Locators.byExactLabel("Отчество супруга/и");
    private static final By PARTNER_BIRTHDATE = Locators.byExactLabel("Дата рождения супруга/и");
    private static final By PARTNER_PASSPORT = Locators.byExactLabel("Номер паспорта супруга/и");
    private static final By NEXT = Locators.endButton();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public MarriageServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public MarriageServiceDataPage fillForm(
            String registrationDate,
            String newSurname,
            String partnerSurname,
            String partnerFirstname,
            String partnerMiddlename,
            String partnerBirthDate,
            String partnerPassport) {
        type(REGISTRATION_DATE, registrationDate);
        type(NEW_SURNAME, newSurname);
        type(PARTNER_SURNAME, partnerSurname);
        type(PARTNER_FIRSTNAME, partnerFirstname);
        type(PARTNER_MIDDLENAME, partnerMiddlename);
        type(PARTNER_BIRTHDATE, partnerBirthDate);
        type(PARTNER_PASSPORT, partnerPassport);
        return this;
    }

    @Override
    public ApplicationStatusPage submit() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(NEXT))
                .click();
        return new ApplicationStatusPage(driver);
    }
}