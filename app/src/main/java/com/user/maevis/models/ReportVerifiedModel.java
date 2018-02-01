package com.user.maevis.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 1/8/2018.
 */

public class ReportVerifiedModel {
    private String dateTime;
    private String description;
    private String imageThumbnailURL;
    private List<String> imageList;
    private String location;
    private double locationLatitude;
    private double locationLongitude;
    private String reportStatus;
    private String reportType;
    private String reportedBy;
    private List<String> mergedReportsID;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();


    //CONSTRUCTOR
    public ReportVerifiedModel(String dateTime, String description, List<String> imageList, String imageThumbnailURL, String location, double locationLatitude, double locationLongitude, List<String> mergedReportsID, String reportStatus, String reportType, String reportedBy, int starCount, Map<String, Boolean> stars) {
        this.dateTime = dateTime;
        this.description = description;
        this.imageList = imageList;
        this.imageThumbnailURL = imageThumbnailURL;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.mergedReportsID = mergedReportsID;
        this.reportStatus = reportStatus;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
        this.starCount = starCount;
        this.stars = stars;
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

    public String getImageThumbnailURL() {
        return imageThumbnailURL;
    }

    public void setImageThumbnailURL(String imageThumbnailURL) {
        this.imageThumbnailURL = imageThumbnailURL;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
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

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
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

    public List<String> getMergedReportsID() {
        return mergedReportsID;
    }

    public void setMergedReportsID(List<String> mergedReportsID) {
        this.mergedReportsID = mergedReportsID;
    }
}
