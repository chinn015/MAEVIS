package com.user.maevis.models;

/**
 * Created by User on 11/23/2017.
 */

public class ReportModel {

    private String dateTime;
    private String description;
    private String imageURL;
    private String location;
    private double locationLatitude;
    private double locationLongitude;
    private String reportType;
    private String reportedBy;

    public ReportModel() {

    }

    /*public ReportModel( String dateTime, String description, String imageURL, String location, double locationLatitude, double locationLongitude, String reportType, String reportedBy) {
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
        this.imageURL = imageURL;
    }*/

    //CONSTRUCTOR

    public ReportModel(String dateTime, String description, String imageURL, String location, double locationLatitude, double locationLongitude, String reportType, String reportedBy) {
        this.dateTime = dateTime;
        this.description = description;
        this.imageURL = imageURL;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
    }


    //GETTER SETTERS

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }
}
