package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BirthServiceDataPage extends BasePage implements ServiceDataPage {

    @FindBy(id = "TextInputField-14")
    private WebElement birthPlaceField;

    @FindBy(id = "TextInputField-15")
    private WebElement motherField;

    @FindBy(id = "TextInputField-16")
    private WebElement fatherField;

    @FindBy(id = "TextInputField-17")
    private WebElement grandmotherField;

    @FindBy(id = "TextInputField-18")
    private WebElement grandfatherField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement submitButton;

    public BirthServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public BirthServiceDataPage fillForm(
            String birthPlace,
            String mother,
            String father,
            String grandmother,
            String grandfather) {
        birthPlaceField.sendKeys(birthPlace);
        motherField.sendKeys(mother);
        fatherField.sendKeys(father);
        grandmotherField.sendKeys(grandmother);
        grandfatherField.sendKeys(grandfather);
        return this;
    }

    @Override
    public ApplicationStatusPage submit() {
        submitButton.click();
        return new ApplicationStatusPage(driver);
    }
}