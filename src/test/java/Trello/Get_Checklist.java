package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Retrieve a specific CheckList")
public class Get_Checklist {


    @BeforeClass
    public Response GetChecklist() {

        if(Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }


        System.out.println("Using Checklist ID to get Checklist: " + Create_Checklist.checklist_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Scenario")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/checklists/" + Create_Checklist.checklist_id )
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of Getting a specific CheckList is = 200")
    @Test(description = "Test that the status code of Getting a specific CheckList is = 200")
    public void TestStatusCode() {
        GetChecklist().then().statusCode(200);
    }

    @Story("Test that the response time of Getting a specific CheckList is less than 3000ms")
    @Test(description = "Test that the response time of Getting a specific CheckList is less than 3000ms")
    public void TestResponseTime() {
        assert GetChecklist().getTime() < 3000;
    }

    @Story("Test that the CheckList is added successfully")
    @Test(description ="Test that the CheckList is added successfully" )
    public void Validate_That_Checklist_is_Created_successfully() {
        String str1 = GetChecklist().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_Checklist.checklist_id);
        String str2 = GetChecklist().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_Checklist.checklist_name);
    }

    @Story("Test that the CheckList is added successfully on card")
    @Test(description ="Test that the CheckList is added successfully on card")
    public void Validate_That_CheckList_is_Created_successfully_on_specific_Card() {
        String str = GetChecklist().jsonPath().getString("idCard");
        Assert.assertEquals(str,Create_Card.card_id);
    }

    @Story("Test that the CheckList is added successfully on board")
    @Test(description ="Test that the CheckList is added successfully on board")
    public void Validate_That_CheckList_is_Created_successfully_on_specific_board() {
        String str =GetChecklist().jsonPath().getString("idBoard");
        Assert.assertEquals(str,Create_Board.board_id);
    }

    @Story("Test that the CheckList is added successfully on List")
    @Test(description ="Test that the CheckList is added successfully on List")
    public void Validate_That_checklist_is_created_successfully_on_specific_list()
    {
        Get_Card card = new Get_Card();
        Response r =   card.GetCard();
        String str = r.jsonPath().getString("idChecklists[0]");
        Assert.assertEquals(str,Create_Checklist.checklist_id);

    }
}
