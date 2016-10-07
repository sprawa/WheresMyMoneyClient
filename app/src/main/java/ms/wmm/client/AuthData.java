package ms.wmm.client;

/**
 * Created by Marcin on 12.08.2016.
 */
public class AuthData {

    private static String username;
    private static String password;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        AuthData.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AuthData.password = password;
    }
}
