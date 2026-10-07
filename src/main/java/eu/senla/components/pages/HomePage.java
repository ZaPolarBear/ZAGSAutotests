package eu.senla.components.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(css = "button:has(svg[data-icon='person'])")
    private WebElement userLoginButton;

    @FindBy(xpath = "//button[contains(., 'Войти как администратор')]")
    private WebElement adminLoginButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public PersonDataPage clickLogin() {
        userLoginButton.click();
        return new PersonDataPage(driver);
    }

    public AdminRegistrationPage clickLoginAsAdmin() {
        adminLoginButton.click();
        return new AdminRegistrationPage(driver);
    }
}