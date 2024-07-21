package org.example.domain;

public class Value {
    private String name;
    private String url;
    private Image image;
    private String description;
    private String datePublished;

    public Value() {
    }

    public Value(String name, String url, Image image, String description, String datePublished) {
        this.name = name;
        this.url = url;
        this.image = image;
        this.description = description;
        this.datePublished = datePublished;
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

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }
}
