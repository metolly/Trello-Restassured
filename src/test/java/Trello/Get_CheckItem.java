package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Retrieve a specific CheckItem")
public class Get_CheckItem {


    @BeforeClass
    public Response GetCheckItem() {

        if(Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }

        if(Create_CheckItem.checkitem_id == null || Create_CheckItem.checkitem_id.isEmpty()) {
            throw new IllegalStateException("CheckItem ID is not initialized. Please ensure Create_CheckItem is executed first.");
        }


        System.out.println("Using Checklist ID to get CheckItems: " + Create_Checklist.checklist_id);

        Response response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Scenario")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .contentType(ContentType.JSON)
                .when()
                .get("/1/checklists/" + Create_Checklist.checklist_id+"/checkItems/"+ Create_CheckItem.checkitem_id)
                .then()
                .log().all()
                .extract()
                .response();

        return response;
    }

    @Story("Test that the status code of Getting a specific CheckItem is = 200")
    @Test(description = "Test that the status code of Getting a specific CheckItem is = 200")
    public void TestStatusCode() {
        GetCheckItem().then().statusCode(200);
    }

    @Story("Test that the response time of Getting a specific CheckItem is less than 3000ms")
    @Test(description = "Test that the response time of Getting a specific CheckItem is less than 3000ms")
    public void TestResponseTime() {
        long responseTime =GetCheckItem().getTime();
        assert responseTime < 3000;
    }

    @Story("Test that the CheckItem is added successfully")
    @Test(description ="Test that the CheckItem is added successfully" )
    public void Validate_That_CheckItem_is_Created_successfully() {
        String str1 = GetCheckItem().jsonPath().getString("id");
        Assert.assertEquals(str1, Create_CheckItem.checkitem_id);
        String str2 = GetCheckItem().jsonPath().getString("name");
        Assert.assertEquals(str2, Create_CheckItem.checkitem_name);
    }

    @Story("Test that the CheckItem is added successfully on CheckList")
    @Test(description ="Test that the CheckItem is added successfully on CheckList")
    public void Validate_That_CheckItem_is_Created_successfully_on_specific_Checklist() {
        String str = GetCheckItem().jsonPath().getString("idChecklist");
        Assert.assertEquals(str,Create_Checklist.checklist_id);
    }


    @Story("Test that the CheckItem is added successfully on card")
    @Test(description ="Test that the CheckItem is added successfully on card")
    public void Validate_That_checkItem_is_created_successfully_on_specific_Card()
    {
        Get_Card card = new Get_Card();
        Response r =   card.GetCard();
        String str = GetCheckItem().jsonPath().getString("idChecklist");
        String str1 = r.jsonPath().getString("idChecklists[0]");
        Assert.assertEquals(str,str1);

    }

}
