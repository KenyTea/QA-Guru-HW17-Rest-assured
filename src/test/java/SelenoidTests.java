import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    // make request to https://selenoid.autotests.cloud/status
    // total: 20

    @Test
    void checkTotal20() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotal20WithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotal20WithResponseAndBadPractice() {
        String response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response().asString();

        System.out.println(response);

        // DONT DO THAT, BAD PRACTICE
        assertEquals("{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":" +
                "{\"chrome\":{\"90.0\":{},\"91.0\":{}},\"firefox\":{\"88.0\":{},\"89.0\":{}}," +
                "\"opera\":{\"76.0\":{},\"77.0\":{}}}}\n", response);
    }

    @Test
    void checkTotal20WithResponse() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(response);

        assertEquals(20, response);
    }

    @Test
    void checkTotal20WithTalkAboutResponse() {
        Response response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response();

        System.out.println(response);  // io.restassured.internal.RestAssuredResponseImpl@25401706
        System.out.println(response.toString()); // io.restassured.internal.RestAssuredResponseImpl@25401706
        System.out.println(response.asString()); // {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"ch
        System.out.println(response.path("total") + ""); // 20
        System.out.println(response.path("browsers.chrome").toString()); // {90.0={}, 91.0={}}

    }

    @Test
    void checkTotal20WithAssertJ() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(response);

        assertThat(response).isEqualTo(20);
    }

    @Test
    void checkWdHubStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void checkWdHubStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void checkWdHubStatus200WithAuth() {
        given()
                .auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }

}
