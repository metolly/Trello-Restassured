package Trello;

import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Delete_List {

    private Response response;

    @BeforeClass
    public void deleteList() {
        // Access the static list_id from Create_List
        String ListId = Create_List.list_id;
        String ListName = Create_List.list_name;
        Create_Board c = new Create_Board();

        // Ensure ListId and BoardId are initialized
        if (ListId == null || ListId.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using List ID for Deletion: " + ListId);

        // Step 1: Archive the list first (if it's open)
        Response archiveResponse = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // Access APIKey directly
                .queryParam("token", Create_Board.token) // Access token directly
                .queryParam("value", "true") // Archive the list
                .when()
                .put("/1/lists/" + ListId + "/closed")
                .then()
                .log().all()
                .extract()
                .response();

        // Check if the list was successfully archived
        if (archiveResponse.getStatusCode() != 200) {
            throw new IllegalStateException("Failed to archive the list: " + archiveResponse.getStatusCode());
        }

        System.out.println("List archived successfully. Now proceeding with deletion.");

        // Step 2: Perform the DELETE request to delete the list
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key", Create_Board.APIKey) // Access APIKey directly
                .queryParam("token", Create_Board.token) // Access token directly
                .contentType(ContentType.JSON)
                .when()
                .delete("/1/lists/" + ListId)
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Story("Test that the Status code of Deleting a Specific List is = 200")
    @Test(description = "Test that the Status code of Deleting a Specific List is = 200")
    public void testStatusCode() {
        // Validate the status code of the DELETE request
        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        assertEquals(statusCode, 200, "Expected status code 200 but got: " + statusCode);
    }

    @Story("Test That the Response time Of Deleting a Specific List is less than 3000ms")
    @Test(description = "Test That the Response time Of Deleting a Specific List is less than 3000ms")
    public void testResponseTime() {
        // Validate the response time of the DELETE request
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms: " + responseTime);
    }

    @Story("Test That the List Deleted Successfully")
    @Test(description = "Test That the List Deleted Successfully")
    public void Validate_That_List_is_Deleted_successfully() {

        Get_List g = new Get_List();
        Response getListResponse = g.GetList();
        // Verify that the list is deleted by checking the status code
        int getStatusCode = getListResponse.getStatusCode();
        System.out.println("Get Status Code: " + getStatusCode);

        // If list is deleted, the response should return 404 (not found)
        assertEquals(getStatusCode, 404, "Expected status code 404 but got: " + getStatusCode);
    }
}
