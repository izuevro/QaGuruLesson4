package qa.guru.allure;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static qa.guru.allure.PrivateData.*;
import static qa.guru.allure.TestData.*;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesLambdaTests {

    @Test
    @Tag("slow")
    @DisplayName("Создание Issue через WEB и проверка через API")
    @Story("Создание Issue через WEB и проверка через API")
    public void createIssueByWebAndCheckByApiTest() {
        link("Test site", getURL());
        parameter("Repository", getREPOSITORY());
        parameter("Issue Title", getTITLE());
        parameter("Issue Description", getDESCRIPTION());
        parameter("Issue Owner", getOWNER());
        parameter("Issue Label", getLABEL());

        step(String.format("Открыть сайт \"%s\"", getURL()), () -> open(getURL()));
        step("Перейти на страницу авторизации", () -> $(byText("Sign in")).click());
        step(String.format("Выполнить авторизацию пользователем \"%s\"", getOWNER()), () -> {
            $("#login_field").val(getLOGIN());
            $("#password").val(getPASSWORD()).pressEnter();
        });

        step(String.format("Перейти в репозиторий \"%s\", и перейти во вкладку \"Issues\"",
                getREPOSITORY()), () -> {
            $(by("title", getREPOSITORY())).click();
            $("[data-tab-item=issues-tab]").click();
        });
        step("Нажать кнопку \"New issue\"", () -> $(".d-md-block").click());
        step("Заполнить форму и нажать кнопку \"Submit new issue\"", () -> {
            $("#issue_title").val(getTITLE());
            $("#issue_body").val(getDESCRIPTION());
            $(byText("Submit new issue")).click();
        });

        step("Записать issue id",
                () -> setIssueId($("span.js-issue-title~span").getText()));
        step(String.format("Назначить issue #%s на \"%s\"", getIssueId(), getOWNER()),
                () -> $(".js-issue-assign-self").click());
        step(String.format("Добавить label \"%s\" для issue #%s", getLABEL(), getIssueId()), () -> {
            $("#labels-select-menu").click();
            $("[role=menuitemcheckbox]").click();
            $("#labels-select-menu").click();
        });
        step("Закрыть браузер", Selenide::closeWebDriver);

        step(String.format("Проверить issue #%s через API", getIssueId()), () -> {
            given()
                    .filter(new AllureRestAssured())
                    .baseUri("https://api.github.com")
                    .header("Authorization", getTOKEN())
            .when()
                    .get(String.format("/repos/%s/%s/issues/%s",
                            getOWNER(), getREPOSITORY(), getIssueId()))
            .then()
                    .statusCode(200)
                    .body("number", equalTo(Integer.parseInt(getIssueId())))
                    .body("title", equalTo(getTITLE()))
                    .body("body", equalTo(getDESCRIPTION()))
                    .body("assignee.login", equalTo(getOWNER()))
                    .body("labels[0].name", equalTo(getLABEL()));
        });
    }
}