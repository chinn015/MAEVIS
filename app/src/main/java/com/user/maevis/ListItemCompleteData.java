package com.user.maevis;

/**
 * Created by User on 12/14/2017.
 */

public class ListItemCompleteData {
    private String reportID;
    private String dateTime;
    private String description;
    private String imageURL;
    private String location;
    private double locationLatitude;
    private double locationLongitude;
    private String reportType;
    private String reportedBy;
    private String displayDateTime; //date sorting shit


    //CONSTRUCTORS
    public ListItemCompleteData(String reportID, String dateTime, String description, String imageURL, String location, double locationLatitude, double locationLongitude, String reportType, String reportedBy, String displayDateTime) {
        this.reportID = reportID;
        this.dateTime = dateTime;
        this.description = description;
        this.imageURL = imageURL;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
        this.displayDateTime = displayDateTime;
    }

    //GETTER SETTER
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

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

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }
}
