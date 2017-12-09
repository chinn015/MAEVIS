package com.user.maevis;


public class ListNotif {

    private String head;
    private String dateTime;

    public ListNotif(String head, String dateTime) {
        this.head = head;
        this.dateTime = dateTime;
    }

    public String getHead() {
        return head;
    }

    public String getDateTime() {
        return dateTime;
    }
}
