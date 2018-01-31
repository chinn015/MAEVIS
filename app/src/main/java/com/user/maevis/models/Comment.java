package com.user.maevis.models;

/**
 * Created by User2016 on 31/01/2018.
 */

public class Comment {

    public String uid;
    public String author;
    public String text;

    public Comment(){
        //Default constructor required for calls to Datasnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text){
        this.uid = uid;
        this.author = author;
        this.text = text;
    }

}
// [END comment_class]
