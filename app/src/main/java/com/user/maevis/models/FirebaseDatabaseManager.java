package com.user.maevis.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.user.maevis.ListItem;
import com.user.maevis.UserItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/15/2017.
 */

public class FirebaseDatabaseManager {
    public static final DatabaseReference FirebaseUsers = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Users");
    public static final DatabaseReference FirebaseReports = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maevis-ecd17.firebaseio.com/Reports");

    private static List<ListItem> listItems;
    private static List<ListItem> verifiedReports;
    private static List<UserItem> userItems;

    public FirebaseDatabaseManager() {

    }

    public static void initializeFirebaseDatabaseManager() {
        listItems = new ArrayList<>(); //List of all Reports
        verifiedReports = new ArrayList<>(); //List of all verified Reports
        userItems = new ArrayList<>(); //List of all Users
    }

    //GETTER SETTER
    public static List<ListItem> getListItems() {
        return listItems;
    }

    public static void setListItems(List<ListItem> listItems) {
        FirebaseDatabaseManager.listItems = listItems;
    }

    public static List<UserItem> getUserItems() {
        return userItems;
    }

    public static void setUserItems(List<UserItem> userItems) {
        FirebaseDatabaseManager.userItems = userItems;
    }

    public static List<ListItem> getVerifiedReports() {
        return verifiedReports;
    }

    public static void setVerifiedReports(List<ListItem> verifiedReports) {
        FirebaseDatabaseManager.verifiedReports = verifiedReports;
    }

//FUNCTIONS THAT CAN BE USED GLOBALLY

    //format date from (yyyy-mm-dd hh:mm:ss A) to (hh:mm A - MMM-dd-yyyy)
    public static String formatDate(String dateToFormat) {
        String year  = dateToFormat.substring(0,4);
        String monthNum = dateToFormat.substring(5,7);
        String day = dateToFormat.substring(8,10);

        String month="";
        switch (monthNum) {
            case "01": month = "JAN"; break;
            case "02": month = "FEB"; break;
            case "03": month = "MAR"; break;
            case "04": month = "APR"; break;
            case "05": month = "MAY"; break;
            case "06": month = "JUN"; break;
            case "07": month = "JUL"; break;
            case "08": month = "AUG"; break;
            case "09": month = "SEP"; break;
            case "10": month = "OCT"; break;
            case "11": month = "NOV"; break;
            case "12": month = "DEC"; break;
        }

        String time = dateToFormat.substring(11, dateToFormat.length());
        String hour = time.substring(0, 2);
        int hr = Integer.parseInt(hour);
        if(hr > 9) {
            hour = time.substring(0, 2);
        } else {
            hour = time.substring(1, 2);
        }
        String min = time.substring(3,5);
        String period = time.substring(9, time.length());

        String formattedDate = hour+":"+min+" "+period+" - "+month+" "+day+" "+year;

        return formattedDate;
    }

    //convert object value to double for LocationLatitude and LocationLongitude
    public static double parseLongToDouble(Object valueToParse) {
        double doubleValue;

        String str = valueToParse.toString();
        //doubleValue = Double.valueOf(str).doubleValue();
        doubleValue = Double.parseDouble(str);

        return doubleValue;
    }

    //takes a User's ID and returns its Full Name
    public static String getFullName(String userID) {
        String reportedByFN="FirstName";
        String reportedByLN="LastName";
        String fullName;

        for(int x=0; x < getUserItems().size(); x++) {
            UserItem userItem = getUserItems().get(x);

            if(userItem.getUserID().equals(userID)) {
                reportedByFN = userItem.getFirstName();
                reportedByLN = userItem.getLastName();
            }
        }

        fullName = reportedByFN+" "+reportedByLN;
        return fullName;
    }
}
