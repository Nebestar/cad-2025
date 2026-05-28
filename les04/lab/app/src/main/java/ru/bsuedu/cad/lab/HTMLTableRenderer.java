package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Primary;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    private final ProductProvider provider;

    @Autowired
    public HTMLTableRenderer(ProductProvider provider) {
        this.provider = provider;
    }

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        if (products.isEmpty()) {
            System.out.println("Нет товаров для отображения.");
            return;
        }

        String html = generateHtml(products);
        String filePath = "products.html";
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            writer.write(html);
            System.out.println("HTML-отчёт сгенерирован: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка записи HTML-файла: " + e.getMessage());
        }
    }

    private String generateHtml(List<Product> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n<head><meta charset=\"UTF-8\"><title>Товары для зоомагазина</title>");
        sb.append("<style>table {border-collapse: collapse; width: 80%; margin: 20px auto;} ");
        sb.append("th, td {border: 1px solid black; padding: 8px; text-align: left;} ");
        sb.append("th {background-color: #f2f2f2;}</style>");
        sb.append("</head>\n<body>\n");
        sb.append("<h2 style=\"text-align:center\">Список товаров</h2>\n");
        sb.append("<table>\n");
        sb.append("<tr><th>ID</th><th>Название</th><th>Цена</th><th>Остаток</th></tr>\n");
        for (Product p : products) {
            sb.append("<tr>");
            sb.append("<td>").append(p.getProductId()).append("</td>");
            sb.append("<td>").append(p.getName()).append("</td>");
            sb.append("<td>").append(p.getPrice()).append("</td>");
            sb.append("<td>").append(p.getStockQuantity()).append("</td>");
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        sb.append("<p style=\"text-align:center\">Всего товаров: ").append(products.size()).append("</p>\n");
        sb.append("</body>\n</html>");
        return sb.toString();
    }
}