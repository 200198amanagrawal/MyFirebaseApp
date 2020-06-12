package com.example.myfirebaseapp;

public class User {
    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String uid;
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
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User)
        {
            User user= (User) obj;
            return this.uid.equals(user.getUid());
        }
        return false;
    }
}
