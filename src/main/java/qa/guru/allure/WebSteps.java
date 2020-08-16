package qa.guru.allure;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.link;
import static io.qameta.allure.Allure.parameter;

@Feature("Работа с новой Issue")
@Owner("Роман Зуев")
public class WebSteps {

    @Step("Открыть главную страницу")
    public void openMainPage(String url) {
        link("Test site", url);
        open(url);
    }

    @Step("Перейти на страницу авторизации")
    public void goToAuthPage() {
        $(byText("Sign in")).click();
    }

    @Step("Выполнить авторизацию")
    public void performAuthorization(String login, String password) {
        $("#login_field").val(login);
        $("#password").val(password).pressEnter();
    }

    @Step("Найти репозиторий и перейти во вкладку Issues")
    public void findRepositoryAndGoIssue(String repository) {
        parameter("Repository", repository);

        $(by("title", repository)).click();
        $("[data-tab-item=issues-tab]").click();
    }

    @Step("Нажать кнопку New issue")
    public void clickNewIssueButton() {
        $(".d-md-block").click();
    }

    @Step("Заполнить форму и создать issue")
    public void fillOutFormAndCreateAnIssue(String title, String description) {
        parameter("Issue Title", title);
        parameter("Issue Description", description);

        $("#issue_title").val(title);
        $("#issue_body").val(description);
        $(byText("Submit new issue")).click();
    }

    @Step("Записать issue id")
    public void writeAnIssueId() {
        TestData.setIssueId($("span.js-issue-title~span").getText());
    }

    @Step("Назначить issue на себя")
    public void assignAnIssueToYourself() {
        parameter("Issue Owner", TestData.getOWNER());

        $(".js-issue-assign-self").click();
    }

    @Step("Добавить label для issue")
    public void addLabelForIssue() {
        parameter("Issue Label", TestData.getLABEL());

        $("#labels-select-menu").click();
        $("[role=menuitemcheckbox]").click();
        $("#labels-select-menu").click();
    }

    @Step("Закрыть браузер")
    public void closeBrowser() {
        closeWebDriver();
    }
}
