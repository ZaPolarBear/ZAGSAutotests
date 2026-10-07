package eu.senla.components.pages;

import eu.senla.components.pages.service.ServiceDataPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Supplier;

public class CitizenDataPage<T extends ServiceDataPage> extends BasePage {

    private static final By SURNAME = Locators.byLabel("Фамилия");
    private static final By FIRSTNAME = Locators.byLabel("Имя");
    private static final By MIDDLENAME = Locators.byLabel("Отчество");
    private static final By BIRTHDATE = Locators.byLabel("Дата рождения");
    private static final By PASSPORT = Locators.byLabel("Номер паспорта");
    private static final By GENDER = Locators.byLabel("Пол");
    private static final By ADDRESS = Locators.byPlaceholder("Введите адрес прописки");
    private static final By NEXT = Locators.nextButton();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final Supplier<T> nextPageFactory;

    public CitizenDataPage(WebDriver driver, Supplier<T> nextPageFactory) {
        super(driver);
        this.nextPageFactory = nextPageFactory;
    }

    public CitizenDataPage<T> fillForm(
            String surname,
            String firstname,
            String middlename,
            String birthDate,
            String passport,
            String gender,
            String address) {
        type(SURNAME, surname);
        type(FIRSTNAME, firstname);
        type(MIDDLENAME, middlename);
        type(BIRTHDATE, birthDate);
        type(PASSPORT, passport);
        type(GENDER, gender);
        type(ADDRESS, address);
        return this;
    }

    public T submit() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(NEXT))
                .click();
        return nextPageFactory.get();
    }

    private void type(By locator, String value) {
        WebElement field = new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        field.clear();
        field.sendKeys(value);
    }
}