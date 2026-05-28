package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;

@Component
public class ConcreteProductProvider implements ProductProvider {
    private final CSVParser csvParser;

    public ConcreteProductProvider(CSVParser csvParser) {
        this.csvParser = csvParser;
    }

    @Override
    public List<Product> getProducts() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("products.csv");
             Scanner scanner = new Scanner(is, "UTF-8")) {
            scanner.useDelimiter("\\A");
            String content = scanner.hasNext() ? scanner.next() : "";
            return csvParser.parse(content);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}