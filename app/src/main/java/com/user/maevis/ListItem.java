package com.user.maevis;


public class ListItem {

    private String head;
    private String desc;
    private String imageURL;
    private String dateTime;

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
}
