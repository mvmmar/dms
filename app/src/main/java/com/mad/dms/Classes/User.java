package com.mad.dms.Classes;

public class User {
    private String id;
    private String name;
    private String position;
    private String email;
    private String password;
    private String phoneNo;

    public User(){

    }

    public User(String name, String position, String email, String password, String phoneNo) {
        this.name = name;
        this.position = position;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    public User(String id, String name, String position, String email, String password, String phoneNo) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
