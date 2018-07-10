package com.example.madchocho.misea;

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