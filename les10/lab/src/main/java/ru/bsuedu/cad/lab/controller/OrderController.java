package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CustomerRepository custRepo;
    private final ProductRepository prodRepo;

    public OrderController(OrderService orderService, CustomerRepository custRepo, ProductRepository prodRepo) {
        this.orderService = orderService;
        this.custRepo = custRepo;
        this.prodRepo = prodRepo;
    }

    @GetMapping("/orders")
    public void list(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        var w = resp.getWriter();
        w.println("<html><body>");
        w.println("<h1>Zakazy</h1>");
        w.println("<table border='1'>");
        w.println("<tr><th>ID</th><th>Data</th><th>Status</th><th>Summa</th></tr>");
        for (Order o : orderService.getAllOrders()) {
            w.println("<tr>");
            w.println("<td>" + o.getOrderId() + "</td>");
            w.println("<td>" + o.getOrderDate() + "</td>");
            w.println("<td>" + o.getStatus() + "</td>");
            w.println("<td>" + o.getTotalPrice() + "</td>");
            w.println("</tr>");
        }
        w.println("</table>");
        w.println("<br/><a href='orders/new'>Sozdat zakaz</a>");
        w.println("</body></html>");
    }

    @GetMapping("/orders/new")
    public void form(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        var w = resp.getWriter();
        var custs = custRepo.findAll();
        var prods = prodRepo.findAll();

        w.println("<html><body>");
        w.println("<h1>Sozdanie zakaza</h1>");
        w.println("<form method='post' action='/petstore/orders'>");
        w.println("Klient: <select name='customerId'>");
        for (var c : custs) {
            w.println("<option value='" + c.getCustomerId() + "'>" + c.getName() + "</option>");
        }
        w.println("</select><br/><br/>");
        w.println("Adres dostavki: <input type='text' name='shippingAddress'/><br/><br/>");
        w.println("<table border='1'>");
        w.println("<tr><th>Tovar</th><th>Cena</th><th>Kol-vo</th></tr>");
        for (var p : prods) {
            w.println("<tr>");
            w.println("<td>" + p.getName() + "</td>");
            w.println("<td>" + p.getPrice() + "</td>");
            w.println("<td><input type='hidden' name='productIds' value='" + p.getProductId() + "'/>");
            w.println("<input type='number' name='quantities' min='0' value='0' style='width:60px'/></td>");
            w.println("</tr>");
        }
        w.println("</table><br/>");
        w.println("<input type='submit' value='Sozdat zakaz'/>");
        w.println("</form>");
        w.println("</body></html>");
    }

    @PostMapping("/orders")
    public void create(@RequestParam Integer customerId,
                       @RequestParam List<Integer> productIds,
                       @RequestParam List<Integer> quantities,
                       @RequestParam String shippingAddress,
                       HttpServletResponse resp) throws IOException {
        List<OrderItemRequest> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            if (quantities.get(i) > 0) {
                items.add(new OrderItemRequest(productIds.get(i), quantities.get(i)));
            }
        }
        orderService.createOrder(customerId, items, shippingAddress);
        resp.sendRedirect("/petstore/orders");
    }
}