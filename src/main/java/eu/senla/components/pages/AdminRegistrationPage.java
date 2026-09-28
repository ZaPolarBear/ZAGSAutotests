package eu.senla.components.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AdminRegistrationPage extends BasePage {

    @FindBy(id = "TextInputField-1")
    private WebElement surnameField;

    @FindBy(id = "TextInputField-2")
    private WebElement firstnameField;

    @FindBy(id = "TextInputField-3")
    private WebElement middlenameField;

    @FindBy(id = "TextInputField-4")
    private WebElement phoneField;

    @FindBy(id = "TextInputField-5")
    private WebElement passportField;

    @FindBy(id = "TextInputField-6")
    private WebElement birthDateField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement nextButton;

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
        surnameField.sendKeys(surname);
        firstnameField.sendKeys(firstname);
        middlenameField.sendKeys(middlename);
        phoneField.sendKeys(phone);
        passportField.sendKeys(passport);
        birthDateField.sendKeys(birthDate);
        return this;
    }

    public ApplicationAdministrationPage submit() {
        nextButton.click();
        return new ApplicationAdministrationPage(driver);
    }
}