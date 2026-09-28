package ui;

import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.pages.ApplicationAdministrationPage;
import eu.senla.components.pages.HomePage;
import eu.senla.components.util.TestData;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
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

        log.info("Создание заявки на регистрацию брака");
        new HomePage(driver)
                .clickLogin()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_PHONE_NUMBER, TestData.VALID_PASSPORT, TestData.VALID_ADDRESS)
                .submit()
                .selectMarriage()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_DATE, TestData.VALID_PASSPORT, TestData.VALID_GENDER, TestData.VALID_ADDRESS)
                .submit()
                .fillForm(TestData.VALID_DATE, TestData.VALID_SURNAME, TestData.VALID_SURNAME,
                        TestData.VALID_FIRSTNAME, TestData.MARRIAGE_PARTNER_MIDDLENAME,
                        TestData.VALID_DATE, TestData.VALID_PASSPORT)
                .submit();
    }

    @Step("Вход в панель администрирования")
    private ApplicationAdministrationPage loginAsAdmin() {
        driver.get(TestData.TARGET_URL);

        log.info("Вход в панель администрирования");

        return new HomePage(driver)
                .clickLoginAsAdmin()
                .fillForm(
                        TestData.ADMIN_SURNAME,
                        TestData.ADMIN_FIRSTNAME,
                        TestData.ADMIN_MIDDLENAME,
                        TestData.ADMIN_PHONE,
                        TestData.ADMIN_PASSPORT,
                        TestData.ADMIN_BIRTH_DATE
                         )
                .submit();
    }

    @Test
    @DisplayName("Администратор видит созданную пользователем заявку в таблице")
    void newlyCreatedApplicationIsVisible() {
        createMarriageApplicationAsUser();
        ApplicationAdministrationPage admin = loginAsAdmin();

        log.info("Проверка видимости заявок");

        assertFalse(admin.isEmpty(), "Ожидалась хотя бы одна заявка, но таблица пуста");
    }
}