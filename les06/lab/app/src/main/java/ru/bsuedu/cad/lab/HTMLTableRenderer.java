package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HTMLTableRenderer implements Renderer {
    private final ConcreteProductProvider productProvider;

    public HTMLTableRenderer(ConcreteProductProvider productProvider) {
        this.productProvider = productProvider;
    }

    @Override
    public void render() {
        List<Product> products = productProvider.getProducts();
        StringBuilder sb = new StringBuilder("<table>");
        for (Product p : products) {
            sb.append("<tr><td>").append(p.getId()).append("</td>")
                    .append("<td>").append(p.getName()).append("</td>")
                    .append("<td>").append(p.getPrice()).append("</td>")
                    .append("<td>").append(p.getStock()).append("</td></tr>");
        }
        sb.append("</table>");
        System.out.println(sb.toString());
    }
}