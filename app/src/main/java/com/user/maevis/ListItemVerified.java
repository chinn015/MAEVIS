package com.user.maevis;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by User on 1/8/2018.
 */

public class ListItemVerified implements Comparable<ListItem> {
    private String reportID;
    private String head;
    private String dateTime;
    private String description;
    private List<String> imageList; //URLs of all images from all users who sent similar reports
    private String imageThumbnailURL;
    private String location;
    private double locationLatitude;
    private double locationLongitude;
    private List<String> mergedReportsID;
    private String reportStatus; //Active or Done
    private String reportType;
    private String reportedBy; //admin ID
    private String displayDateTime; //date sorting shit
    private String userPhoto;


    //CONSTRUCTORS
    public ListItemVerified(String reportID, String head, String dateTime, String description, List<String> imageList, String imageThumbnailURL, String location, double locationLatitude, double locationLongitude, List<String> mergedReportsID, String reportStatus, String reportType, String reportedBy, String displayDateTime, String userPhoto) {
        this.reportID = reportID;
        this.head = head;
        this.dateTime = dateTime;
        this.description = description;
        this.imageList = imageList;
        this.imageThumbnailURL = imageThumbnailURL;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.reportStatus = reportStatus;
        this.reportType = reportType;
        this.reportedBy = reportedBy;
        this.mergedReportsID = mergedReportsID;
        this.displayDateTime = displayDateTime;
        this.userPhoto = userPhoto;
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

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public String getHead() {
        return head;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public int compareTo(ListItem li) {
        if (getDateTime() == null || li.getDateTime() == null) {
            return 0;
        }

        return getDateTime().compareTo(li.getDateTime());
    }

    public static int getReportTypeImage(String reportType){
        int ret = 0;
        int[] reportIcons = {
                R.mipmap.btn_fire,
                R.mipmap.btn_flood,
                R.mipmap.btn_accident
        };

        switch(reportType){
            case "Fire":
                ret = reportIcons[0];
                break;

            case "Flood":
                ret = reportIcons[1];
                break;

            case "Vehicular Accident":
                ret = reportIcons[2];
                break;
        }

        return ret;
    }

    public static int getReportMarkerImage(String reportType){
        int retIcon = 0;
        int[] reportIcons = {
                R.mipmap.ic_marker_fire,
                R.mipmap.ic_marker_flood,
                R.mipmap.ic_marker_accident,
        };

        switch (reportType){

            case "Fire" :
                retIcon = reportIcons[0];
                break;

            case "Flood" :
                retIcon = reportIcons[1];
                break;

            case "Vehicular Accident" :
                retIcon = reportIcons[2];
                break;
        }

        return retIcon;
    }
}
