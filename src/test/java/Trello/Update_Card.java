package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Update the name of a Specific Card")
public class Update_Card {

    public Response update_card() {
        // Access the static board_id directly

        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        System.out.println("Using Card ID to Update Card: " + Create_Card.card_id);
        System.out.println("Using Card List o Update Card: " + Create_Card.card_name);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","Logout")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/cards/" + Create_Card.card_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of updating a specific Card is = 200")
    @Test(description = "Test that the status code of update a specific Card is = 200")
    public void TestStatusCode() {
        update_card().then().statusCode(200);
    }

    @Story("Test that the response time of updating a specific card is less than 3000ms")
    @Test(description = "Test that the response time of updating a specific card is less than 3000ms")
    public void TestResponseTime() {
        assert update_card().getTime() < 3000;
    }

    @Story("Test that the card name updated successfully")
    @Test(description ="Test that the card name updated successfully" )
    public void Validate_That_Card_is_Updated_successfully() {
        Assert.assertNotEquals(update_card().jsonPath().getString("name"),Create_Card.card_name);
    }

}
