package com.nhohantu.tcbookbe.common.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class Util {
    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

//    public String generateRedisKey(String... arg) {
//        return String.join(RedisConstant.SEPARATOR, arg);
//    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

//    public String generateFileDirectory(String... arg) {
//        return String.join(FileConstant.DIRECTORY_DIVIDE, arg);
//    }

    public String generateAuthorizationCode() {
        int length = 60;
        byte[] randomBytes = new byte[length];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);

        String authorizationCode = Base64.getEncoder().encodeToString(randomBytes);

        return authorizationCode;
    }

    public String removeEmojiFromString(String input) {
        // Step 1: Remove sequences starting with '\' followed by 5 characters
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '\\' && i + 5 < input.length()) {
                // Skip the backslash and the next 5 characters
                i += 6;
            } else {
                // Append the character if it's not part of the "\ + 5 characters"
                result.append(input.charAt(i));
                i++;
            }
        }

        // Step 2: Remove emojis from the resulting string
        String stringWithoutSequences = result.toString();
        String emojiPattern = "[\\p{So}\\p{C}]"; // Regex pattern for emojis
        return stringWithoutSequences.replaceAll(emojiPattern, "");
    }

    public List<String> removeDuplicateItemInList(List<String> list) {
        Set<String> uniqueSet = new HashSet<>(list);

        return uniqueSet.stream().toList();
    }
}
