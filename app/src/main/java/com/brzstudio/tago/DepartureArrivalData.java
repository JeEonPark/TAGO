package com.brzstudio.tago;

public class DepartureArrivalData {
    private static Boolean doneDeparture = false;
    private static String departure;
    private static double departureX;
    private static double departureY;

    private static Boolean doneArrival = false;
    private static String arrival;
    private static double arrivalX;
    private static double arrivalY;

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

    public static double getDepartureX() {
        return departureX;
    }
    public static void setDepartureX(double i) {
        departureX = i;
    }

    public static double getDepartureY() {
        return departureY;
    }
    public static void setDepartureY(double i) {
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

    public static double getArrivalX() {
        return arrivalX;
    }
    public static void setArrivalX(double i) {
        arrivalX = i;
    }

    public static double getArrivalY() {
        return arrivalY;
    }
    public static void setArrivalY(double i) {
        arrivalY = i;
    }
}