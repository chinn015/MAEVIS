package com.user.maevis;

/**
 * Created by User on 12/15/2017.
 */

public class UserItem {
    private String userID;
    private String address;
    private String birthdate;
    private double currentLat;
    private double currentLong;
    private String deviceToken;
    private String email;
    private String firstName;
    private double homeLat;
    private double homeLong;
    private String lastName;
    private String userPhoto;
    private String userStatus;
    private String userType;
    private String username;

    public UserItem(String userID, String address, String birthdate, double currentLat, double currentLong, String deviceToken, String email, String firstName, double homeLat, double homeLong, String lastName, String userPhoto, String userStatus, String userType, String username) {
        this.userID = userID;
        this.address = address;
        this.birthdate = birthdate;
        this.currentLat = currentLat;
        this.currentLong = currentLong;
        this.deviceToken = deviceToken;
        this.email = email;
        this.firstName = firstName;
        this.homeLat = homeLat;
        this.homeLong = homeLong;
        this.lastName = lastName;
        this.userPhoto = userPhoto;
        this.userStatus = userStatus;
        this.userType = userType;
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public double getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(double currentLong) {
        this.currentLong = currentLong;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public double getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(double homeLat) {
        this.homeLat = homeLat;
    }

    public double getHomeLong() {
        return homeLong;
    }

    public void setHomeLong(double homeLong) {
        this.homeLong = homeLong;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
