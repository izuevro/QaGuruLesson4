package qa.guru.allure;

public class TestData {
    private static final String URL = "https://github.com/",
                                TITLE = "Test Issue",
                                DESCRIPTION = "Test Issue description",
                                OWNER = "",
                                REPOSITORY = "QaGuruLesson4",
                                LABEL = "bug";
    private static String issueId;

    public static String getURL() {
        return URL;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public static String getOWNER() {
        return OWNER;
    }

    public static String getREPOSITORY() {
        return REPOSITORY;
    }

    public static String getLABEL() {
        return LABEL;
    }

    public static String getIssueId() {
        return issueId;
    }

    public static void setIssueId(String issueId) {
        TestData.issueId = issueId.replaceAll("#", "");
    }

}
