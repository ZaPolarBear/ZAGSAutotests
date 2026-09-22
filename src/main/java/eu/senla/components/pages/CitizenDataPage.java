package eu.senla.components.pages;

import eu.senla.components.pages.service.ServiceDataPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Supplier;

public class CitizenDataPage<T extends ServiceDataPage> extends BasePage {

    @FindBy(id = "TextInputField-7")
    private WebElement surnameField;

    @FindBy(id = "TextInputField-8")
    private WebElement firstnameField;

    @FindBy(id = "TextInputField-9")
    private WebElement middlenameField;

    @FindBy(id = "TextInputField-10")
    private WebElement birthDateField;

    @FindBy(id = "TextInputField-11")
    private WebElement passportField;

    @FindBy(id = "TextInputField-12")
    private WebElement genderField;

    @FindBy(id = "TextInputField-13")
    private WebElement addressField;

    @FindBy(css = "button:has(svg[data-icon='arrow-right'])")
    private WebElement nextButton;

    private final Supplier<T> nextPageFactory;

    public CitizenDataPage(WebDriver driver, Supplier<T> nextPageFactory) {
        super(driver);
        this.nextPageFactory = nextPageFactory;
    }

    public CitizenDataPage<T> fillForm(String surname,
                                       String firstname,
                                       String middlename,
                                       String birthDate,
                                       String passport,
                                       String gender,
                                       String address) {
        surnameField.sendKeys(surname);
        firstnameField.sendKeys(firstname);
        middlenameField.sendKeys(middlename);
        birthDateField.sendKeys(birthDate);
        passportField.sendKeys(passport);
        genderField.sendKeys(gender);
        addressField.sendKeys(address);
        return this;
    }

    public T submit() {
        nextButton.click();
        return nextPageFactory.get();
    }
}