package br.com.evasion.src.integration.impl;

import br.com.evasion.src.enums.UserTypeEnum;
import br.com.evasion.src.request.AuthUserRequest;
import br.com.evasion.src.request.CreateUserRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceIntegrationTest {
    @Test
    public void UserRequestSucessTest() {

        Response responseCreateUser = null;
        Response responseAuthUser = null;
        Response responseAllUser = null;
        Response responseDeleteUser = null;

        try {
            responseCreateUser = createUser();
            assertEquals("Teste", responseCreateUser.getBody().asString().split("\n")[0]);
            assertEquals(200, responseCreateUser.statusCode());

            responseAuthUser = authUser();
            assertEquals("Teste", responseAuthUser.getBody().asString().split("\n")[0]);
            assertEquals(200, responseAuthUser.statusCode());

            responseAllUser = allUser();
            assertEquals(200, responseAllUser.statusCode());

            responseDeleteUser = deleteUser();
            assertEquals(200, responseDeleteUser.statusCode());

        } catch (Exception e) {
            throw e;
        }
    }

    public static Response createUser() {
        return given().baseUri("http://localhost:8080/create/user")
                .contentType(ContentType.JSON)
                .body(createUserRequest())
                .accept("*/*")
                .log().all()
                .when()
                .post().then().log().all().extract().response();

    }

    public static Response authUser() {
        return given().baseUri("http://localhost:8080/auth/user")
                .contentType(ContentType.JSON)
                .body(createAuthRequest())
                .accept("*/*")
                .log().all()
                .when()
                .post().then().log().all().extract().response();

    }

    public static Response allUser() {
        return given().baseUri("http://localhost:8080/all/user")
                .contentType(ContentType.JSON)
                .body(createUserRequest())
                .accept("*/*")
                .log().all()
                .when()
                .get().then().log().all().extract().response();
    }

    public static Response deleteUser() {
        return given().baseUri("http://localhost:8080/delete/user")
                .contentType(ContentType.JSON)
                .body(createUserRequest())
                .accept("*/*")
                .log().all()
                .when()
                .post().then().log().all().extract().response();
    }

    private static CreateUserRequest createUserRequest() {
        return CreateUserRequest.builder()
                .userName("Teste")
                .userPassword("12345")
                .userType(UserTypeEnum.ADMINISTRATOR)
                .userEmail("teste@teste")
                .build();
    }

    private static AuthUserRequest createAuthRequest() {
        return AuthUserRequest.builder()
                .login("teste@teste")
                .senha("12345")
                .build();
    }
}
