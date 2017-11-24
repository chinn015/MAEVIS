package com.user.maevis.models;

/**
 * Created by User on 11/23/2017.
 */

public class ReportModel {
    private String reportType;
    private String reportedBy;
    private String reportDescription;
    private String locationLatitude;
    private String locationLongitude;
    private String location;
    private String dateTime;

    public ReportModel() {

    }

    public ReportModel(String reportType, String reportedBy, String reportDescription, String locationLatitude, String locationLongitude, String location, String dateTime) {
        this.reportType = reportType;
        this.reportedBy = reportedBy;
        this.reportDescription = reportDescription;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.location = location;
        this.dateTime = dateTime;
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

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
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
