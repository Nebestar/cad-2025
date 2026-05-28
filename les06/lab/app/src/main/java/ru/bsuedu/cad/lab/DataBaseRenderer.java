package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataBaseRenderer implements Renderer {
    private final JdbcTemplate jdbcTemplate;
    private final ConcreteProductProvider productProvider;
    private final ConcreteCategoryProvider categoryProvider;

    @Autowired
    public DataBaseRenderer(JdbcTemplate jdbcTemplate, ConcreteProductProvider productProvider, ConcreteCategoryProvider categoryProvider) {
        this.jdbcTemplate = jdbcTemplate;
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
    }

    @Override
    public void render() {
        List<Category> categories = categoryProvider.loadCategories();
        for (Category c : categories) {
            jdbcTemplate.update("MERGE INTO CATEGORIES (id, name) KEY(id) VALUES (?, ?)",
                    c.getId(), c.getName());
        }

        List<Product> products = productProvider.getProducts();
        for (Product p : products) {
            jdbcTemplate.update("MERGE INTO PRODUCTS (id, name, price, stock_quantity, category_id) KEY(id) VALUES (?, ?, ?, ?, ?)",
                    p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getCategoryId());
        }
        System.out.println("Данные успешно сохранены в БД!");
    }
}