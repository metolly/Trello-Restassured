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
@Feature("Delete a Specific Card")
public class Delete_Card {

    private Response response;

    @BeforeClass
    public void deleteCard() {

        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        System.out.println("Using Card ID for Deletion: " + Create_Card.card_id);


        // Step 1: Perform the DELETE request to delete the list
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // Access APIKey directly
                .queryParam("token", Create_Board.token) // Access token directly
                .contentType(ContentType.JSON)
                .when()
                .delete("/1/cards/" + Create_Card.card_id)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the Status code of Deleting a Specific Card is = 200")
    @Test(description = "Test that the Status code of Deleting a Specific Card is = 200")
    public void testStatusCode() {
        // Validate the status code of the DELETE request
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test That the Response time Of Deleting a Specific Card is less than 3000ms")
    @Test(description = "Test That the Response time Of Deleting a Specific Card is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the DELETE request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms: " + responseTime);
    }

    @Story("Test That the Card Deleted Successfully")
    @Test(description = "Test That the Card Deleted Successfully")
    public void Validate_That_Card_is_Deleted_successfully() {
        Get_Card getCard = new Get_Card();
        Response getcardResponse = getCard.GetCard();
        // Verify that the list is deleted by checking the status code
        int getStatusCode = getcardResponse.getStatusCode();
        System.out.println("Get Status Code: " + getStatusCode);

        // If list is deleted, the response should return 404 (not found)
        assertEquals(getStatusCode, 404, "Expected status code 404 but got: " + getStatusCode);
    }
}
