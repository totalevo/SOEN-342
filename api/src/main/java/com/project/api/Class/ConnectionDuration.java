package com.project.api.Class;
public class ConnectionDuration {
    public static final int HOURS_IN_A_DAY=24;
    public static final int MINUTES_IN_AN_HOUR=60;

    public static int getTotalMinutes(String[] departureParts, String[] arrivalParts) {
        int departureHours = Integer.parseInt(departureParts[0]);
        int departureMinutes = Integer.parseInt(departureParts[1]);
        int arrivalHours = Integer.parseInt(arrivalParts[0]);
        int arrivalMinutes = Integer.parseInt(arrivalParts[1]);
        int totalMinutes=0;
        if(arrivalHours< departureHours){
            totalMinutes= (((HOURS_IN_A_DAY+arrivalHours)*MINUTES_IN_AN_HOUR)+arrivalMinutes)-(departureHours*MINUTES_IN_AN_HOUR+departureMinutes);
        }
        else{
            totalMinutes= (arrivalHours*MINUTES_IN_AN_HOUR+arrivalMinutes) - (departureHours*MINUTES_IN_AN_HOUR+departureMinutes);
        }
        return totalMinutes;
    }
}
