package eu.senla.components.pages;

import eu.senla.components.data.ApplicationRow;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Slf4j
public class ApplicationAdministrationPage extends BasePage {

    private static final By TABLE_ROOT = By.cssSelector("table.MuiTable-root");
    private static final By DATA_ROW   = By.cssSelector(
            "table.MuiTable-root tr.MuiTableRow-root:not(.MuiTableRow-head)");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static final By FIRST_ROW  = By.cssSelector(
            "table.MuiTable-root tr.MuiTableRow-root:not(.MuiTableRow-head):nth-of-type(1)");

    @FindBy(xpath = "//button[contains(., 'Закрыть')]")
    private WebElement closeButton;

    public ApplicationAdministrationPage(WebDriver driver) {
        super(driver);
    }

    public List<ApplicationRow> rows() {
        return driver.findElements(DATA_ROW).stream()
                .map(ApplicationRow::new)
                .toList();
    }

    public ApplicationAdministrationPage waitUntilOpened() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(TABLE_ROOT));
        return this;
    }

    public ApplicationAdministrationPage waitUntilRowsLoaded() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(DATA_ROW, 0));
        return this;
    }

    public ApplicationRow topRow() {
        WebElement first = new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(FIRST_ROW));
        return new ApplicationRow(first);
    }

    public int getMaxApplicationId() {
        ApplicationRow top = topRow();
        int id = top.idAsInt();
        log.info("Верхняя строка: №={}, заявитель={}, тип={}, статус={}, id={}",
                top.number(), top.applicant(), top.type(), top.status(), id);
        return id;
    }

    public void close() {
        closeButton.click();
    }

    public ApplicationAdministrationPage waitUntilTopIdGreaterThan(int lastId) {
        new WebDriverWait(driver, TIMEOUT)
                .pollingEvery(Duration.ofMillis(500))
                .until(d -> {
                    try {
                        ApplicationRow top = new ApplicationRow(d.findElement(FIRST_ROW));
                        int current = top.idAsInt();
                        log.debug("top id: {}, ожидание > {}, №={}, заявитель={}",
                                current, lastId, top.number(), top.applicant());
                        return current > lastId;
                    } catch (StaleElementReferenceException | NoSuchElementException e) {
                        log.debug("строка ещё не готова: {}", e.getClass().getSimpleName());
                        return false;
                    }
                });
        return this;
    }
}