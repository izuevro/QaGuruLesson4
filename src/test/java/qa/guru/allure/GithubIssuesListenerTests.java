package qa.guru.allure;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.link;
import static io.qameta.allure.Allure.parameter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static qa.guru.allure.AttachmentsHelper.*;
import static qa.guru.allure.PrivateData.*;
import static qa.guru.allure.TestData.*;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesListenerTests {

    public static String getBrowserConsoleLogs() {
        return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
    }

    @BeforeEach
    public void initSelenideListener() {
        Configuration.headless = true;
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @AfterEach
    public void afterEach() {
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Console logs", getBrowserConsoleLogs());
        Selenide.closeWebDriver();
    }

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

        open(getURL());
        $(byText("Sign in")).click();
        $("#login_field").val(getLOGIN());
        $("#password").val(getPASSWORD()).pressEnter();

        $(by("title", getREPOSITORY())).click();
        $("[data-tab-item=issues-tab]").click();
        $(".d-md-block").click();
        $("#issue_title").val(getTITLE());
        $("#issue_body").val(getDESCRIPTION());
        $(byText("Submit new issue")).click();

        setIssueId($("span.js-issue-title~span").getText());
        $(".js-issue-assign-self").click();
        $("#labels-select-menu").click();
        $("[role=menuitemcheckbox]").click();
        $("#labels-select-menu").click();
        closeWebDriver();

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
    }
}