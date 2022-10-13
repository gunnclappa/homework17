package reqres.tests;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reqres.models.ChangeUserNamePojoModel;
import reqres.models.LoginBodyPojoModel;
import reqres.models.LoginResponsePojoModel;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static reqres.specs.LoginSpecs.loginRequestSpec;
import static reqres.specs.LoginSpecs.loginResponseSpec;

public class ReqresTests {

    @Test
    @DisplayName("Тест на успешную регистрацию")
    void registerSuccessfulTest() {

        LoginBodyPojoModel body = new LoginBodyPojoModel();
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("pistol");

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("id", "4");
        expectedBody.put("token", "QpwL5tke4Pnpja7X4");

        LoginResponsePojoModel response = given()
                .spec(loginRequestSpec)
                .basePath("api/register")
                .body(body)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .body("token", notNullValue())
                .extract()
                .as(LoginResponsePojoModel.class);

        assertThat(response.getId()).isEqualTo(expectedBody.get("id"));
        assertThat(response.getToken()).isEqualTo(expectedBody.get("token"));
    }

    @Test
    @DisplayName("Тест на успешное получение имени юзера")
    void singleUserFirstNameTest() {

        String expectedFirstName = "George";

        given()
                .spec(loginRequestSpec)
                .basePath("api/users/11")
                .when()
                .get()
                .then()
                .spec(loginResponseSpec)
                .assertThat()
                .body("data.first_name", is(expectedFirstName));
    }

    @Test
    @DisplayName("Тест на успешное ненахождение юзера")
    void singleUserNotFoundTest() {

        given()
                .spec(loginRequestSpec)
                .basePath("api/users/23")
                .when()
                .get()
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Тест на успешное удаление юзера")
    void deleteUserTest() {

        given()
                .spec(loginRequestSpec)
                .basePath("/api/users/2")
                .when()
                .delete()
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    @DisplayName("Тест на успешное изменение имени и работы юзера")
    void updateUserNameTest() {

        ChangeUserNamePojoModel body = new ChangeUserNamePojoModel();
        body.setName("morpheus");
        body.setJob("zion resident");

        given()
                .spec(loginRequestSpec)
                .basePath("/api/users/12")
                .body(body)
                .when()
                .put()
                .then()
                .spec(loginResponseSpec)
                .assertThat()
                .body("name", is(body.getName()))
                .body("job", is(body.getJob()));
    }
}
