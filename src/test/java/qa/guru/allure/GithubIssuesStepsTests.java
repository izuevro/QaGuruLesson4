package qa.guru.allure;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import static org.openqa.selenium.logging.LogType.BROWSER;
import static qa.guru.allure.AttachmentsHelper.*;
import static qa.guru.allure.PrivateData.*;
import static qa.guru.allure.TestData.*;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesStepsTests {

    private WebSteps steps = new WebSteps();
    private ApiSteps apiSteps = new ApiSteps();

    public static String getBrowserConsoleLogs() {
        return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
    }

    @BeforeEach
    public void beforeEach() {
        Configuration.headless = true;
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
        steps.openMainPage(getURL());
        steps.goToAuthPage();
        steps.performAuthorization(getLOGIN(), getPASSWORD());

        steps.findRepositoryAndGoIssue(getREPOSITORY());
        steps.clickNewIssueButton();
        steps.fillOutFormAndCreateAnIssue(getTITLE(), getDESCRIPTION());

        steps.writeAnIssueId();
        steps.assignAnIssueToYourself(getOWNER());
        steps.addLabelForIssue(getLABEL());
        steps.closeBrowser();

        apiSteps.checkCreatedIssue(getOWNER(), getREPOSITORY(), getIssueId(), getTOKEN());
    }
}