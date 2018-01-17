package com.user.maevis.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

/**
 * Created by User on 11/23/2017.
 */

public class SessionManager {

    private static SharedPreferences sessionPreferences;
    private static SharedPreferences.Editor sessionEditor;
    private static Context sessionContext;
    private static int PRIVATE_MODE = 0;

    private static FirebaseAuth firebaseAuth;
    //private FirebaseAuth.AuthStateListener authStateListener;

    public static final String PREF_NAME ="MAEVIS SESSION";
    public static final String KEY_USERID = "SESSION_USERID";
    public static final String KEY_USERNAME = "SESSION_USERNAME";
    //public static final String KEY_PASSWORD = "SESSION_PASSWORD";
    public static final String KEY_EMAIL = "SESSION_EMAIL";
    public static final String KEY_FIRSTNAME = "SESSION_FIRSTNAME";
    public static final String KEY_LASTNAME = "SESSION_LASTNAME";
    public static final String KEY_BIRTHDATE = "SESSION_BIRTHDATE";
    public static final String KEY_ADDRESS = "SESSION_ADDRESS";
    public static final String KEY_USERSTATUS = "SESSION_USERSTATUS";
    public static final String KEY_SESSIONSTATUS = "SESSION_SESSIONSTATUS";
    public static final String KEY_USERTYPE = "SESSION_USERTYPE";
    public static final String KEY_DEVICE_TOKEN = "SESSION_DEVICE_TOKEN";

    //CONSTRUCTOR
    public SessionManager(Context context) {
        this.sessionContext = context;
        sessionPreferences = sessionContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        sessionEditor = sessionPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static void createLoginSession(String userID, String username, String email, String firstName, String lastName, String birthdate, String address, String userStatus, String userType) {
        sessionEditor.putString(KEY_USERID, userID);
        sessionEditor.putString(KEY_USERNAME, username);
        sessionEditor.putString(KEY_EMAIL, email);
        sessionEditor.putString(KEY_FIRSTNAME, firstName);
        sessionEditor.putString(KEY_LASTNAME, lastName);
        sessionEditor.putString(KEY_BIRTHDATE, birthdate);
        sessionEditor.putString(KEY_ADDRESS, address);
        sessionEditor.putString(KEY_USERSTATUS, userStatus);
        sessionEditor.putBoolean(KEY_SESSIONSTATUS, true);
        sessionEditor.putString(KEY_USERTYPE, userType);
        //sessionEditor.putString(KEY_DEVICE_TOKEN, deviceToken);

        sessionEditor.commit();
    }

    public static HashMap<String, String> getUserSessionDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERID, sessionPreferences.getString(KEY_USERID, null));
        user.put(KEY_USERNAME, sessionPreferences.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, sessionPreferences.getString(KEY_EMAIL, null));
        user.put(KEY_FIRSTNAME, sessionPreferences.getString(KEY_FIRSTNAME, null));
        user.put(KEY_LASTNAME, sessionPreferences.getString(KEY_LASTNAME, null));
        user.put(KEY_BIRTHDATE, sessionPreferences.getString(KEY_BIRTHDATE, null));
        user.put(KEY_USERTYPE, sessionPreferences.getString(KEY_USERTYPE, null));
        user.put(KEY_ADDRESS, sessionPreferences.getString(KEY_ADDRESS, null));
        //user.put(KEY_DEVICE_TOKEN, sessionPreferences.getString(KEY_DEVICE_TOKEN, null));

        return user;
    }

    public static boolean isLoggedIn() {

        return sessionPreferences.getBoolean(KEY_SESSIONSTATUS, false);
    }

    public static void clearSession() {
        sessionEditor.clear();
        sessionEditor.commit();
    }

    public static String getUserID() {
        return sessionPreferences.getString(KEY_USERID, null);
    }

    public static String getUsername() {
        return sessionPreferences.getString(KEY_USERNAME, null);
    }

    public static String getFirstName() {
        return sessionPreferences.getString(KEY_FIRSTNAME, null);
    }

    public static String getLastName() {
        return sessionPreferences.getString(KEY_LASTNAME, null);
    }

    public static String getEmail() {
        return sessionPreferences.getString(KEY_EMAIL, null);
    }

    public static String getBirthdate() {
        return sessionPreferences.getString(KEY_BIRTHDATE, null);
    }

    public static String getAddress() {
        return sessionPreferences.getString(KEY_ADDRESS, null);
    }

    public static String getUserStatus() {
        return sessionPreferences.getString(KEY_USERSTATUS, null);
    }

    public static String getUserType() {
        return sessionPreferences.getString(KEY_USERTYPE, null);
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        SessionManager.firebaseAuth = firebaseAuth;
    }

    public static String getKeyDeviceToken() {
        return sessionPreferences.getString(KEY_DEVICE_TOKEN, null);
    }
}
