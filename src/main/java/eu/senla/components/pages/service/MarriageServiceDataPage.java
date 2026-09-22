package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MarriageServiceDataPage extends BasePage implements ServiceDataPage {

    @FindBy(id = "TextInputField-14")
    private WebElement registrationDateField;

    @FindBy(id = "TextInputField-15")
    private WebElement newSurnameField;

    @FindBy(id = "TextInputField-16")
    private WebElement partnerSurnameField;

    @FindBy(id = "TextInputField-17")
    private WebElement partnerFirstnameField;

    @FindBy(id = "TextInputField-18")
    private WebElement partnerMiddlenameField;

    @FindBy(id = "TextInputField-19")
    private WebElement partnerBirthDateField;

    @FindBy(id = "TextInputField-20")
    private WebElement partnerPassportField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement submitButton;

    public MarriageServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public MarriageServiceDataPage fillForm(String registrationDate,
                                            String newSurname,
                                            String partnerSurname,
                                            String partnerFirstname,
                                            String partnerMiddlename,
                                            String partnerBirthDate,
                                            String partnerPassport) {
        registrationDateField.sendKeys(registrationDate);
        newSurnameField.sendKeys(newSurname);
        partnerSurnameField.sendKeys(partnerSurname);
        partnerFirstnameField.sendKeys(partnerFirstname);
        partnerMiddlenameField.sendKeys(partnerMiddlename);
        partnerBirthDateField.sendKeys(partnerBirthDate);
        partnerPassportField.sendKeys(partnerPassport);
        return this;
    }

    @Override
    public ApplicationStatusPage submit() {
        submitButton.click();
        return new ApplicationStatusPage(driver);
    }
}