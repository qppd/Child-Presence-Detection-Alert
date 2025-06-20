package com.qppd.carmonitoring.Globals;

import com.qppd.carmonitoring.Classes.User;

public class UserGlobal {

    private static User user;
    private static String user_id;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserGlobal.user = user;
    }

    public static String getUser_id() {
        return user_id;
    }

    public static void setUser_id(String user_id) {
        UserGlobal.user_id = user_id;
    }
}
