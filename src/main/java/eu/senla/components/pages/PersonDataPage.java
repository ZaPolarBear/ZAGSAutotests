package eu.senla.components.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class PersonDataPage extends BasePage {

    @FindBy(css = "input[placeholder*='Введите фамилию']")
    private WebElement surnameField;

    @FindBy(css = "input[placeholder*='Введите имя']")
    private WebElement firstnameField;

    @FindBy(css = "input[placeholder*='Введите отчество']")
    private WebElement middlenameField;

    @FindBy(css = "input[placeholder*='Введите номер телефона']")
    private WebElement phoneField;

    @FindBy(css = "input[placeholder*='Введите номер паспорта']")
    private WebElement passportField;

    @FindBy(css = "input[placeholder*='Введите адрес']")
    private WebElement addressField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement nextButton;

    public PersonDataPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        return surnameField.isDisplayed()
                && firstnameField.isDisplayed()
                && nextButton.isDisplayed();
    }

    public boolean isFormEmpty() {
        List<WebElement> fields = List.of(
                surnameField, firstnameField, middlenameField,
                phoneField, passportField, addressField);
        return fields.stream()
                .map(f -> f.getAttribute("value"))
                .allMatch(v -> v == null || v.isEmpty());
    }

    public PersonDataPage fillForm(
            String surname,
            String firstname,
            String middlename,
            String phone,
            String passport,
            String address) {
        surnameField.sendKeys(surname);
        firstnameField.sendKeys(firstname);
        middlenameField.sendKeys(middlename);
        phoneField.sendKeys(phone);
        passportField.sendKeys(passport);
        addressField.sendKeys(address);
        return this;
    }

    public ServiceSelectionPage submit() {
        nextButton.click();
        return new ServiceSelectionPage(driver);
    }
}