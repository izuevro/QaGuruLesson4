package qa.guru.allure;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.link;
import static io.qameta.allure.Allure.parameter;
import static qa.guru.allure.NamedBy.css;
import static qa.guru.allure.NamedBy.named;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesNamedByTests {
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
        $(named(byText("Sign in")).as("Перейти на страницу авторизации")).click();
        $(css("#login_field").as("Заполнить поле Login")).val(PrivateData.getLOGIN());
        $(css("#password").as("Заполнить поле Password")).val(PrivateData.getPASSWORD()).pressEnter();

        $(named(by("title", TestData.getREPOSITORY()))
                .as(String.format("Перейти в репозиторий \"%s\"", TestData.getREPOSITORY()))).click();
        $(css("[data-tab-item=issues-tab]").as("Перейти во вкладку \"Issues\"")).click();
        $(css(".d-md-block").as("Нажать кнопку \"New issue\"")).click();
        $(css("#issue_title").as("Заполнить поле Title")).val(TestData.getTITLE());
        $(css("#issue_body").as("Заполнить поле Description")).val(TestData.getDESCRIPTION());
        $(named(byText("Submit new issue")).as("Нажать кнопку \"Submit new issue\"")).click();

        TestData.setIssueId($(css("span.js-issue-title~span").as("Записать issue id")).getText());
        $(css(".js-issue-assign-self").as(String.format("Назначить issue #%s на \"%s\"",
                TestData.getIssueId(), TestData.getOWNER()))).click();
        $(css("#labels-select-menu").as(String.format("Перейти в блок label для issue #%s",
                TestData.getIssueId()))).click();
        $(css("[role=menuitemcheckbox]").as(String.format("Выбрать label \"%s\"", TestData.getLABEL()))).click();
        $(css("#labels-select-menu").as("Закрыть блок label")).click();
        closeWebDriver();

        new ApiSteps().checkCreatedIssue(TestData.getOWNER(), TestData.getREPOSITORY(),
                TestData.getIssueId(), PrivateData.getTOKEN());
    }
}