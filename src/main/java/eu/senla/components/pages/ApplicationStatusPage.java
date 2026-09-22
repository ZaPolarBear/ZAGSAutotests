package eu.senla.components.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ApplicationStatusPage extends BasePage {

    @FindBy(css = "button:has(svg[data-icon='refresh'])")
    private WebElement updateButton;

    @FindBy(xpath = "//button[contains(., 'Создать новую заявку')]")
    private WebElement createNewApplicationButton;

    @FindBy(xpath = "//*[contains(text(), 'Спасибо за обращение')]")
    private WebElement thankYouText;

    @FindBy(xpath = "//*[contains(text(), 'Статус заявки')]")
    private WebElement statusText;

    public ApplicationStatusPage(WebDriver driver) {
        super(driver);
    }

    public ApplicationStatusPage refresh() {
        updateButton.click();
        return this;
    }

    public PersonDataPage createNewApplication() {
        createNewApplicationButton.click();
        return new PersonDataPage(driver);
    }

    public boolean isUpdateButtonDisplayed() {
        return updateButton.isDisplayed();
    }

    public boolean isUpdateButtonEnabled() {
        return updateButton.isEnabled();
    }

    public String thankYouMessage() {
        return thankYouText.getText();
    }

    public String statusMessage() {
        return statusText.getText();
    }
}