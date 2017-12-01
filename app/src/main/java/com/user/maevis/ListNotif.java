package com.user.maevis;


public class ListNotif {

    private String head;
    private String imageURL;
    private String dateTime;

    public ListNotif(String head, String dateTime) {
        this.head = head;
        this.dateTime = dateTime;
//        this.imageURL = imageURL;
    }

    public String getHead() {
        return head;
    }

//    public String getImageURL() {
//        return imageURL;
//    }

    public String getDateTime() {
        return dateTime;
    }
}
