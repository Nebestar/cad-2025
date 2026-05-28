package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Component
public class CSVParser implements Parser {

    private static final String DELIMITER = ",";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Product> parse(String data) {
        List<Product> products = new ArrayList<>();

        String[] lines = data.split("\n");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            Product product = parseLine(line);
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }

    private Product parseLine(String line) {
        try {
            String[] columns = line.split(",");
            if (columns.length < 9) return null;

            long productId = Long.parseLong(columns[0].trim());
            String name = columns[1].trim();
            String description = columns[2].trim();
            int categoryId = Integer.parseInt(columns[3].trim());
            BigDecimal price = new BigDecimal(columns[4].trim());
            int stockQuantity = Integer.parseInt(columns[5].trim());
            String imageUrl = columns[6].trim();

            Date createdAt = DATE_FORMAT.parse(columns[7].trim());
            Date updatedAt = DATE_FORMAT.parse(columns[8].trim());

            return new Product(productId, name, description, categoryId,
                    price, stockQuantity, imageUrl, createdAt, updatedAt);
        } catch (Exception e) {
            System.err.println("Ошибка парсинга строки: " + line);
            return null;
        }
    }
}