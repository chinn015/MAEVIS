package com.user.maevis.models;

/**
 * Created by User on 11/23/2017.
 */

public class ReportModel {
    private String reportType;
    private String reportedBy;
    private String reportDescription;
    private double locationLatitude;
    private double locationLongitude;
    private String location;
    private String dateTime;

    public ReportModel() {

    }

    public ReportModel( String dateTime, String reportDescription, String location, double locationLatitude, double locationLongitude, String reportType, String reportedBy) {
        this.dateTime = dateTime;
        this.location = location;
        this.reportDescription = reportDescription;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
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

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
