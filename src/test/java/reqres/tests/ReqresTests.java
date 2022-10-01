package reqres.tests;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {

    private static final int STATUSCODE200 = 200;
    private static final int STATUSCODE404 = 404;
    private static final int STATUSCODE204 = 204;
    private static final String CONTENTTYPEJSON = "application/json";

    @Test
    @DisplayName("Тест на успешную регистрацию")
    void registerSuccessfulTest() {

        final String uri = "https://reqres.in/api/register";

        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "eve.holt@reqres.in");
        requestParams.put("password", "pistol");

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("id", 4);
        expectedBody.put("token", "QpwL5tke4Pnpja7X4");

        String actualBody = given()
                .log().uri()
                .contentType(CONTENTTYPEJSON)
                .body(requestParams.toString())
                .when()
                .post(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUSCODE200)
                .extract().response().getBody().asString();

        assertEquals(expectedBody.toString(), actualBody);
    }

    @Test
    @DisplayName("Тест на успешное получение имени юзера")
    void singleUserFirstNameTest() {

        final String uri = "https://reqres.in/api/users/11";

        String expectedFirstName = "George";

        given()
                .log().uri()
                .when()
                .get(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUSCODE200)
                .assertThat()
                .body("data.first_name", is(expectedFirstName));
    }

    @Test
    @DisplayName("Тест на успешное ненахождение юзера")
    void singleUserNotFoundTest() {

        final String uri = "https://reqres.in/api/users/23";

        given()
                .log().uri()
                .when()
                .get(uri)
                .then()
                .log().status()
                .statusCode(STATUSCODE204);
    }


    @Test
    @DisplayName("Тест на успешное удаление юзера")
    void deleteUserTest() {

        final String uri = "https://reqres.in/api/users/2";

        given()
                .log().uri()
                .when()
                .delete(uri)
                .then()
                .log().status()
                .statusCode(STATUSCODE404);
    }

    @Test
    @DisplayName("Тест на успешное изменение имени и работы юзера")
    void updateUserNameTest() {
        final String uri = "https://reqres.in/api/users/12";

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "morpheus");
        requestParams.put("job", "zion resident");

        given()
                .log().uri()
                .log().body()
                .contentType(CONTENTTYPEJSON)
                .body(requestParams.toString())
                .when()
                .put(uri)
                .then()
                .log().status()
                .log().body()
                .statusCode(STATUSCODE200)
                .assertThat()
                .body("name", is(requestParams.get("name")))
                .body("job", is(requestParams.get("job")));
    }
}
