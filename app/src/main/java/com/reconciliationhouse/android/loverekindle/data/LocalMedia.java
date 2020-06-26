package com.reconciliationhouse.android.loverekindle.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.reconciliationhouse.android.loverekindle.models.MediaItem;

@Entity(tableName = "local_media_table")
public class LocalMedia {

    @PrimaryKey
    @NonNull
    private String mediaId;
    @NonNull
    private String media_uri;
    private int current_position;
    @NonNull
    private String image_url;
    @NonNull
    private String type;
    private boolean downloaded;
    @NonNull
    private String title;
    @NonNull
    private String author;

    /**
     * The LocalMedia Contains all media that has been purchased by the user
     */
    public LocalMedia(@NonNull String mediaId, @NonNull String media_uri, int current_position, @NonNull String image_url, @NonNull String type, @NonNull boolean downloaded, @NonNull String title, @NonNull String author) {
        this.mediaId = mediaId;
        this.media_uri = media_uri;
        this.current_position = current_position;
        this.image_url = image_url;
        this.type = type;
        this.downloaded = downloaded;
        this.title = title;
        this.author = author;
    }

    @Ignore
    public LocalMedia(@NonNull MediaItem mediaItem) {
        this.mediaId = mediaItem.getId();
        this.media_uri = mediaItem.getMedia_url();
        this.current_position = 0;
        this.image_url = mediaItem.getImage_url();
        this.type = mediaItem.getType().toString();
        this.downloaded = false;
        this.title = mediaItem.getTitle();
        this.author = mediaItem.getAuthor();
    }

    @NonNull
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(@NonNull String mediaId) {
        this.mediaId = mediaId;
    }

    @NonNull
    public String getMedia_uri() {
        return media_uri;
    }

    public void setMedia_uri(@NonNull String media_uri) {
        this.media_uri = media_uri;
    }

    public int getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(int current_position) {
        this.current_position = current_position;
    }

    @NonNull
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(@NonNull String image_url) {
        this.image_url = image_url;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(@NonNull boolean downloaded) {
        this.downloaded = downloaded;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }
}
