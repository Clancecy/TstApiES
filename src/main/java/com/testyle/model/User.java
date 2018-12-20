package com.testyle.model;

import java.util.Date;

public class User {
    private long userID=-1;
    private int userType=-1;
    private String userName;
    private String userRealName;
    private String userPassword;
    private int userSex=-1;
    private String userMobile;
    private String userMail;
    private String userNumber;
    private int userCompanyID=-1;
    private Date addtime=new Date();

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public int getUserCompanyID() {
        return userCompanyID;
    }

    public void setUserCompanyID(int userCompanyID) {
        this.userCompanyID = userCompanyID;
    }

    public Date getAddTime() {
        return addtime;
    }

    public void setAddTime(Date addtime) {
        this.addtime = addtime;
    }
    public User() {
    }
}
