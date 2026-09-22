package ui;

import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.pages.ApplicationAdministrationPage;
import eu.senla.components.pages.HomePage;
import eu.senla.components.util.TestData;
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

    private void createMarriageApplicationAsUser() {
        driver.get(TestData.TARGET_URL);

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

    private ApplicationAdministrationPage loginAsAdmin() {
        driver.get(TestData.TARGET_URL);

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

        assertFalse(admin.isEmpty(), "Ожидалась хотя бы одна заявка, но таблица пуста");
    }
}