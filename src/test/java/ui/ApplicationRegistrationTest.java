package ui;

import api.factory.UserRequestFactory;
import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.pages.ApplicationStatusPage;
import eu.senla.components.pages.CitizenDataPage;
import eu.senla.components.pages.HomePage;
import eu.senla.components.pages.PersonDataPage;
import eu.senla.components.pages.service.BirthServiceDataPage;
import eu.senla.components.util.Gender;
import eu.senla.components.util.TestData;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationRegistrationTest {

    private WebDriver driver;

    @BeforeAll
    void setUp() {
        driver = new Augmenter().augment(DriverSingleton.getInstance());
        ((HasAuthentication) driver)
                .register(UsernameAndPassword.of(TestData.USERNAME, TestData.PASSWORD));
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
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.phone(), UserRequestFactory.passport(), UserRequestFactory.address()
                         )
                .submit()
                .selectMarriage()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport(),
                        Gender.MALE, UserRequestFactory.address()
                         )
                .submit()
                .fillForm(
                        UserRequestFactory.inputDate(UserRequestFactory.pastDate()), UserRequestFactory.surname(),
                        UserRequestFactory.surname(),
                        UserRequestFactory.firstname(), UserRequestFactory.femaleMiddlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport()
                         )
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
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.phone(), UserRequestFactory.passport(), UserRequestFactory.address()
                         )
                .submit()
                .selectBirth();

        ApplicationStatusPage status = citizenStep
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport(),
                        Gender.MALE, UserRequestFactory.address()
                         )
                .submit()
                .fillForm(
                        UserRequestFactory.address(), UserRequestFactory.firstname(), UserRequestFactory.firstname(),
                        UserRequestFactory.firstname(), UserRequestFactory.firstname()
                         )
                .submit();

        assertTrue(status.isUpdateButtonDisplayed());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));
    }

    @Test
    @DisplayName("Регистрация смерти")
    void deathRegistration() {
        log.info("Создание новой заявки регистрации смерти");

        ApplicationStatusPage status = createDeathApplication();

        assertTrue(status.isUpdateButtonDisplayed());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));
    }

    @Test
    @DisplayName("Обновление статуса заявки и создание новой")
    void refreshAndCreateNewApplication() {
        log.info("Создание новой заявки");

        ApplicationStatusPage status = createDeathApplication();

        log.info("Обновление страницы с готовой заявкой");

        status.refresh();
        assertTrue(status.isUpdateButtonEnabled());
        assertTrue(status.thankYouMessage().contains("Спасибо за обращение"));

        PersonDataPage newApplication = status.createNewApplication();

        assertAll(
                "Создание новой заявки",
                () -> assertTrue(
                        newApplication.isOpened(),
                        "Должна открыться страница ввода персональных данных"
                                ),
                () -> assertTrue(
                        newApplication.isFormEmpty(),
                        "Форма новой заявки должна быть пустой"
                                )
                 );
    }

    @Step("Создание заявки на регистрацию смерти")
    private ApplicationStatusPage createDeathApplication() {
        return new HomePage(driver)
                .clickLogin()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.phone(), UserRequestFactory.passport(), UserRequestFactory.address()
                         )
                .submit()
                .selectDeath()
                .fillForm(
                        UserRequestFactory.surname(), UserRequestFactory.firstname(), UserRequestFactory.middlename(),
                        UserRequestFactory.inputDate(UserRequestFactory.birthDate()), UserRequestFactory.passport(),
                        Gender.MALE, UserRequestFactory.address()
                         )
                .submit()
                .fillForm(UserRequestFactory.inputDate(UserRequestFactory.pastDate()), UserRequestFactory.address())
                .submit();
    }
}