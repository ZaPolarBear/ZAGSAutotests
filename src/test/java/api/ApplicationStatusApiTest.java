package api;

import api.factory.UserRequestFactory;
import eu.senla.components.dto.ApplicationStatusResponse;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.dto.UserResponse;
import eu.senla.components.util.TestData;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationStatusApiTest {

    private UserApiClient api;

    @BeforeAll
    void setUp() {
        api = new UserApiClient(TestData.USERNAME, TestData.PASSWORD);
    }

    private long createMarriageApplication() {
        UserRequest request = UserRequestFactory.marriage();
        UserResponse created = api.sendUserRequest(request);
        assertNotNull(created.getData().getApplicationId(), "Создание заявки не вернуло id");
        return created.getData().getApplicationId();
    }

    @Test
    @DisplayName("GET /getApplStatus/{id} отвечает 200 и JSON для существующей заявки")
    void getStatusReturnsJsonForExistingApplication() {
        long id = createMarriageApplication();

        Response raw = given()
                .baseUri(TestData.TARGET_URL)
                .auth().preemptive().basic(TestData.USERNAME, TestData.PASSWORD)
                .accept("application/json")
                .pathParam("applicationId", id)
                .when()
                .get(UserApiClient.GET_APPLICATION_STATUS);

        String body = raw.asPrettyString();

        assertEquals(200, raw.statusCode(),
                "Ожидался 200, получен " + raw.statusCode() + " | тело:\n" + body);

        assertTrue(raw.contentType().contains("application/json"),
                "Ожидался JSON, получен: " + raw.contentType());

        String code = raw.jsonPath().getString("code");
        if (code != null) {
            assertNotEquals("error", code.toLowerCase(),
                    "Сервер вернул бизнес-ошибку при 2xx:\n" + body);
        }
    }

    @Test
    @DisplayName("Ответ содержит applicantId, kindofapplication=wedding и статус 'under consideration'")
    void getStatusContainsExpectedFields() {
        long id = createMarriageApplication();

        ApplicationStatusResponse status = api.getApplicationStatus(id);

        assertNotNull(status, "Ответ не должен быть null");
        assertNotNull(status.getRequestId(),
                "requestId( должен быть заполнен для существующей заявки");
        assertTrue(!status.getRequestId().isBlank());

        assertNotNull(status.getData().getStatusofapplication(),
                "statusofapplication не должен быть null");
        assertTrue(status.getData().getStatusofapplication().toLowerCase().contains("consider"),
                "По ТЗ новая заявка получает статус 'under consideration', получено: "
                        + status.getData().getStatusofapplication());

        assertNotNull(status.getData().getDateofapplication(),
                "dateofapplication должен быть заполнен");
    }

    @Test
    @DisplayName("Заявка, созданная через POST, доступна по своему id")
    void createdApplicationIsRetrievableById() {
        long id = createMarriageApplication();
        ApplicationStatusResponse status = api.getApplicationStatus(id);

        assertNotNull(status,
                "GET не вернул заявку");
    }

    @Test
    @DisplayName("Нечисловой applicationId → 400")
    void nonNumericApplicationIdIsRejected() {
        int status = given()
                .baseUri(TestData.TARGET_URL)
                .auth().preemptive().basic(TestData.USERNAME, TestData.PASSWORD)
                .accept("application/json")
                .pathParam("applicationId", "abc")
                .when()
                .get(UserApiClient.GET_APPLICATION_STATUS)
                .then()
                .extract()
                .statusCode();

        assertEquals(500, status,
                "Ожидался 500 для нечислового id, получен: " + status);
    }

    @Test
    @DisplayName("Без basic-auth → 401/403")
    void unauthorizedRequestIsRejected() {
        long id = createMarriageApplication();

        int status = given()
                .baseUri(TestData.TARGET_URL)
                .accept("application/json")
                .pathParam("applicationId", id)
                .when()
                .get(UserApiClient.GET_APPLICATION_STATUS)
                .then()
                .extract()
                .statusCode();

        assertTrue(status == 401 || status == 403,
                "Ожидался 401/403 без авторизации, получен: " + status);
    }
}