package ui;

import api.factory.UserRequestFactory;
import eu.senla.components.data.ApplicationData;
import eu.senla.components.data.ApplicationRow;
import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.pages.ApplicationAdministrationPage;
import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.HomePage;
import eu.senla.components.util.TestData;
import lombok.extern.slf4j.Slf4j;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminApplicationTest {

    private ChromeDriver driver;

    @BeforeAll
    void setUpDriver() {
        driver = DriverSingleton.getInstance();
        driver.register(UsernameAndPassword.of(TestData.USERNAME, TestData.PASSWORD));
    }

    @BeforeEach
    void openHome() {
        driver.get(TestData.TARGET_URL);
    }

    @AfterAll
    void tearDown() {
        DriverSingleton.quit();
    }

    @Step("Создание заявки на регистрацию брака")
    private void createMarriageApplicationAsUser() {
        driver.get(TestData.TARGET_URL);

        ApplicationStatusPage statusPage = new HomePage(driver)
                .clickLogin()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.phone(), UserRequestFactory.passport(), UserRequestFactory.address()
                         )
                .submit()
                .selectMarriage()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport(),
                        TestData.VALID_GENDER, UserRequestFactory.address()
                         )
                .submit()
                .fillForm(
                        UserRequestFactory.inputDate(UserRequestFactory.pastDate()), UserRequestFactory.surname(),
                        UserRequestFactory.surname(),
                        UserRequestFactory.firstname(), UserRequestFactory.femaleMiddlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport()
                         )
                .submit();

        statusPage.waitForThankYouMessage();
    }

    @Step("Вход в панель администрирования")
    private ApplicationAdministrationPage loginAsAdmin() {
        driver.get(TestData.TARGET_URL);

        log.info("Вход в панель администрирования");

        return new HomePage(driver)
                .clickLoginAsAdmin()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.phone(), UserRequestFactory.passport(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate())
                         )
                .submit();
    }

    @Test
    @DisplayName("Созданная пользователем заявка о браке появляется в админской таблице")
    void createdMarriageApplicationAppearsInAdminTable() {
        ApplicationAdministrationPage admin = loginAsAdmin()
                .waitUntilOpened()
                .waitUntilRowsLoaded();

        int lastId = admin.getMaxApplicationId();
        admin.close();

        createMarriageApplicationAsUser();

        admin = loginAsAdmin()
                .waitUntilOpened()
                .waitUntilRowsLoaded()
                .waitUntilTopIdGreaterThan(lastId);

        ApplicationRow latest = admin.topRow();
        assertAll(
                () -> assertTrue(latest.idAsInt() > lastId, "id должен быть > " + lastId),
                () -> assertTrue(latest.isMarriageCertificate(), "тип: " + latest.type()),
                () -> assertEquals("На рассмотрении", latest.status())
                 );
    }
}