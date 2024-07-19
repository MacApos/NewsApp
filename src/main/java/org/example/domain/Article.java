package org.example.domain;

import java.util.LinkedHashMap;

public class Article {
    private String name;
    private String url;
    private Image image;

    public Article() {
    }

    public Article(String name, String url, Image image) {
        this.name = name;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image  image) {
        this.image = image;
    }
}
