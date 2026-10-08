package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import eu.senla.components.pages.Locators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BirthServiceDataPage extends BasePage implements ServiceDataPage {

    private static final By BIRTH_PLACE = Locators.byExactLabel("Место рождения");
    private static final By MOTHER = Locators.byExactLabel("Мать");
    private static final By FATHER = Locators.byExactLabel("Отец");
    private static final By GRANDMOTHER = Locators.byExactLabel("Бабушка");
    private static final By GRANDFATHER = Locators.byExactLabel("Дедушка");
    private static final By NEXT = Locators.endButton();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public BirthServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public BirthServiceDataPage fillForm(
            String birthPlace,
            String mother,
            String father,
            String grandmother,
            String grandfather) {
        type(BIRTH_PLACE, birthPlace);
        type(MOTHER, mother);
        type(FATHER, father);
        type(GRANDMOTHER, grandmother);
        type(GRANDFATHER, grandfather);
        return this;
    }

    @Override
    public ApplicationStatusPage submit() {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(NEXT))
                .click();
        return new ApplicationStatusPage(driver);
    }
}