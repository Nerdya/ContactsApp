package com.example.contactsapp;

public class Contact {
    private String userName;
    private String phoneNumber;
    private String emailAddress;

    public Contact() {
    }

    public Contact(String userName, String phoneNumber, String emailAddress) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
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
        return "Họ tên: " + userName + "\n" +
                "Số điện thoại: " + phoneNumber + "\n" +
                "Email: " + emailAddress;
    }
}
