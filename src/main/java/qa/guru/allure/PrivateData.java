package qa.guru.allure;

public class PrivateData {

    private static final String LOGIN = System.getProperty("login"),
            PASSWORD = System.getProperty("password"),
            TOKEN = System.getProperty("token");

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
