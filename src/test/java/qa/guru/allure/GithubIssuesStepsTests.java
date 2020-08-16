package qa.guru.allure;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Feature("Работа с новой Issue")
@Owner("Роман Зуев")
public class GithubIssuesStepsTests {
    private WebSteps steps = new WebSteps();
    private ApiSteps apiSteps = new ApiSteps();

    @Test
    @DisplayName("Создание Issue через WEB и проверка через API")
    @Story("Создание Issue через WEB и проверка через API")
    public void createIssueByWebAndCheckByApiTest() {
        steps.openMainPage(TestData.getURL());
        steps.goToAuthPage();
        steps.performAuthorization(PrivateData.getLOGIN(), PrivateData.getPASSWORD());

        steps.findRepositoryAndGoIssue(TestData.getREPOSITORY());
        steps.clickNewIssueButton();
        steps.fillOutFormAndCreateAnIssue(TestData.getTITLE(), TestData.getDESCRIPTION());

        steps.writeAnIssueId();
        steps.assignAnIssueToYourself();
        steps.addLabelForIssue();
        steps.closeBrowser();

        apiSteps.checkCreatedIssue(TestData.getOWNER(), TestData.getREPOSITORY(),
                TestData.getIssueId(), PrivateData.getTOKEN());
    }
}