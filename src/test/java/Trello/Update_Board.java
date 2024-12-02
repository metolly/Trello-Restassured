package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Update the name of a Specific Board")
@Test(priority = 3)
public class Update_Board {

    public Response update_board() {
        // Access the static board_id directly

        String boardId = Create_Board.board_id;
        String boardName = Create_Board.board_name;
        Create_Board c = new Create_Board();
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID to Update board: " + boardId);
        System.out.println("Using Board Name to Update board: " + boardName);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParams("name","RestAssured")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token)
                .contentType(ContentType.JSON)
                .when()
                .put("/1/boards/" + boardId)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of updating a specific board is = 200")
    @Test(description = "Test that the status code of update a specific board is = 200")
    public void TestStatusCode() {
        update_board().then().statusCode(200);
    }

    @Story("Test that the response time of updating a specific board is less than 3000ms")
    @Test(description = "Test that the response time of updating a specific board is less than 3000ms")
    public void TestResponseTime() {
        assert update_board().getTime() < 3000;
    }

    @Story("Test that the board name updated successfully")
    @Test(description ="Test that the board name updated successfully" )
    public void Validate_That_Board_is_Updated_successfully() {
        Assert.assertNotEquals(update_board().jsonPath().getString("name"),Create_Board.board_name);
    }
}

