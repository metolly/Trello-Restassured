package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
@Feature("Delete a Specific Board")
public class Delete_Board {

    private Response response; // Store the response to reuse in tests

    @BeforeClass
    public void deleteBoard() {
        // Access the static board_id from Create_Board
        String boardId = Create_Board.board_id;

        // Ensure board_id is initialized
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID for Deletion: " + boardId);

        // Perform the DELETE request to delete the board
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // Access APIKey directly
                .queryParam("token", Create_Board.token) // Access token directly
                .contentType(ContentType.JSON)
                .when()
                .delete("/1/boards/" + boardId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the Status code of Deleting a Specific board is = 200")
    @Test(description = "Test that the Status code of Deleting a Specific board is = 200")
    public void testStatusCode() {
        // Validate the status code of the DELETE request
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test That the Response time Of Deleting a Specific board is less than 3000ms")
    @Test(description = "Test That the Response time Of Deleting a Specific board is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the DELETE request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms: " + responseTime);
    }

    @Story("Test That the Board Deleted Successfully")
    @Test(description = "Test That the Board Deleted Successfully")
    public void Validate_That_Board_is_Deleted_successfully() {
        Get_Board getBoard = new Get_Board();
        Response getListResponse = getBoard.getBoard();

        // Verify that the list is deleted by checking the status code
        int getStatusCode = getListResponse.getStatusCode();
        System.out.println("Get Status Code: " + getStatusCode);

        // If list is deleted, the response should return 404 (not found)
        assertEquals(getStatusCode, 404, "Expected status code 404 but got: " + getStatusCode);
    }
}
