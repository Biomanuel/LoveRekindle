package com.reconciliationhouse.android.loverekindle.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MediaReview {

    private String id;
    @ServerTimestamp
    private Date timeStamp;
    private String mediaId;
    private String userId;
    private String review;
    private HashMap<String, Boolean> votes; // HashMap<UserId, inSupport>

    public MediaReview() {
    }

    public MediaReview(Date timeStamp, String mediaId, String userId, String review) {
        this.timeStamp = timeStamp;
        this.mediaId = mediaId;
        this.userId = userId;
        this.review = review;
    }

    public MediaReview(String mediaId, String userId, String review) {
        this.mediaId = mediaId;
        this.userId = userId;
        this.review = review;
    }

    /**
     * @param votes is an HashMap of the voterId to the vote's state i.e. HashMap<voterId, inSupport>
     */
    public MediaReview(String id, Date timeStamp, String mediaId, String userId, String review, HashMap<String, Boolean> votes) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.mediaId = mediaId;
        this.userId = userId;
        this.review = review;
        this.votes = votes;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public HashMap<String, Boolean> getVotes() {
        return votes;
    }

    public void setVotes(HashMap<String, Boolean> votes) {
        this.votes = votes;
    }

    public void vote(String voterId, boolean inSupport) {
        votes.put(voterId, inSupport);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaReview)) return false;
        MediaReview review1 = (MediaReview) o;
        return Objects.equals(getId(), review1.getId()) &&
                Objects.equals(getTimeStamp(), review1.getTimeStamp()) &&
                getMediaId().equals(review1.getMediaId()) &&
                getUserId().equals(review1.getUserId()) &&
                getReview().equals(review1.getReview());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeStamp(), getMediaId(), getUserId(), getReview());
    }
}
