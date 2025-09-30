package com.project.api.Class;

public class DaysBitMap {
    public static final int MONDAY = 1 << 6;    // 1000000 = 64
    public static final int TUESDAY = 1 << 5;   // 0100000 = 32
    public static final int WEDNESDAY = 1 << 4; // 0010000 = 16
    public static final int THURSDAY = 1 << 3;  // 0001000 = 8
    public static final int FRIDAY = 1 << 2;    // 0000100 = 4
    public static final int SATURDAY = 1 << 1;  // 0000010 = 2
    public static final int SUNDAY = 1 << 0;    // 0000001 = 1

    public static final int ALL_DAYS = 127; // 1111111


    public static int convertToBitmap(String daysText) {
        if (daysText == null || daysText.trim().isEmpty()) {
            return 0;
        }

        daysText = daysText.trim();

        if (daysText.equalsIgnoreCase("Daily")) {
            return ALL_DAYS;
        }

        int bitmap = 0;

        if (daysText.contains("-")) {
            String[] parts = daysText.split("-");
            if (parts.length == 2) {
                int start = getDayBit(parts[0].trim());
                int end = getDayBit(parts[1].trim());

                int startPos = Integer.numberOfTrailingZeros(start);
                int endPos = Integer.numberOfTrailingZeros(end);

                for (int i = startPos; i >= endPos; i--) {
                    bitmap |= (1 << i);
                }
            }
        }
        else if (daysText.contains(",")) {
            String[] dayList = daysText.split(",");
            for (String day : dayList) {
                bitmap |= getDayBit(day.trim());
            }
        }
        else {
            bitmap = getDayBit(daysText);
        }


        return bitmap;

    }


    public static int getDayBit(String day){
        day = day.toLowerCase();
        return switch (day) {
            case "mon", "monday" -> MONDAY;
            case "tue", "tuesday" -> TUESDAY;
            case "wed", "wednesday" -> WEDNESDAY;
            case "thu", "thursday" -> THURSDAY;
            case "fri", "friday" -> FRIDAY;
            case "sat", "saturday" -> SATURDAY;
            case "sun", "sunday" -> SUNDAY;
            default -> 0;
        };
    }
    public static boolean operatesOnDay(int bitmap, int dayBit) {
        return (bitmap & dayBit) != 0;
    }
}
