package com.example.adminshoesshop.models;

public class ProductModel {
    private String id, name, img_url, description, type;
    private long price;
    private long price_1;
    private String rating;

    public ProductModel() {
    }

    public ProductModel(String id, String name, String img_url, String description, String type, long price, long price_1, String rating) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.description = description;
        this.type = type;
        this.price = price;
        this.price_1 = price_1;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPrice_1() {
        return price_1;
    }

    public void setPrice_1(long price_1) {
        this.price_1 = price_1;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
