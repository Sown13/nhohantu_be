package com.nhohantu.tcbookbe.common.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class UsernameUtil {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9._-]{6,20}$");

    /***
     * Check if the username is valid
     * @param username
     * @return
     */
    public boolean valid(String username) {
        return !Util.isNullOrEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }
}
