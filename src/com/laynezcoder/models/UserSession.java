package com.laynezcoder.models;

public class UserSession {

    private Integer customerId;
    private String nameUser;
    private String email;
    private String pass;
    private String biography;
    private String dialogTransition;
    private String userType;

    public UserSession() {
    }

    public UserSession(Integer customerId, String nameUser, String email, String pass, String biography, String dialogTransition, String userType) {
        this.customerId = customerId;
        this.nameUser = nameUser;
        this.email = email;
        this.pass = pass;
        this.biography = biography;
        this.dialogTransition = dialogTransition;
        this.userType = userType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getDialogTransition() {
        return dialogTransition;
    }

    public void setDialogTransition(String dialogTransition) {
        this.dialogTransition = dialogTransition;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
