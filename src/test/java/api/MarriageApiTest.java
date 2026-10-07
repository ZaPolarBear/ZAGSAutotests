package api;

import api.factory.UserRequestFactory;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.dto.UserResponse;
import eu.senla.components.util.TestData;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarriageApiTest {

    private UserApiClient api;

    @BeforeAll
    void setUp() {
        api = new UserApiClient(TestData.USERNAME, TestData.PASSWORD);
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
    @DisplayName("POST /sendUserRequest отвечает 200 и JSON")
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
        assertEquals(
                HttpStatus.SC_OK,
                status, "Ожидался 200, получен: %s | тело: %s".formatted(status, raw.asString())
                    );

        assertTrue(
                raw.contentType().contains("application/json"),
                "Ожидался JSON, получен: " + raw.contentType()
                  );
    }

    @Test
    @DisplayName("Пустое тело запроса → 400")
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


        assertEquals(HttpStatus.SC_BAD_REQUEST, status, "Ожидалась клиентская ошибка 400, получен: " + status);
    }

    @Test
    @DisplayName("Отсутствие basic-auth → 401")
    void unauthorizedRequestIsRejected() {
        int status = given()
                .baseUri(TestData.TARGET_URL)
                .contentType("application/json")
                .body(UserRequestFactory.marriage())
                .when()
                .post(UserApiClient.SEND_USER_REQUEST)
                .then()
                .extract()
                .statusCode();

        assertEquals(
                HttpStatus.SC_UNAUTHORIZED, status,
                "Ожидался 401 без авторизации, получен: %s".formatted(status)
                    );
    }

    @Test
    @DisplayName("Невалидный mode → 400")
    void invalidModeIsRejected() {
        UserRequest bad = UserRequestFactory.marriage();
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

        assertEquals(
                HttpStatus.SC_BAD_REQUEST, status,
                "Ожидалась 400 на невалидный mode, получен: %s".formatted(status)
                    );
    }
}