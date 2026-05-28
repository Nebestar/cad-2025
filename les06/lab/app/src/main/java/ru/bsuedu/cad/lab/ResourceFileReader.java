package ru.bsuedu.cad.lab;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ResourceFileReader implements Reader {

    private final String filePath;

    public ResourceFileReader(@Value("${product.file.name:products.csv}") String filePath) {
        this.filePath = filePath;
    }

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);
        System.out.println("ResourceFileReader инициализирован: " + now);
    }

    @Override
    public String read() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден: " + filePath);
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }
}