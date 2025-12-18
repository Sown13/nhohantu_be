package com.nhohantu.tcbookbe.common.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class EmailUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    /***
     * Check if the email is valid
     * @param email
     * @return
     */
    public boolean valid(String email) {
        if (Util.isNullOrEmpty(email) || email.length() < 5 || email.length() > 50) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    /***
     * Format to hide email of user
     * @param email
     * @return
     */
    public String hideEmail(String email) {
        if (Util.isNullOrEmpty(email)) {
            return null;
        }

        int position = email.indexOf("@");

        for (int i = 0; i < position - 2; i++) {
            email = email.replaceFirst(String.valueOf(email.charAt(i)), "*");
        }

        return email;
    }
}
