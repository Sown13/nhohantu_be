package com.nhohantu.tcbookbe.common.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PhoneUtil {
    /***
     * Check if the phone number is a Vietnam number
     * @param phoneNumber
     * @return
     */
    private boolean isVietnamPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(84|0|)" +
                "(32|33|34|35|36|37|38|39" +
                "|56|58" +
                "|70|76|77|78|79" +
                "|81|82|83|84|85|86|88|89" +
                "|90|91|92|93|94|96|97|98)[0-9]{7}\\b");
        return pattern.matcher(phoneNumber).matches();
    }
}
