package eu.senla.components.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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