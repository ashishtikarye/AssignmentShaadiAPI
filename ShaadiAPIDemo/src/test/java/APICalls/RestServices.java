package APICalls;

import constant.Endpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static constant.Endpoints.PAGE;
import static constant.Endpoints.NAME;
import static constant.Endpoints.JOB;


public class RestServices {


    @BeforeClass
    public void setup() {
        RestAssured.baseURI = Endpoints.BASE_URL;
    }

    @Test(invocationCount = 10)
    public void getUsersList() {
        Response response = RestAssured
                .given()
                .header("Content-Type", ContentType.JSON)
                .request()
                .queryParam(PAGE,2)
                .get(Endpoints.GETUSERLIST)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response();

        Assert.assertEquals(HttpStatus.SC_OK,response.getStatusCode());
        System.out.println(response.getBody().asPrettyString());
    }

    @Test(invocationCount = 10,dependsOnMethods = "getUsersList")
    public void updateUser() {

        JSONObject requestParams = new JSONObject();
        requestParams.put(NAME, "Ashish Tikarye");
        requestParams.put(JOB, "Automation Engineer");

        Response response = RestAssured
                .given()
                .header("Content-Type", ContentType.JSON)
                .request().body(requestParams.toString())
                .post(Endpoints.UPDATEUSER)
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response();
        Assert.assertEquals("Ashish Tikarye",response.jsonPath().get("name"));

    }

    @Test(invocationCount = 10,dependsOnMethods = "getUsersList")
    public void updateJob() {

        JSONObject requestParams = new JSONObject();
        requestParams.put(NAME, "Ashish Tikarye");
        requestParams.put(JOB, "Automation Tester");
        Response response = RestAssured
                .given()
                .header("Content-Type", ContentType.JSON)
                .request().body(requestParams.toString())
                .put(Endpoints.UPDATEUSER)
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response();

        Assert.assertEquals("Automation Tester",response.jsonPath().get("job"));
    }

    @Test(invocationCount = 10,dependsOnMethods = "updateJob")
    public void deleteUser() {
        Response response = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("Content-Type", ContentType.JSON)
                .request()
                .delete(Endpoints.DELETEUSER)
                .then().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().response();

    }
}
