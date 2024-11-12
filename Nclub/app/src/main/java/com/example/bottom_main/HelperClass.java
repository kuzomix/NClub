package com.example.bottom_main;

import android.util.Log;

public class HelperClass {
    String name,email, username, password, createdAt, lastLogin, userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    };

    public String getLastLogin() { return lastLogin; }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    };

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId;
    };
    public HelperClass(String name, String email, String username, String password, String createdAt, String lastLogin, String userId) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password =password;
        this.createdAt =createdAt;
        this.lastLogin =lastLogin;
        this.userId = userId;
        Log.d("Debug", "HelperClass.java userId: " + this.userId);
        Log.d("Debug", "HelperClass.java hashedPassword: " + password);


    }

    public HelperClass(){

    }

}
