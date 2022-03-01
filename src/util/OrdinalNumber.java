package util;

public class OrdinalNumber {

    private OrdinalNumber() {
    }

    public static String getOrdinalNo(int value) {
        int hunRem = value % 100;
        int tenRem = value % 10;
        if (hunRem - tenRem == 10) {
            return "th";
        }
        return switch (tenRem) {
            case 1 -> value + "st";
            case 2 -> value + "nd";
            case 3 -> value + "rd";
            default -> value + "th";
        };
    }

}
