package reqres.tests;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reqres.models.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static reqres.specs.LoginSpecs.loginRequestSpec;
import static reqres.specs.LoginSpecs.loginResponseSpec;

public class ReqresTests {

    @Test
    @DisplayName("Тест на успешную регистрацию")
    void registerSuccessfulTest() {

        LoginBodyLombokModel body = new LoginBodyLombokModel();
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("pistol");

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("id", "4");
        expectedBody.put("token", "QpwL5tke4Pnpja7X4");

        LoginResponseLombokModel response = given()
                .spec(loginRequestSpec)
                .body(body)
                .when()
                .post("/register")
                .then()
                .spec(loginResponseSpec)
                .body("token", notNullValue())
                .extract()
                .as(LoginResponseLombokModel.class);

        assertThat(response.getId()).isEqualTo(expectedBody.get("id"));
        assertThat(response.getToken()).isEqualTo(expectedBody.get("token"));

        given()
                .spec(loginRequestSpec)
                .when()
                .get("/users")
                .then()
                .spec(loginResponseSpec)
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
    }

    @Test
    @DisplayName("Тест на успешное получение имени юзера")
    void singleUserFirstNameTest() {

        String expectedFirstName = "George";

        given()
                .spec(loginRequestSpec)
                .when()
                .get("/users/11")
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
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    @DisplayName("Тест на успешное удаление юзера")
    void deleteUserTest() {

        given()
                .spec(loginRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    @DisplayName("Тест на успешное изменение имени и работы юзера")
    void updateUserNameTest() {

        ChangeUserNameLombokModel body = new ChangeUserNameLombokModel();
        body.setName("morpheus");
        body.setJob("zion resident");

        given()
                .spec(loginRequestSpec)
                .body(body)
                .when()
                .put("/users/12")
                .then()
                .spec(loginResponseSpec)
                .assertThat()
                .body("name", is(body.getName()))
                .body("job", is(body.getJob()));
    }
}
