package com.user.maevis;


import java.util.Date;

public class ListItem implements Comparable<ListItem> {

    private String head;
    private String desc;
    private String imageURL;
    private String dateTime;
    private String displayDateTime; //date sorting shit

    /*public ListItem(String head, String desc, String imageURL) {
        this.head = head;
        this.desc = desc;
        this.imageURL = imageURL;
    }*/

    public ListItem(String head, String desc, String dateTime, String imageURL) {
        this.head = head;
        this.desc = desc;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
    }

    public ListItem(String head, String desc, String dateTime, String imageURL, String displayDateTime) {
        this.head = head;
        this.desc = desc;
        this.dateTime = dateTime;
        this.imageURL = imageURL;
        this.displayDateTime = displayDateTime;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDateTime() {
        return dateTime;
    }


    //date sorting shit
    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String datetime) {
        this.displayDateTime = datetime;
    }

    public int compareTo(ListItem li) {
        if (getDateTime() == null || li.getDateTime() == null) {
            return 0;
        }

        return getDateTime().compareTo(li.getDateTime());
    }
}
