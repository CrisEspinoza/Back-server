package com.example.Entities;

public class Tweet {
    private String name;
    private String text;
    private String followers;
    private String location;

    public Tweet(String text, String name, String followers) {
        this.name=name;
        this.text=text;
        this.followers=followers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
