package qa.guru.allure;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.link;
import static io.qameta.allure.Allure.parameter;
import static qa.guru.allure.NamedBy.css;
import static qa.guru.allure.NamedBy.named;
import static qa.guru.allure.PrivateData.*;
import static qa.guru.allure.TestData.*;


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
        $(named(byText("Sign in")).as("Перейти на страницу авторизации")).click();
        $(css("#login_field").as("Заполнить поле Login")).val(getLOGIN());
        $(css("#password").as("Заполнить поле Password")).val(getPASSWORD()).pressEnter();

        $(named(by("title", getREPOSITORY()))
                .as(String.format("Перейти в репозиторий \"%s\"", getREPOSITORY()))).click();
        $(css("[data-tab-item=issues-tab]").as("Перейти во вкладку \"Issues\"")).click();
        $(css(".d-md-block").as("Нажать кнопку \"New issue\"")).click();
        $(css("#issue_title").as("Заполнить поле Title")).val(getTITLE());
        $(css("#issue_body").as("Заполнить поле Description")).val(getDESCRIPTION());
        $(named(byText("Submit new issue")).as("Нажать кнопку \"Submit new issue\"")).click();

        setIssueId($(css("span.js-issue-title~span").as("Записать issue id")).getText());
        $(css(".js-issue-assign-self").as(String.format("Назначить issue #%s на \"%s\"",
                getIssueId(), getOWNER()))).click();
        $(css("#labels-select-menu").as(String.format("Перейти в блок label для issue #%s",
                getIssueId()))).click();
        $(css("[role=menuitemcheckbox]").as(String.format("Выбрать label \"%s\"", getLABEL()))).click();
        $(css("#labels-select-menu").as("Закрыть блок label")).click();
        closeWebDriver();

        new ApiSteps().checkCreatedIssue(getOWNER(), getREPOSITORY(), getIssueId(), getTOKEN());
    }
}