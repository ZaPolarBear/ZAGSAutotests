package eu.senla.components.pages.service;

import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.BasePage;
import eu.senla.components.pages.Locators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DeathServiceDataPage extends BasePage implements ServiceDataPage {

    private static final By DEATH_DATE = Locators.byExactLabel("Дата смерти");
    private static final By DEATH_PLACE = Locators.byExactLabel("Место смерти");
    private static final By NEXT = Locators.endButton();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    public DeathServiceDataPage(WebDriver driver) {
        super(driver);
    }

    public DeathServiceDataPage fillForm(String deathDate, String deathPlace) {
        type(DEATH_DATE, deathDate);
        type(DEATH_PLACE, deathPlace);
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
