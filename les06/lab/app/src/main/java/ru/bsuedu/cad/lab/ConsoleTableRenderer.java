package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {
    private final ConcreteProductProvider productProvider;

    public ConsoleTableRenderer(ConcreteProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        List<Product> products = productProvider.getProducts();
        for (Product p : products) {
            System.out.printf("%d | %s | %s | %d%n", p.getId(), p.getName(), p.getPrice(), p.getStock());
        }
    }
}