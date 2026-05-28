package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private long id;
    private String name;
    private String description;
    private int categoryId;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private Date createdAt;
    private Date updatedAt;

    public Product(long id, String name, String description, int categoryId,
                   BigDecimal price, int stock, String imageUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
}