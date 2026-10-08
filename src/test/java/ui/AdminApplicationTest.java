package ui;

import api.UserApiClient;
import api.factory.UserRequestFactory;
import eu.senla.components.data.ApplicationRow;
import eu.senla.components.driver.DriverSingleton;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.dto.UserResponse;
import eu.senla.components.pages.ApplicationAdministrationPage;
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
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminApplicationTest {

    private WebDriver driver;

    @BeforeAll
    void setUpDriver() {
        driver = new Augmenter().augment(DriverSingleton.getInstance());
        ((HasAuthentication) driver)
                .register(UsernameAndPassword.of(TestData.USERNAME, TestData.PASSWORD));
    }

    @BeforeEach
    void openHome() {
        driver.get(TestData.TARGET_URL);
    }

    @AfterAll
    void tearDown() {
        DriverSingleton.quit();
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

    private long createMarriageApplication() {
        UserApiClient api = new UserApiClient(TestData.USERNAME, TestData.PASSWORD);
        UserRequest request = UserRequestFactory.marriage();
        UserResponse created = api.sendUserRequest(request);
        assertNotNull(created.getData().getApplicationId(), "Создание заявки не вернуло id");
        return created.getData().getApplicationId();
    }


    @Test
    @DisplayName("Созданная пользователем заявка о браке появляется в админской таблице")
    void createdMarriageApplicationAppearsInAdminTable() {
        long lastId = createMarriageApplication();

        ApplicationAdministrationPage admin = loginAsAdmin()
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