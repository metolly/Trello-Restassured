package Trello;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
@Feature("Create CheckItem")
public class Create_CheckItem {

    public static String checkitem_id = " "; // Make it static
    public static String checkitem_name = " ";
    private Response response;

    @BeforeClass
    public void CreateCheckitem() {

        if (Create_Checklist.checklist_id == null || Create_Checklist.checklist_id.isEmpty()) {
            throw new IllegalStateException("Checklist ID is not initialized. Please ensure Create_Checklist is executed first.");
        }
        System.out.println("Using Checklist ID to Create new CheckItem: " + Create_Checklist.checklist_id);
        response = given()
                .baseUri("https://api.trello.com")
                .queryParam("name" , "Valid Logout redirection")
                .queryParam("key",Create_Board.APIKey )
                .queryParam("token",Create_Board.token )
                .queryParam("idCard", Create_Card.card_id)
                .contentType(ContentType.JSON)
                .when()
                .post("/1/checklists/" + Create_Checklist.checklist_id+"/checkItems" )
                .then()
                .log().all()
                .extract()
                .response();
        checkitem_id= response.jsonPath().getString("id");
        checkitem_name = response.jsonPath().getString("name");

    }

    @Story("Test that the Status code of Creating a new CheckItem is = 200")
    @Test(description = "Test that the Status code of Creating a new CheckItem is = 200")
    public void TestStatusCode() {
        response.then().statusCode(200);
    }

    @Story("Test That the Response time Of Creating a New CheckItem is less than 3000ms")
    @Test(description = "Test That the Response time Of Creating a New CheckItem is less than 3000ms")
    public void TestResponseTime() {
        assert response.getTime() < 3000;
    }

    @Story("Test That the CheckItem is Created Successfully on CheckList")
    @Test(description = "Test That the CheckItem is Created Successfully on CheckList")
    public void Validate_That_checkItem_is_created_successfully_on_Checklist() {
        Assert.assertEquals(response.jsonPath().getString("idChecklist"),Create_Checklist.checklist_id);
    }
}
