package com.user.maevis.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2/15/2018.
 */

public class MergedReportsModel {
    private String userName;
    private String description;
    private String dateTime;
    private String userPhoto;
    private String reportPhoto;
    private String reportType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getReportPhoto() {
        return reportPhoto;
    }

    public void setReportPhoto(String reportPhoto) {
        this.reportPhoto = reportPhoto;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
