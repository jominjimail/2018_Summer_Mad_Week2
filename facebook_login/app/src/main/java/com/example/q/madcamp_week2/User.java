package com.example.q.madcamp_week2;

public class User {

    public String name;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uname, String uphone) {
        this.name = uname;
        this.phone = uphone;
    }

}
