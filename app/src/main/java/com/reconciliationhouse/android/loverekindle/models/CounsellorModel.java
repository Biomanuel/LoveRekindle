package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CounsellorModel {
    private  String counsellorId;
    private  String counsellorName;
    private  String counsellorProfileImage;

    public CounsellorModel(String counsellorId, String counsellorName, String counsellorProfileImage) {
        this.counsellorId = counsellorId;
        this.counsellorName = counsellorName;
        this.counsellorProfileImage = counsellorProfileImage;
    }

    public String getCounsellorId() {
        return counsellorId;
    }

    public String getCounsellorName() {
        return counsellorName;
    }

    public String getCounsellorProfileImage() {
        return counsellorProfileImage;
    }
}
