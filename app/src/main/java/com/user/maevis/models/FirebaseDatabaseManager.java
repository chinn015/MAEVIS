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
    private static List<UserItem> userItems;

    public FirebaseDatabaseManager() {

    }

    public static void initializeFirebaseDatabaseManager() {
        listItems = new ArrayList<>(); //List of all Reports
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
}
