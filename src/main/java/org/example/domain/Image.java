package org.example.domain;

import java.util.LinkedHashMap;

public class Image {
    private String contentUrl;

    public Image() {
    }

    public Image(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
