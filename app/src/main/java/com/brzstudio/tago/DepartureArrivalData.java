package com.brzstudio.tago;

public class DepartureArrivalData {
    private static Boolean doneDeparture = false;
    private static String departure;
    private static int departureX;
    private static int departureY;

    private static Boolean doneArrival = false;
    private static String arrival;
    private static int arrivalX;
    private static int arrivalY;

    //Departure Get Set
    public static Boolean getDoneDeparture() {
        return doneDeparture;
    }
    public static void setDoneDeparture(Boolean b) {
        doneDeparture = b;
    }

    public static String getDeparture() {
        return departure;
    }
    public static void setDeparture(String s) {
        departure = s;
    }

    public static int getDepartureX() {
        return departureX;
    }
    public static void setDepartureX(int i) {
        departureX = i;
    }

    public static int getDepartureY() {
        return departureY;
    }
    public static void setDepartureY(int i) {
        departureY = i;
    }

    //Arrival Get Set
    public static Boolean getDoneArrival() {
        return doneArrival;
    }
    public static void setDoneArrival(Boolean b) {
        doneArrival = b;
    }

    public static String getArrival() {
        return arrival;
    }
    public static void setArrival(String s) {
        arrival = s;
    }

    public static int getArrivalX() {
        return arrivalX;
    }
    public static void setArrivalX(int i) {
        arrivalX = i;
    }

    public static int getArrivalY() {
        return arrivalY;
    }
    public static void setArrivalY(int i) {
        arrivalY = i;
    }
}