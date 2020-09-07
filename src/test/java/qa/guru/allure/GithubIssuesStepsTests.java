package qa.guru.allure;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static qa.guru.allure.PrivateData.*;
import static qa.guru.allure.TestData.*;


@Feature("Работа с новой Issue в Github")
@Owner("Роман Зуев")
public class GithubIssuesStepsTests {

    private WebSteps steps = new WebSteps();
    private ApiSteps apiSteps = new ApiSteps();

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