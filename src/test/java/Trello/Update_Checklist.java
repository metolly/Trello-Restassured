package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Update the name of a Specific CheckList")
public class Update_Checklist {

    public Response update_checklist() {
        // Access the static board_id directly

        if (Create_Checklist.checklist_id == null ||Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        System.out.println("Using Checklist ID to Update Checklist: " + Create_Checklist.checklist_id);
        System.out.println("Using Checklist List o Update Checklist: " + Create_Checklist.checklist_name);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Valid Test Cases")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/checklists/" +Create_Checklist.checklist_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of updating a specific CheckList is = 200")
    @Test(description = "Test that the status code of update a specific CheckList is = 200")
    public void TestStatusCode() {
        update_checklist().then().statusCode(200);
    }

    @Story("Test that the response time of updating a specific CheckList is less than 3000ms")
    @Test(description = "Test that the response time of updating a specific CheckList is less than 3000ms")
    public void TestResponseTime() {
        assert update_checklist().getTime() < 3000;
    }

    @Story("Test that the CheckList name updated successfully")
    @Test(description ="Test that the CheckList name updated successfully")
    public void Validate_That_Checklist_is_Updated_successfully() {
        Assert.assertNotEquals(update_checklist().jsonPath().getString("name"),Create_Checklist.checklist_name);
    }
}
