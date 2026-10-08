package api;

import eu.senla.components.dto.ApplicationStatusResponse;
import eu.senla.components.dto.UserRequest;
import eu.senla.components.dto.UserResponse;
import eu.senla.components.util.TestData;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserApiClient {

    public static final String SEND_USER_REQUEST = "/sendUserRequest";
    public static final String GET_APPLICATION_STATUS = "/getApplStatus/{applicationId}";

    private final RequestSpecification spec;

    public UserApiClient(String username, String password) {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(TestData.TARGET_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setAuth(RestAssured.basic(username, password))
                .log(LogDetail.ALL)
                .build();
    }

    @Step("POST /sendUserRequest — создание заявки (mode={request.mode})")
    public UserResponse sendUserRequest(UserRequest request) {
        Response raw = given()
                .spec(spec)
                .body(request)
                .when()
                .post(SEND_USER_REQUEST)
                .then()
                .log().all()
                .extract().response();

        int status = raw.statusCode();
        String body = raw.asString();

        assertEquals(200, status);

        UserResponse response;
        try {
            response = raw.as(UserResponse.class);
        } catch (Exception e) {
            throw new AssertionError(
                    "Не удалось десериализовать UserResponse (HTTP " + status + "). Тело: " + body, e);
        }

        assertNotNull(response, "Пустой ответ. Тело: " + body);
        assertNotNull(response.getData());
        attachResponse(raw);
        return response;
    }

    @Step("GET /getApplStatus/{applicationId} — получить статус заявки id={applicationId}")
    public ApplicationStatusResponse getApplicationStatus(long applicationId) {
        Response response = given()
                .spec(spec)
                .pathParam("applicationId", applicationId)
                .when()
                .get(GET_APPLICATION_STATUS)
                .then()
                .log().all()
                .extract()
                .response();

        attachResponse(response);

        int status = response.statusCode();
        String body = response.asPrettyString();

        if (status < 200 || status >= 300) {
            throw new ApiException(
                    status,
                    "Ожидался 2xx, получен " + status + ". Тело ответа:\n" + body
            );
        }

        if ("error".equalsIgnoreCase(response.jsonPath().getString("code"))) {
            throw new ApiException(status, "Сервер вернул бизнес-ошибку:\n" + body);
        }

        return response.as(ApplicationStatusResponse.class);
    }

    @Attachment(value = "response.json", type = "application/json")
    private String attachResponse(Response response) {
        return response.asPrettyString();
    }
}