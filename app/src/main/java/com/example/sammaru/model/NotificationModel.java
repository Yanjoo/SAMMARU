package com.example.sammaru.model;

public class NotificationModel {
    public String to;
    public Notification notification = new Notification();

    public class Notification {
        public String title;
        public String text;
    }

}
