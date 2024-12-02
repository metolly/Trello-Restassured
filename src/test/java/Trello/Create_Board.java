package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

@Feature("Create new Board")
@Test(priority = 1)
public class Create_Board {

    public static String APIKey = "8ba64317e64c45a0fdd01029af42fee8";
    public static String token = "ATTA6b9eb13ce99f32848f63c778397b60639fd93a22c962aab8a062248016386a9cCDEF058F";
    public static String board_id = " "; // Make it static
    public static String board_name = " ";
    private Response response; // To store the response of board creation

    @BeforeClass
    public void createBoard() {
        // Create the board once before all tests
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name", "API Testing")
                .queryParam("key", APIKey)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/boards/")
                .then()
                .log().all()
                .extract()
                .response();
        // Extract and store the board ID
        board_id = response.jsonPath().getString("id");
        board_name = response.jsonPath().getString("name");
        System.out.println("Created Board ID: " + board_id);

    }

    @Story("Test that the Status code of Create new board is = 200")
    @Test(description = "Test that the Status code of Create new board is =200")
    public void TestStatusCode() {
        // Validate the status code of the board creation
        response.then().statusCode(200);
    }

    @Story("Test That the Response time Of Creating a New Board is less than 3000ms")
    @Test(description = "Test That the Response time Of Creating a New Board is less than 3000ms")
    public void TestResponseTime() {
        // Validate the response time of the board creation
        assertTrue(response.getTime() < 3000, "Response time is greater than 3000ms");
    }

}
