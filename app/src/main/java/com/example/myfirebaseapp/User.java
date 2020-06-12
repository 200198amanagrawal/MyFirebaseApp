package com.example.myfirebaseapp;

public class User {
    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    String name;
    String age;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
