package eu.senla.components.pages;
import eu.senla.components.pages.service.BirthServiceDataPage;
import eu.senla.components.pages.service.DeathServiceDataPage;
import eu.senla.components.pages.service.MarriageServiceDataPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ServiceSelectionPage extends BasePage {

    @FindBy(xpath = "//button[text()='Регистрация брака']")
    private WebElement marriageButton;

    @FindBy(xpath = "//button[text()='Регистрация рождения']")
    private WebElement birthButton;

    @FindBy(xpath = "//button[text()='Регистрация смерти']")
    private WebElement deathButton;

    public ServiceSelectionPage(WebDriver driver) {
        super(driver);
    }

    public CitizenDataPage<MarriageServiceDataPage> selectMarriage() {
        marriageButton.click();
        return new CitizenDataPage<>(driver, () -> new MarriageServiceDataPage(driver));
    }

    public CitizenDataPage<BirthServiceDataPage> selectBirth() {
        birthButton.click();
        return new CitizenDataPage<>(driver, () -> new BirthServiceDataPage(driver));
    }

    public CitizenDataPage<DeathServiceDataPage> selectDeath() {
        deathButton.click();
        return new CitizenDataPage<>(driver, () -> new DeathServiceDataPage(driver));
    }
}