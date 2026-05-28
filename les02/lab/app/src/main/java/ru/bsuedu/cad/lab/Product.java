package ru.bsuedu.cad.lab;
import java.math.BigDecimal;
import java.util.Date;

public class Product {

    private long productId;         // номер товара
    private String name;
    private String description;     // описание
    private int categoryId;
    private BigDecimal price;
    private int stockQuantity;      // кол-во на складе
    private String imageUrl;
    private Date createdAt;         // дата создания товара
    private Date updatedAt;         // дата последнего изменения

    public Product(long productId, String name, String description, int categoryId,
                   BigDecimal price, int stockQuantity, String imageUrl,
                   Date createdAt, Date updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    //для просмотра
    public long getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public BigDecimal getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getImageUrl() { return imageUrl; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    //для изменения
    public void setProductId(long productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', price=%s, stock=%d}",
                productId, name, price, stockQuantity);
    }
}