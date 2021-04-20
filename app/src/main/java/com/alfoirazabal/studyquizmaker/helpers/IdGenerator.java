package com.alfoirazabal.studyquizmaker.helpers;

import java.util.Random;

public class IdGenerator {

    private static char[] ALLOWED_CHARACTERS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static int DEFAULT_ID_LENGTH = 8;

    public static String generateId(int length) {
        StringBuilder id = new StringBuilder();
        Random random = new Random();
        for (int i = 0 ; i < length ; i++) {
            id.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)]);
        }
        return id.toString();
    }

    public static String generateId() {
        return generateId(DEFAULT_ID_LENGTH);
    }

}
