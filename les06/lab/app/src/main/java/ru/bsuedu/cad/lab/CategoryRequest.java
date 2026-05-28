package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequest {
    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void printCategoriesWithMoreThanOneProduct() {
        String sql = "SELECT C.name FROM CATEGORIES C " +
                "JOIN PRODUCTS P ON C.id = P.category_id " +
                "GROUP BY C.name " +
                "HAVING COUNT(P.id) > 1";

        jdbcTemplate.query(sql, (rs, row) -> rs.getString("name"))
                .forEach(name -> logger.info("Категория с > 1 товаром: {}", name));
    }
}