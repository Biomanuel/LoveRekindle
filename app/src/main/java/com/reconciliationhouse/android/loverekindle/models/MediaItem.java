package com.reconciliationhouse.android.loverekindle.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

@IgnoreExtraProperties
public class MediaItem {

    private @ServerTimestamp java.util.Date timestamp ;
    private String id;
    private String image_url;
    private String title;
    private String category;
    private String author = "Joshua Odeyemi";
    private String released;
    private float price;
    private String length;
    private int download_count;
    private String description;
    private MediaType mType = MediaType.AUDIO;
    private String media_url;
    private String mediaPath;
    private MediaState state = MediaState.PERMANENT;

    public enum MediaState {
        CACHED, PERMANENT
    }

    public enum MediaType {
        AUDIO, EBOOK, SERMON
    }

    public MediaItem(MediaType mediaType){
        this.mType = mediaType;
        this.id = String.valueOf((new Date()).getTime());
    }
    public MediaItem(){
        this.id = String.valueOf((new Date()).getTime());
    }

    public MediaItem(String id, String image_url, String title, String category, String author,
                     String released, float price, String length, int download_count, String description,
                     MediaType type, String media_url, java.util.Date timestamp, String mediaPath) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.category = category;
        this.author = author;
        this.released = released;
        this.price = price;
        this.length = length;
        this.download_count = download_count;
        this.description = description;
        mType = type;
        this.media_url = media_url;
        this.timestamp=timestamp;
        this.mediaPath=mediaPath;
    }

    public MediaItem(String image_url, String title, String category, String author, String released,
                     float price, String length, int download_count, String description, MediaType type,
                     String media_url, java.util.Date timestamp) {
        this.id = String.valueOf((new Date()).getTime());
        this.image_url = image_url;
        this.title = title;
        this.category = category;
        this.author = author;
        this.released = released;
        this.price = price;
        this.length = length;
        this.download_count = download_count;
        this.description = description;
        mType = type;
        this.media_url = media_url;
        this.timestamp=timestamp;
    }

    //    region Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MediaType getType() {
        return mType;
    }

    public void setType(MediaType type) {
        mType = type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public MediaState getState() {
        return state;
    }

    public void setState(MediaState state) {
        this.state = state;
    }

    //    endregion Getters and Setters

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @BindingAdapter("android:setImage")
    public static void setImage (ImageView view ,String image_url){
        Picasso.get().load(image_url).into(view);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaItem)) return false;
        MediaItem mediaItem = (MediaItem) o;
        return Float.compare(mediaItem.getPrice(), getPrice()) == 0 &&
                getDownload_count() == mediaItem.getDownload_count() &&
                Objects.equals(getTimestamp(), mediaItem.getTimestamp()) &&
                getId().equals(mediaItem.getId()) &&
                Objects.equals(getImage_url(), mediaItem.getImage_url()) &&
                getTitle().equals(mediaItem.getTitle()) &&
                Objects.equals(getCategory(), mediaItem.getCategory()) &&
                Objects.equals(getAuthor(), mediaItem.getAuthor()) &&
                Objects.equals(getReleased(), mediaItem.getReleased()) &&
                Objects.equals(getLength(), mediaItem.getLength()) &&
                Objects.equals(getDescription(), mediaItem.getDescription()) &&
                getType() == mediaItem.getType() &&
                Objects.equals(getMedia_url(), mediaItem.getMedia_url()) &&
                Objects.equals(getMediaPath(), mediaItem.getMediaPath()) &&
                getState() == mediaItem.getState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getId(), getImage_url(), getTitle(), getCategory(), getAuthor(), getReleased(), getPrice(), getLength(), getDownload_count(), getDescription(), getType(), getMedia_url(), getMediaPath(), getState());
    }
}
