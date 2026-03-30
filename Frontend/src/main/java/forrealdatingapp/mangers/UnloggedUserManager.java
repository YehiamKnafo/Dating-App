package forrealdatingapp.mangers;

import forrealdatingapp.dtos.User;

public class UnloggedUserManager {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UnloggedUserManager.user = user;
    }
}
