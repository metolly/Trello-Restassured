package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Retrieve a specific Card")
public class Get_Card {

    @BeforeClass
    public Response GetCard() {



        if(Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }

        if (Create_List.list_id == null || Create_List.list_id.isEmpty()) {
            throw new IllegalStateException("List ID is not initialized. Please ensure Create_List is executed first.");
        }

        System.out.println("Using Card ID to get Card: " + Create_Card.card_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/cards/" + Create_Card.card_id )
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of Getting a specific Card is = 200")
    @Test(description = "Test that the status code of Getting a specific Card is = 200")
    public void TestStatusCode() {
        GetCard().then().statusCode(200);
    }

    @Story("Test that the response time of Getting a specific Card is less than 3000ms")
    @Test(description = "Test that the response time of Getting a specific Card is less than 3000ms")
    public void TestResponseTime() {
        long responseTime =GetCard().getTime();
        assert responseTime < 3000;
    }

    @Story("Test that the Card is added successfully")
    @Test(description ="Test that the Card is added successfully" )
    public void Validate_That_Card_is_Created_successfully() {
        String str1 = GetCard().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_Card.card_id);
        String str2 = GetCard().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_Card.card_name);
    }

    @Story("Test that the Card is added successfully on List")
    @Test(description ="Test that the Card is added successfully on List" )
    public void Validate_That_Card_is_Created_successfully_on_specific_list() {
        String str = GetCard().jsonPath().getString("idList");
        Assert.assertEquals(str,Create_List.list_id);
    }

    @Story("Test that the Card is added successfully on board")
    @Test(description ="Test that the Card is added successfully on board")
    public void Validate_That_Card_is_Created_successfully_on_specific_board() {
        String str = GetCard().jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_Board.board_id);
    }

}
