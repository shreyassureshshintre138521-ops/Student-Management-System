public class SessionManager {
    private static String currentUser = null;
    private static long loginTimestamp = 0;

    public static void login(String username) {
        currentUser = username;
        loginTimestamp = System.currentTimeMillis();
    }

    public static void logout() {
        currentUser = null;
        loginTimestamp = 0;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
}