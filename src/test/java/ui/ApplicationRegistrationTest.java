package ui;

import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.CitizenDataPage;
import eu.senla.components.pages.HomePage;
import eu.senla.components.pages.PersonDataPage;
import eu.senla.components.pages.service.BirthServiceDataPage;
import eu.senla.components.util.TestData;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationRegistrationTest {

    private ChromeDriver driver;

    @BeforeAll
    void setUp() {
        driver = DriverSingleton.getInstance();
        driver.register(UsernameAndPassword.of(TestData.USERNAME, TestData.PASSWORD));
    }

    @AfterAll
    void tearDown() {
        DriverSingleton.quit();
    }

    @BeforeEach
    void openApp() {
        driver.get(TestData.TARGET_URL);
    }

    @Test
    @DisplayName("Регистрация брака")
    void marriageRegistration() {
        log.info("Создание новой заявки регистрации брака");

        ApplicationStatusPage status = new HomePage(driver)
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

        assertTrue(status.isUpdateButtonDisplayed());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));
    }

    @Test
    @DisplayName("Регистрация рождения")
    void birthRegistration() {
        log.info("Создание новой заявки регистрации рождения");

        CitizenDataPage<BirthServiceDataPage> citizenStep = new HomePage(driver)
                .clickLogin()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_PHONE_NUMBER, TestData.VALID_PASSPORT, TestData.VALID_ADDRESS)
                .submit()
                .selectBirth();

        ApplicationStatusPage status = citizenStep
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_DATE, TestData.VALID_PASSPORT, TestData.VALID_GENDER, TestData.VALID_ADDRESS)
                .submit()
                .fillForm(TestData.BIRTH_PLACE, TestData.MOTHER, TestData.FATHER,
                        TestData.GRANDMOTHER, TestData.GRANDFATHER)
                .submit();

        assertTrue(status.isUpdateButtonDisplayed());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));
    }

    @Test
    @DisplayName("Регистрация смерти")
    void deathRegistration() {
        log.info("Создание новой заявки регистрации смерти");

        ApplicationStatusPage status = new HomePage(driver)
                .clickLogin()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_PHONE_NUMBER, TestData.VALID_PASSPORT, TestData.VALID_ADDRESS)
                .submit()
                .selectDeath()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_DATE, TestData.VALID_PASSPORT, TestData.VALID_GENDER, TestData.VALID_ADDRESS)
                .submit()
                .fillForm(TestData.DEATH_DATE, TestData.DEATH_PLACE)
                .submit();

        assertTrue(status.isUpdateButtonDisplayed());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));
    }

    @Test
    @DisplayName("Обновление статуса заявки и создание новой")
    void refreshAndCreateNewApplication() {
        log.info("Создание новой заявки");

        ApplicationStatusPage status = new HomePage(driver)
                .clickLogin()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_PHONE_NUMBER, TestData.VALID_PASSPORT, TestData.VALID_ADDRESS)
                .submit()
                .selectDeath()
                .fillForm(TestData.VALID_SURNAME, TestData.VALID_FIRSTNAME, TestData.VALID_MIDDLENAME,
                        TestData.VALID_DATE, TestData.VALID_PASSPORT, TestData.VALID_GENDER, TestData.VALID_ADDRESS)
                .submit()
                .fillForm(TestData.DEATH_DATE, TestData.DEATH_PLACE)
                .submit();

        log.info("Обновление страницы с готовой заявкой");

        status.refresh();
        assertTrue(status.isUpdateButtonEnabled());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));

        PersonDataPage newApplication = status.createNewApplication();

        assertAll("Создание новой заявки",
                () -> assertTrue(newApplication.isOpened(),
                        "Должна открыться страница ввода персональных данных"),
                () -> assertTrue(newApplication.isFormEmpty(),
                        "Форма новой заявки должна быть пустой"));
    }
}