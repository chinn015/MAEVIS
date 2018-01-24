package com.user.maevis.models;

/**
 * Created by Chen on 1/23/2018.
 */

public class NotifModel {
    private String notifMessage;
    private String notifReportID;
    private String notifTitle;
    private String notifiedTo;


    public NotifModel(String notifMessage, String notifReportID, String notifTitle, String notifiedTo) {
        this.notifMessage = notifMessage;
        this.notifReportID = notifReportID;
        this.notifTitle = notifTitle;
        this.notifiedTo = notifiedTo;
    }

    public String getNotifMessage() {
        return notifMessage;
    }

    public void setNotifMessage(String notifMessage) {
        this.notifMessage = notifMessage;
    }

    public String getNotifReportID() {
        return notifReportID;
    }

    public void setNotifReportID(String notifReportID) {
        this.notifReportID = notifReportID;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public String getNotifiedTo() {
        return notifiedTo;
    }

    public void setNotifiedTo(String notifiedTo) {
        this.notifiedTo = notifiedTo;
    }
}
