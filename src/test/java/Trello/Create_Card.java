package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Create a new card")
public class Create_Card {

    public static String card_id = " "; // Make it static
    public static String card_name = " ";
    private Response response;

    @BeforeClass
    public void CreateCard() {
        String li = Create_List.list_id;

        if (Create_List.list_id == null || Create_List.list_id.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }
        if (Create_Board.board_id == null || Create_Board.board_id.isEmpty()) {
            throw new IllegalStateException("Board ID is not initialized. Please ensure Create_Board is executed first.");
        }

        System.out.println("Using Card ID to Create new card: " + li);


        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Login")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idList", Create_List.list_id)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/cards")
                .then()
                .log().all()
                .extract()
                .response();
        card_id = response.jsonPath().getString("id");
        card_name = response.jsonPath().getString("name");

    }

    @Story("Test that the Status code of Creating a new Card is = 200")
    @Test(description = "Test that the Status code of Creating a new Card is = 200")
    public void TestStatusCode() {
       response.then().statusCode(200);
    }

    @Story("Test That the Response time Of Creating a New Card is less than 3000ms")
    @Test(description = "Test That the Response time Of Creating a New Card is less than 3000ms")
    public void TestResponseTime() {
        assert response.getTime() < 3000;
    }


    @Story("Test That The Card Created Successfully on a board")
    @Test(description = "Test That The Card Created Successfully on a board")
    public void Validate_That_card_is_created_successfully_on_board()
    {
        Assert.assertEquals(response.jsonPath().getString("idBoard"),Create_Board.board_id);
    }

    @Story("Test That The Card Created Successfully on a list")
    @Test(description = "Test That The Card Created Successfully on a list")
    public void Validate_That_card_is_created_successfully_on_list()
    {
        Assert.assertEquals(response.jsonPath().getString("idList"),Create_List.list_id);
    }


}
