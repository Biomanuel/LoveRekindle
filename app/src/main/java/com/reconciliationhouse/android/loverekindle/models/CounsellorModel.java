package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class CounsellorModel {

    private  String counsellorName;
    private  String counsellorProfileImage;
    private String counsellorEmail;
    private String counsellorDescription;
    private UserModel.Category counsellorCategory;
    private Date timeStamp;



    public CounsellorModel(){

    }

//    public CounsellorModel(String counsellorName, String counsellorProfileImage, String counsellorEmail, String counsellorDescription, String counsellorCategory) {
//        this.counsellorName = counsellorName;
//        this.counsellorProfileImage = counsellorProfileImage;
//        this.counsellorEmail = counsellorEmail;
//        this.counsellorDescription = counsellorDescription;
//        this.counsellorCategory = counsellorCategory;
//    }

    @ServerTimestamp
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCounsellorName() {
        return counsellorName;
    }

    public void setCounsellorName(String counsellorName) {
        this.counsellorName = counsellorName;
    }

    public String getCounsellorProfileImage() {
        return counsellorProfileImage;
    }

    public void setCounsellorProfileImage(String counsellorProfileImage) {
        this.counsellorProfileImage = counsellorProfileImage;
    }

    public String getCounsellorEmail() {
        return counsellorEmail;
    }

    public void setCounsellorEmail(String counsellorEmail) {
        this.counsellorEmail = counsellorEmail;
    }

    public String getCounsellorDescription() {
        return counsellorDescription;
    }

    public void setCounsellorDescription(String counsellorDescription) {
        this.counsellorDescription = counsellorDescription;
    }

    public UserModel.Category getCounsellorCategory() {
        return counsellorCategory;
    }

    public void setCounsellorCategory(UserModel.Category counsellorCategory) {
        this.counsellorCategory = counsellorCategory;
    }
}
