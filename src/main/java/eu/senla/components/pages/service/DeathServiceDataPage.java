package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeathServiceDataPage extends BasePage implements ServiceDataPage {

    @FindBy(id = "TextInputField-14")
    private WebElement deathDateField;

    @FindBy(id = "TextInputField-15")
    private WebElement deathPlaceField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement submitButton;

    public DeathServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public DeathServiceDataPage fillForm(String deathDate,
                                         String deathPlace) {
        deathDateField.sendKeys(deathDate);
        deathPlaceField.sendKeys(deathPlace);
        return this;
    }

    @Override
    public ApplicationStatusPage submit() {
        submitButton.click();
        return new ApplicationStatusPage(driver);
    }
}
