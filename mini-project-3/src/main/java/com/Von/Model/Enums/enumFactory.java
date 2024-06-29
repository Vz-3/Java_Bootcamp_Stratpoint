package com.Von.Model.Enums;
import java.util.Arrays;

public class enumFactory {

    public static <T extends Enum<T>> T fromChoice(int choice, Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        if (choice >= 0 && choice < constants.length) {
            return constants[choice];
        } else {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
    }
}
