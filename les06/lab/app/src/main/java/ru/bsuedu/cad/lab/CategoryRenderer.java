package ru.bsuedu.cad.lab;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CategoryRenderer {
    private final JdbcTemplate jdbcTemplate;
    private final ConcreteCategoryProvider categoryProvider;

    public CategoryRenderer(JdbcTemplate jdbcTemplate, ConcreteCategoryProvider categoryProvider) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryProvider = categoryProvider;
    }

    public void render() {
        List<Category> categories = categoryProvider.loadCategories();
        for (Category c : categories) {
            jdbcTemplate.update("MERGE INTO CATEGORIES (id, name) KEY(id) VALUES (?, ?)",
                    c.getId(), c.getName());
        }
    }
}