package eu.senla.components.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Slf4j
public class ApplicationAdministrationPage extends BasePage {

    private static final By ROW_LOCATOR = By.cssSelector("table tbody tr");

    @FindBy(xpath = "//th[contains(., 'Время')]")
    private WebElement timeHeader;

    @FindBy(xpath = "//th[contains(., 'Время')]//*[name()='svg' or contains(@class,'arrow') or contains(@class,'sort')]")
    private WebElement timeSortIndicator;

    @FindBy(xpath = "//button[contains(., 'Обновить')]")
    private WebElement refreshButton;

    @FindBy(xpath = "//button[contains(., 'Закрыть')]")
    private WebElement closeButton;

    public ApplicationAdministrationPage(WebDriver driver) {
        super(driver);
    }

    public List<WebElement> rows() {
        return driver.findElements(ROW_LOCATOR);
    }

    public boolean isEmpty() {
        return driver.findElements(By.xpath("//*[contains(text(), 'В данный момент заявок нет')]"))
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    public ApplicationAdministrationPage refresh() {
        refreshButton.click();
        return this;
    }

    public void close() {
        closeButton.click();
    }

    public boolean isRefreshVisible() {
        return refreshButton.isDisplayed();
    }
}
