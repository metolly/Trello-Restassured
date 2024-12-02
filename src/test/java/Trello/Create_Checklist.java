package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Create a new CheckList")
public class Create_Checklist {

    public static String checklist_id = " "; // Make it static
    public static String checklist_name = " ";
    private Response response;

    @BeforeClass
    public void CreateChecklist() {

        if (Create_Card.card_id == null || Create_Card.card_id.isEmpty()) {
            throw new IllegalStateException("Card ID is not initialized. Please ensure Create_Card is executed first.");
        }
        System.out.println("Using Card ID to Create new Checklist: " + Create_Card.card_id);

        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Login")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idCard", Create_Card.card_id)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/checklists")
                .then()
                .log().all()
                .extract()
                .response();
        checklist_id= response.jsonPath().getString("id");
        checklist_name = response.jsonPath().getString("name");

    }

    @Story("Test that the Status code of Creating a new CheckList is = 200")
    @Test(description = "Test that the Status code of Creating a new CheckList is = 200")
    public void TestStatusCode() {
        response.then().statusCode(200);
    }

    @Story("Test That the Response time Of Creating a New CheckList is less than 3000ms")
    @Test(description = "Test That the Response time Of Creating a New CheckList is less than 3000ms")
    public void TestResponseTime() {
        assert response.getTime()< 3000;
    }


    @Story("Test That the CheckList is Created Successfully on Card")
    @Test(description = "Test That the CheckList is Created Successfully on Card")
    public void Validate_That_checklist_is_created_successfully_on_card() {
        Assert.assertEquals(response.jsonPath().getString("idCard"),Create_Card.card_id);
    }

    @Story("Test That the CheckList is Created Successfully on Board")
    @Test(description = "Test That the CheckList is Created Successfully on Board")
    public void Validate_That_card_is_created_successfully_on_board() {
        Assert.assertEquals(response.jsonPath().getString("idBoard"),Create_Board.board_id);
    }

}
