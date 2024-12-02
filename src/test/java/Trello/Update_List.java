package Trello;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Update the name of a Specific List")
public class Update_List {

    public Response update_list() {
        // Access the static board_id directly

        String ListId = Create_List.list_id;
        String ListName = Create_List.list_name;
        Create_Board c = new Create_Board();
        if (ListId == null || ListId.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using List ID to Update List: " + ListId);
        System.out.println("Using Board List o Update List: " + ListName);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","In Progress")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/lists/" + Create_List.list_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of updating a specific List is = 200")
    @Test(description = "Test that the status code of update a specific List is = 200")
    public void TestStatusCode() {
       update_list().then().statusCode(200);
    }

    @Story("Test that the response time of updating a specific List is less than 3000ms")
    @Test(description = "Test that the response time of updating a specific List is less than 3000ms")
    public void TestResponseTime() {
        assert update_list().getTime() < 3000;
    }

    @Story("Test that the List name updated successfully")
    @Test(description ="Test that the List name updated successfully")
    public void Validate_That_List_is_Updated_successfully() {
        Assert.assertNotEquals(update_list().jsonPath().getString("name"),Create_List.list_name);
    }
}

