package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Admin {
    private boolean loggedIn = false;
    private String username;
    private List<String> activityLog = new ArrayList<>();

    private static final Map<String, String> CREDENTIALS = new HashMap<>() {{
        put("admin", "admin123");
    }};

    public boolean login(String user, String pass) {
        if (CREDENTIALS.containsKey(user) && CREDENTIALS.get(user).equals(pass)) {
            username = user;
            loggedIn = true;
            log("Admin " + user + " logged in.");
            return true;
        }
        return false;
    }

    public void logout() {
        if (loggedIn) {
            log("Admin " + username + " logged out.");
            loggedIn = false;
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void log(String entry) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        activityLog.add("[" + time + "] " + entry);
    }

    public List<String> getLog() {
        return activityLog;
    }
}
