package qa.guru.allure;

public class PrivateData {
    private static final String LOGIN = "test";
    private static final String PASSWORD = "test";
    private static final String TOKEN = "test";

    public static String getLOGIN() {
        return LOGIN;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getTOKEN() {
        return TOKEN;
    }
}
