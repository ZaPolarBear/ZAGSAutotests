package api;

import api.factory.UserRequestFactory;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.dto.UserResponse;
import eu.senla.components.util.ApplicationMode;
import eu.senla.components.util.TestData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("ЗАГС API")
@Feature("Регистрация брака")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarriageApiTest {

    private UserApiClient api;

    @BeforeAll
    void setUp() {
        api = new UserApiClient(TestData.USERNAME, TestData.PASSWORD);
    }

    private UserRequest buildMarriageRequest() {
        return UserRequest.builder()
                .mode(ApplicationMode.MARRIAGE)
                .personalLastName(TestData.VALID_SURNAME)
                .personalFirstName(TestData.VALID_FIRSTNAME)
                .personalMiddleName(TestData.VALID_MIDDLENAME)
                .personalPhoneNumber(TestData.VALID_PHONE_NUMBER)
                .personalNumberOfPassport(TestData.VALID_PASSPORT)
                .personalAddress(TestData.VALID_ADDRESS)
                .citizenLastName(TestData.VALID_SURNAME)
                .citizenFirstName(TestData.VALID_FIRSTNAME)
                .citizenMiddleName(TestData.VALID_MIDDLENAME)
                .citizenBirthDate(TestData.VALID_DATE)
                .citizenNumberOfPassport(TestData.VALID_PASSPORT)
                .citizenGender(TestData.VALID_GENDER)
                .citizenAddress(TestData.VALID_ADDRESS)
                .dateOfMarriage(TestData.VALID_DATE)
                .newLastName(TestData.VALID_SURNAME)
                .anotherPersonLastName(TestData.VALID_SURNAME)
                .anotherPersonFirstName(TestData.VALID_FIRSTNAME)
                .anotherPersonMiddleName(TestData.MARRIAGE_PARTNER_MIDDLENAME)
                .birthOfAnotherPerson(TestData.VALID_DATE)
                .anotherPersonPassport(TestData.VALID_PASSPORT)
                .build();
    }

    @Test
    @DisplayName("POST /sendUserRequest создаёт заявку на регистрацию брака")
    void createMarriageApplication() {
        UserRequest request = UserRequestFactory.marriage();

        UserResponse response = api.sendUserRequest(request);

        assertNotNull(response, "Ответ API не должен быть null");
        assertNotNull(response.getData().getApplicationId(), "В ответе ожидался id созданной заявки");
        assertTrue(response.getData().getApplicationId() > 0, "id должен быть положительным");
    }

    @Test
    @DisplayName("POST /sendUserRequest отвечает 200/201 и JSON")
    void responseHasCorrectHttpStatusAndContentType() {
        UserRequest request = UserRequestFactory.marriage();

        Response raw = given()
                .baseUri(TestData.TARGET_URL)
                .auth().preemptive().basic(TestData.USERNAME, TestData.PASSWORD)
                .contentType("application/json")
                .body(request)
                .when()
                .post(UserApiClient.SEND_USER_REQUEST);

        int status = raw.statusCode();
        assertTrue(status == 200 || status == 201,
                "Ожидался 200 или 201, получен: " + status + " | тело: " + raw.asString());

        assertTrue(raw.contentType().contains("application/json"),
                "Ожидался JSON, получен: " + raw.contentType());
    }

    @Test
    @DisplayName("Пустое тело запроса → 4xx")
    void emptyBodyIsRejected() {
        int status = given()
                .baseUri(TestData.TARGET_URL)
                .auth().preemptive().basic(TestData.USERNAME, TestData.PASSWORD)
                .contentType("application/json")
                .body("{}")
                .when()
                .post(UserApiClient.SEND_USER_REQUEST)
                .then()
                .extract()
                .statusCode();

        assertTrue(status >= 400 && status < 500,
                "Ожидалась клиентская ошибка 4xx, получен: " + status);
    }

    @Test
    @DisplayName("Отсутствие basic-auth → 401/403")
    void unauthorizedRequestIsRejected() {
        int status = given()
                .baseUri(TestData.TARGET_URL)
                .contentType("application/json")
                .body(buildMarriageRequest())
                .when()
                .post(UserApiClient.SEND_USER_REQUEST)
                .then()
                .extract()
                .statusCode();

        assertTrue(status == 401 || status == 403,
                "Ожидался 401/403 без авторизации, получен: " + status);
    }

    @Test
    @DisplayName("Невалидный mode → 4xx")
    void invalidModeIsRejected() {
        UserRequest bad = buildMarriageRequest();
        bad.setMode("SOMETHING_ELSE");

        int status = given()
                .baseUri(TestData.TARGET_URL)
                .auth().preemptive().basic(TestData.USERNAME, TestData.PASSWORD)
                .contentType("application/json")
                .body(bad)
                .when()
                .post(UserApiClient.SEND_USER_REQUEST)
                .then()
                .extract()
                .statusCode();

        assertTrue(status >= 400 && status < 500,
                "Ожидалась 4xx на невалидный mode, получен: " + status);
    }
}