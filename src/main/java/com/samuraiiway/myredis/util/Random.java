package com.samuraiiway.myredis.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class Random {

    private Random() {}

    static final java.util.Random random = new SecureRandom();
    static final String randomString = "ABCDEFGHIJKLMNPOQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    static final List<String> roles = Arrays.asList("user", "admin", "anonymous", "supplier", "operation", "system", "service");

    public static String getRandomString(int number) {
        String str = "";
        for (int i = 0; i < number; i++) {
            str += randomString.charAt(random.nextInt(randomString.length()));
        }

        return str;
    }

    public static String getRandomRole(int i) {
        return roles.get(i % roles.size());
    }
}
