package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Update the name of a Specific CheckItem")
public class Update_CheckItem {
    public Response update_checkItem() {
        // Access the static board_id directly

        if (Create_CheckItem.checkitem_id == null ||Create_CheckItem.checkitem_id.isEmpty()) {
            throw new IllegalStateException("CheckItem ID is not initialized. Please ensure Create_CheckItem is executed first.");
        }

        System.out.println("Using CheckItem ID to Update CheckItem: " + Create_CheckItem.checkitem_id);
        System.out.println("Using CheckItem List o Update CheckItem: " + Create_CheckItem.checkitem_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Validate logout working correctly")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/cards/" +Create_Card.card_id+"/checkItem/"+Create_CheckItem.checkitem_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of updating a specific CheckItem is = 200")
    @Test(description = "Test that the status code of update a specific CheckItem is = 200")
    public void TestStatusCode() {
        update_checkItem().then().statusCode(200);
    }

    @Story("Test that the response time of updating a specific CheckItem is less than 3000ms")
    @Test(description = "Test that the response time of updating a specific CheckItem is less than 3000ms")
    public void TestResponseTime() {
        assert update_checkItem().getTime() < 3000;
    }

    @Story("Test that the CheckItem name updated successfully")
    @Test(description ="Test that the CheckItem name updated successfully")
    public void Validate_That_CheckItem_is_Updated_successfully() {
        Assert.assertNotEquals(update_checkItem().jsonPath().getString("name"),Create_CheckItem.checkitem_name);
    }
}
