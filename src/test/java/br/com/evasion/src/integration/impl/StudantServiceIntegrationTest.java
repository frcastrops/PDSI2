package br.com.evasion.src.integration.impl;

import br.com.evasion.src.request.CreateStudantRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudantServiceIntegrationTest {

    @Test
    public void createUserRequestTest() {

        Response responseCreateOrUpdateStudant = null;
        Response responseRepportStudant = null;
        Response responseDeleteStudant = null;

        try {
            responseCreateOrUpdateStudant = createStudant();
            assertEquals(200, responseCreateOrUpdateStudant.statusCode());

            responseRepportStudant = allStudantWillEvade();
            assertEquals(200, responseRepportStudant.statusCode());

            responseDeleteStudant = deleteStudant();
            assertEquals(200, responseDeleteStudant.statusCode());

        } catch (Exception e) {
            throw e;
        }
    }

    public static Response createStudant() {
        return given().baseUri("http://localhost:8080/createOrUpdate/studant")
                .contentType(ContentType.JSON)
                .body(createStudantRequest())
                .accept("*/*")
                .log().all()
                .when()
                .post().then().log().all().extract().response();

    }

    public static Response deleteStudant() {
        String id = "11221BSI210";
        return given().baseUri("http://localhost:8080/createOrUpdate/studant")
                .contentType(ContentType.JSON)
                .body(id)
                .accept("*/*")
                .log().all()
                .when()
                .post().then().log().all().extract().response();

    }

    public static Response allStudantWillEvade() {
        return given().baseUri("http://localhost:8080/repport/studant")
                .contentType(ContentType.JSON)
                .accept("*/*")
                .log().all()
                .when()
                .get().then().log().all().extract().response();

    }

    private static CreateStudantRequest createStudantRequest() {
        return CreateStudantRequest.builder()
                .studantCRA(88)
                .idStudantRegistration("11221BSI210")
                .studantGender(Boolean.TRUE)
                .studantHasSon(Boolean.FALSE)
                .studantHaveJob(Boolean.FALSE)
                .studantLiveAlone(Boolean.FALSE)
                .studantNumAge(20)
                .studantNumDisciplines(6)
                .studantPeriod(Boolean.TRUE)
                .studantWillEvade(null)
                .userEmail("11221BSI210@ufu.br")
                .userName("11221BSI210")
                .build();
    }
}
