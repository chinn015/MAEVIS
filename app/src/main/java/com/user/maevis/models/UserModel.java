package com.user.maevis.models;

/**
 * Created by Chen on 11/21/2017.
 */

public class UserModel {

    //private String userID;
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
    private String password;
    private String userPhoto;
    private String userStatus;
    private String userType;
    private String username;

    //Constructor

    public UserModel(String address, String birthdate, double currentLat, double currentLong, String deviceToken, String email, String firstName, double homeLat, double homeLong, String lastName, String password, String userPhoto, String userStatus, String userType, String username) {
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
        this.password = password;
        this.userPhoto = userPhoto;
        this.userStatus = userStatus;
        this.userType = userType;
        this.username = username;
    }

    public UserModel(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public UserModel() {

    }

    //Getter Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserType() {return userType;}

    public void setUserType(String userType) {this.userType = userType;}

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
}
