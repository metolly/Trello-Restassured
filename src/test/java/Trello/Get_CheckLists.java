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
@Feature("Retrieve CheckLists")
public class Get_CheckLists {

    private Response response;
    @BeforeClass
    public void fetchChecklists() {

        // Validate if the board_id is initialized
        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        System.out.println("Using Card ID to fetch checklists: " + Create_Card.card_id);

        // Fetch all lists on the board
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // API Key
                .queryParam("token", Create_Board.token) // API Token
                .contentType(ContentType.JSON)
                .when()
                .get("/1/cards/" + Create_Card.card_id+"/checklists") // Correct endpoint
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the status code of Getting CheckLists is = 200")
    @Test(description = "Test that the status code of Getting CheckLists is = 200")
    public void testStatusCode() {
        // Verify the status code of the response
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test that the response time of Getting CheckLists is less than 3000ms")
    @Test(description = "Test that the response time of Getting CheckLists is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms");
    }

    @Story("Retrieve CheckLists")
    @Test(description ="Retrieve CheckLists")
    public void Validate_that_checklists_is_retrieved_successfully() {
        // Verify that lists were retrieved successfully
        String jsonResponse = response.getBody().asString();
        System.out.println("Cards Response Body: " + jsonResponse);
        // Assert that the response contains at least one list
        int checklistCount = response.jsonPath().getList("$").size(); // Count the number of items in the response array
        System.out.println("Number of Checklists: " + checklistCount);
        assertTrue(checklistCount > 0, "No checklists were retrieved from the card.");
    }
}
