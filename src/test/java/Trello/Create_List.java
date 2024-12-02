package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;
@Feature("Create a new List")
@Test(priority = 4)
public class Create_List {

    public static String list_id = ""; // To store the list ID
    public static String list_name = ""; // To store the list name
    public static String BID = ""; // To store the board ID
    private Response response; // To store the API response

    @BeforeClass
    public void setup() {
        // Create the list only once before all tests
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name", "Pending Testing") // Specify list name
                .queryParam("idBoard", Create_Board.board_id) // Specify the board ID
                .queryParam("key", Create_Board.APIKey) // API key
                .queryParam("token", Create_Board.token) // API token
                .contentType(ContentType.JSON)
                .when()
                .post("/1/lists")
                .then()
                .log().all()
                .extract()
                .response();

        // Extract and store values for reuse
        list_id = response.jsonPath().getString("id");
        list_name = response.jsonPath().getString("name");
        BID = response.jsonPath().getString("idBoard");

        System.out.println("Created List ID: " + list_id);
    }

    @Story("Test that the Status code of Creating a new List is = 200")
    @Test(description = "Test that the Status code of Creating a new List is = 200")
    public void TestStatusCode() {
        // Validate the status code of the list creation
        response.then().statusCode(200);
    }

    @Story("Test That the Response time Of Creating a New List is less than 3000ms")
    @Test(description = "Test That the Response time Of Creating a New List is less than 3000ms")
    public void TestResponseTime() {
        // Validate the response time of the list creation
        long responseTime = response.getTime();
        System.out.println("Response Time: " + responseTime + "ms");
        assertTrue(responseTime < 3000, "Response time exceeded 3000ms");
    }

    @Story("Test That the List is Created Successfully on Board")
    @Test(description = "Test That the List is Created Successfully on Board")
    public void Validate_That_List_Is_Created_Successfully_On_Board() {
        // Validate the list was created on the correct board
        String boardIdFromResponse = response.jsonPath().getString("idBoard");
        System.out.println("Board ID from Response: " + boardIdFromResponse);
        Assert.assertEquals(boardIdFromResponse, Create_Board.board_id, "List is not created on the expected board.");
    }
}
