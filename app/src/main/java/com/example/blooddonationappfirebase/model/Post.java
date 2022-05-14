package com.example.blooddonationappfirebase.model;

public class Post {

    private String bloodType;
    private String quantity;
    private String contactInfo;
    private String otherInfo;
    private String postId;
    private String userId;
    private String author;

    public Post() {

    }

    public Post(String bloodType, String quantity, String contactInfo, String otherInfo, String postId, String userId, String author) {
        this.bloodType = bloodType;
        this.quantity = quantity;
        this.contactInfo = contactInfo;
        this.otherInfo = otherInfo;
        this.postId = postId;
        this.userId = userId;
        this.author = author;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthor() {
        return author;
    }
}
