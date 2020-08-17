package qa.guru.allure;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.link;
import static io.qameta.allure.Allure.parameter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesListenerTests {
    @BeforeEach
    public void initSelenideListener() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @Test
    @DisplayName("Создание Issue через WEB и проверка через API")
    @Story("Создание Issue через WEB и проверка через API")
    public void createIssueByWebAndCheckByApiTest() {
        link("Test site", TestData.getURL());
        parameter("Repository", TestData.getREPOSITORY());
        parameter("Issue Title", TestData.getTITLE());
        parameter("Issue Description", TestData.getDESCRIPTION());
        parameter("Issue Owner", TestData.getOWNER());
        parameter("Issue Label", TestData.getLABEL());

        open(TestData.getURL());
        $(byText("Sign in")).click();
        $("#login_field").val(PrivateData.getLOGIN());
        $("#password").val(PrivateData.getPASSWORD()).pressEnter();

        $(by("title", TestData.getREPOSITORY())).click();
        $("[data-tab-item=issues-tab]").click();
        $(".d-md-block").click();
        $("#issue_title").val(TestData.getTITLE());
        $("#issue_body").val(TestData.getDESCRIPTION());
        $(byText("Submit new issue")).click();

        TestData.setIssueId($("span.js-issue-title~span").getText());
        $(".js-issue-assign-self").click();
        $("#labels-select-menu").click();
        $("[role=menuitemcheckbox]").click();
        $("#labels-select-menu").click();
        closeWebDriver();

        given()
                .filter(new AllureRestAssured())
                .baseUri("https://api.github.com")
                .header("Authorization", PrivateData.getTOKEN())
                .when()
                .get(String.format("/repos/%s/%s/issues/%s",
                        TestData.getOWNER(), TestData.getREPOSITORY(), TestData.getIssueId()))
                .then()
                .statusCode(200)
                .body("number", equalTo(Integer.parseInt(TestData.getIssueId())))
                .body("title", equalTo(TestData.getTITLE()))
                .body("body", equalTo(TestData.getDESCRIPTION()))
                .body("assignee.login", equalTo(TestData.getOWNER()))
                .body("labels[0].name", equalTo(TestData.getLABEL()));
    }
}