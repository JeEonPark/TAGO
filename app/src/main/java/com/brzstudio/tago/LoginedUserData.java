package com.brzstudio.tago;

public class LoginedUserData {

    private static String email;
    private static int gender;
    private static String nickname;
    private static String uid;

    public static String getEmail() {
        return email;
    }
    public static void setEmail(String s) {
        email = s;
    }

    public static int getGender() {
        return gender;
    }
    public static void setGender(int i) {
        gender = i;
    }
    public static void setLongGender(Long i) {
        gender = i.intValue();
    }

    public static String getNickname() {
        return nickname;
    }
    public static void setNickname(String s) {
        nickname = s;
    }

    public static String getUid() {
        return uid;
    }
    public static void setUid(String s) {
        uid = s;
    }


}
