package recipeApp;

import java.io.IOException;

public class Numeric {

    public static boolean checkNumeric(String number) {

        boolean parsable = true;

        try {
            Double.parseDouble(number);
        } catch (NumberFormatException n) {
            parsable = false;
        }

        return parsable;
    }

    public static boolean checkNumeric(String number, double lowerBound, double upperBound) {

        boolean parsable = true;
        double parsedNumber;

        try {
            parsedNumber = Double.parseDouble(number);

            if(!(parsedNumber <= upperBound && parsedNumber >= lowerBound)) {
                parsable = false;
            }
        } catch (NumberFormatException n) {
            parsable = false;
        }

        return parsable;
    }

    public static boolean checkNumericInt(String number, double lowerBound, double upperBound) {

        boolean parsable = true;
        double parsedNumber;

        try {
            parsedNumber = Double.parseDouble(number);

            if(!(parsedNumber <= upperBound && parsedNumber >= lowerBound)) {
                parsable = false;
            }

            if (!(((int) parsedNumber) == parsedNumber)) {
                parsable = false;
            }
        } catch (NumberFormatException n) {
            parsable = false;
        }


        return parsable;
    }


    public static boolean checkNumericLower(String number, double lowerBound) {

        boolean parsable = true;
        double parsedNumber;

        try {
            parsedNumber = Double.parseDouble(number);

            if(!(parsedNumber >= lowerBound)) {
                parsable = false;
            }
        } catch (NumberFormatException n) {
            parsable = false;
        }

        return parsable;
    }

    public static boolean checkNumericUpper(String number, double upperBound) {

        boolean parsable = true;
        double parsedNumber;

        try {
            parsedNumber = Double.parseDouble(number);

            if(!(parsedNumber <= upperBound)) {
                parsable = false;
            }
        } catch (NumberFormatException n) {
            parsable = false;
        }

        return parsable;
    }



}
