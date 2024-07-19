package org.example.domain;

import java.util.LinkedHashMap;

public class Image {
    private String contentUrl;
    private LinkedHashMap<String, Object> thumbnail;

    public Image() {
    }

    public Image(String contentUrl, LinkedHashMap<String, Object> thumbnail) {
        this.contentUrl = contentUrl;
        this.thumbnail = thumbnail;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public LinkedHashMap<String, Object> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(LinkedHashMap<String, Object> thumbnail) {
        this.thumbnail = thumbnail;
    }
}
