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
@Feature("Retrieve Lists")
public class Get_Lists {

    private Response response;

    @BeforeClass
    public void fetchLists() {
        // Validate if the board_id is initialized
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Board ID to fetch lists: " + Create_Board.board_id);

        // Fetch all lists on the board
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // API Key
                .queryParam("token", Create_Board.token) // API Token
                .contentType(ContentType.JSON)
                .when()
                .get("/1/boards/" + Create_Board.board_id + "/lists") // Correct endpoint
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the status code of Getting Lists is = 200")
    @Test(description = "Test that the status code of Getting Lists is = 200")
    public void testStatusCode() {
        // Verify the status code of the response
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test that the response time of Getting Lists is less than 3000ms")
    @Test(description = "Test that the response time of Getting Lists is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms");
    }

    @Story("Retrieve Lists")
    @Test(description ="Retrieve Lists")
    public void Validate_that_lists_is_retrieved_successfully() {
        // Verify that lists were retrieved successfully
        String jsonResponse = response.getBody().asString();
        System.out.println("Lists Response Body: " + jsonResponse);
        // Assert that the response contains at least one list
        int listCount = response.jsonPath().getList("$").size(); // Count the number of items in the response array
        System.out.println("Number of Lists: " + listCount);
        assertTrue(listCount > 0, "No lists were retrieved from the board.");
    }

}
