package com.reconciliationhouse.android.loverekindle.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
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
    private MediaType type;
    private String media_url;
    private List<String> tags;
    private String publisher;
    private String contributors;

    public MediaItem(MediaType mediaType) {
        this.type = mediaType;
        this.id = String.valueOf((new Date()).getTime());
    }

    public MediaItem() {
        this.id = String.valueOf((new Date()).getTime());
    }

    public void downloaded() {
        this.download_count++;
    }

    public enum MediaType {
        AUDIO, EBOOK, SERMON
    }

    public MediaItem(String id, String image_url, String title, String category, String author,
                     String released, float price, String length, int download_count, String description,
                     MediaType type, String media_url, java.util.Date timestamp, List<String> tags, String publisher, String contributors) {
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
        this.type = type;
        this.media_url = media_url;
        this.timestamp=timestamp;
        this.tags = tags;
        this.publisher = publisher;
        this.contributors = contributors;
    }

    public MediaItem(String image_url, String title, String category, String author, String released,
                     float price, String length, int download_count, String description, MediaType type,
                     String media_url, java.util.Date timestamp, List<String> tags, String publisher, String contributors) {
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
        this.type = type;
        this.media_url = media_url;
        this.timestamp=timestamp;
        this.tags = tags;
        this.publisher = publisher;
        this.contributors = contributors;
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
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }


    //    endregion Getters and Setters

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
                getTimestamp().equals(mediaItem.getTimestamp()) &&
                getId().equals(mediaItem.getId()) &&
                getImage_url().equals(mediaItem.getImage_url()) &&
                getTitle().equals(mediaItem.getTitle()) &&
                getCategory().equals(mediaItem.getCategory()) &&
                getAuthor().equals(mediaItem.getAuthor()) &&
                getReleased().equals(mediaItem.getReleased()) &&
                getLength().equals(mediaItem.getLength()) &&
                getDescription().equals(mediaItem.getDescription()) &&
                getType() == mediaItem.getType() &&
                getMedia_url().equals(mediaItem.getMedia_url()) &&
                Objects.equals(getTags(), mediaItem.getTags()) &&
                Objects.equals(getPublisher(), mediaItem.getPublisher()) &&
                Objects.equals(getContributors(), mediaItem.getContributors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getId(), getImage_url(), getTitle(), getCategory(), getAuthor(), getReleased(), getPrice(), getLength(), getDownload_count(), getDescription(), getType(), getMedia_url(), getTags(), getPublisher(), getContributors());
    }
}
