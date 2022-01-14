package guru.qa;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void singleUserTest() {
        given()
                .when()
                .get("api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void listResourceTest() {
        given()
                .when()
                .get("api/unknown")
                .then()
                .statusCode(200)
                .body("page", is(1))
                .body("total", is(12))
                .body("data[0].id", is(1))
                .body("data[1].name", is("fuchsia rose"))
                .body("data[2].year", is(2002));
    }

    @Test
    void listResourceWithForTest() {
        Response data =
                given()
                        .when()
                        .get("api/unknown")
                        .then()
                        .extract().response();
        ArrayList<Object> dataList = data.path("data");
        ArrayList<String> names = new ArrayList<>();
        names.add("cerulean");
        names.add("fuchsia rose");
        names.add("true red");
        names.add("aqua sky");
        names.add("tigerlily");
        names.add("blue turquoise");
        int i = 0;
        for (Object x : dataList) {
            if (i + 1 == (int) ((LinkedHashMap) x).get("id")) {
                assertEquals(((LinkedHashMap) x).get("name"), names.get(i));
            }
            i++;
        }
    }

    @Test
    void singleResourceTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void updateTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .put("api/users?page=2")
                .then()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }
}
