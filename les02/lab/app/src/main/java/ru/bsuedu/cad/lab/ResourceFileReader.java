package ru.bsuedu.cad.lab;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ResourceFileReader implements Reader {

    private final String filePath;

    public ResourceFileReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String read() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден: " + filePath);
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                scanner.useDelimiter("\\A"); // чтобы сканер вернул весь текст целиком
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }
}