package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {

    private final ProductProvider provider;

    public ConsoleTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();

        if (products.isEmpty()) {
            System.out.println("Нет товаров для отображения.");
            return;
        }

        System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(30) + "+" +
                "-".repeat(15) + "+" + "-".repeat(10) + "+");

        System.out.printf("| %-8s | %-28s | %-13s | %-8s |\n",
                "ID", "Название", "Цена", "Остаток");

        System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(30) + "+" +
                "-".repeat(15) + "+" + "-".repeat(10) + "+");

        for (Product product : products) {
            System.out.printf("| %-8d | %-28s | %-13.2f | %-8d |\n",
                    product.getProductId(),
                    truncate(product.getName(), 28),   // обрезаем длинное имя
                    product.getPrice(),
                    product.getStockQuantity());
        }

        System.out.println("+" + "-".repeat(10) + "+" + "-".repeat(30) + "+" +
                "-".repeat(15) + "+" + "-".repeat(10) + "+");
        System.out.println("Всего товаров: " + products.size());
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}