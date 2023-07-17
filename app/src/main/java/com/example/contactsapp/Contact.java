package com.example.contactsapp;

public class Contact {
    private String id;
    private String userName;
    private String phoneNumber;
    private String emailAddress;

    public Contact() {
    }

    public Contact(String id, String userName, String phoneNumber, String emailAddress) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "UserName: " + userName + "\n" +
                "PhoneNumber: " + phoneNumber + "\n" +
                "EmailAddress: " + emailAddress;
    }
}
