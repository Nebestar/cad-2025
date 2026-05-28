package ru.bsuedu.cad.lab;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConcreteProductProvider implements ProductProvider {

    private final Reader reader;
    private final Parser parser;

    public ConcreteProductProvider(Reader reader, Parser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Product> getProducts() {
        String rawData = reader.read();   // 1. читаем данные
        return parser.parse(rawData);     // 2. парсим строку в список товаров
    }
}