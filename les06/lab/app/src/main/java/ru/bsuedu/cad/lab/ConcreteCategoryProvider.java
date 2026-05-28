package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;

@Component
public class ConcreteCategoryProvider {
    public List<Category> loadCategories() {
        List<Category> categories = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("category.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                categories.add(new Category(id, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }
}