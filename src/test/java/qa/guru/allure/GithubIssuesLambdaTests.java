package qa.guru.allure;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Feature("Работа с новой Issue")
@Owner("Роман Зуев")
public class GithubIssuesLambdaTests {

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

        step("Открыть " + TestData.getURL(), () -> open(TestData.getURL()));
        step("Перейти на страницу авторизации", () -> $(byText("Sign in")).click());
        step("Выполнить авторизацию пользователем " + TestData.getOWNER(), () -> {
            $("#login_field").val(PrivateData.getLOGIN());
            $("#password").val(PrivateData.getPASSWORD()).pressEnter();
        });

        step("Перейти в репозиторий " + TestData.getREPOSITORY()
                + ", и перейти во вкладку Issues", () -> {
            $(by("title", TestData.getREPOSITORY())).click();
            $("[data-tab-item=issues-tab]").click();
        });
        step("Нажать кнопку New issue", () -> $(".d-md-block").click());
        step("Заполнить форму и создать issue", () -> {
            $("#issue_title").val(TestData.getTITLE());
            $("#issue_body").val(TestData.getDESCRIPTION());
            $(byText("Submit new issue")).click();
        });

        step("Записать issue id",
                () -> TestData.setIssueId($("span.js-issue-title~span").getText()));
        step("Назначить issue #" + TestData.getIssueId() + " на " + TestData.getOWNER(),
                () -> $(".js-issue-assign-self").click());
        step("Добавить label для issue #" + TestData.getIssueId(), () -> {
            $("#labels-select-menu").click();
            $("[role=menuitemcheckbox]").click();
            $("#labels-select-menu").click();
        });
        step("Закрыть браузер", Selenide::closeWebDriver);

        step("Проверить issue #" + TestData.getIssueId() + " через API", () -> {
            given()
                    .filter(new AllureRestAssured())
                    .baseUri("https://api.github.com")
                    .header("Authorization", PrivateData.getTOKEN())
                    .when()
                    .get("/repos/" + TestData.getOWNER() + "/" + TestData.getREPOSITORY()
                            + "/issues/" + TestData.getIssueId())
                    .then()
                    .statusCode(200)
                    .body("number", equalTo(Integer.parseInt(TestData.getIssueId())))
                    .body("title", equalTo(TestData.getTITLE()))
                    .body("body", equalTo(TestData.getDESCRIPTION()))
                    .body("assignee.login", equalTo(TestData.getOWNER()))
                    .body("labels[0].name", equalTo(TestData.getLABEL()));
        });
    }
}