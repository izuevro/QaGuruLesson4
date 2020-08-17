package qa.guru.allure;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class ApiSteps {
    @Step("Проверка issue через API")
    public void checkCreatedIssue(String owner, String repository,
                                  String issueId, String token) {
        given()
                .filter(new AllureRestAssured())
                .baseUri("https://api.github.com")
                .header("Authorization", token)
                .when()
                .get(String.format("/repos/%s/%s/issues/%s", owner, repository, issueId))
                .then()
                .statusCode(200)
                .body("number", equalTo(Integer.parseInt(issueId)))
                .body("title", equalTo(TestData.getTITLE()))
                .body("body", equalTo(TestData.getDESCRIPTION()))
                .body("assignee.login", equalTo(TestData.getOWNER()))
                .body("labels[0].name", equalTo(TestData.getLABEL()));
    }
}
