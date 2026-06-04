package ru.bsuedu.cad.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final CustomerRepository custRepo;
    private final ProductRepository prodRepo;

    public OrderWebController(OrderService orderService, CustomerRepository custRepo, ProductRepository prodRepo) {
        this.orderService = orderService;
        this.custRepo = custRepo;
        this.prodRepo = prodRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "order-list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customers", custRepo.findAll());
        model.addAttribute("products", prodRepo.findAll());
        return "order-form";
    }

    @PostMapping
    public String create(@RequestParam Integer customerId,
                         @RequestParam List<Integer> productIds,
                         @RequestParam List<Integer> quantities,
                         @RequestParam String shippingAddress) {
        List<OrderItemRequest> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            if (quantities.get(i) > 0) {
                items.add(new OrderItemRequest(productIds.get(i), quantities.get(i)));
            }
        }
        orderService.createOrder(customerId, items, shippingAddress);
        return "redirect:/web/orders";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/web/orders";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("customers", custRepo.findAll());
        model.addAttribute("products", prodRepo.findAll());
        return "order-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                         @RequestParam Integer customerId,
                         @RequestParam List<Integer> productIds,
                         @RequestParam List<Integer> quantities,
                         @RequestParam String shippingAddress,
                         @RequestParam String status) {
        List<OrderItemRequest> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            if (quantities.get(i) > 0) {
                items.add(new OrderItemRequest(productIds.get(i), quantities.get(i)));
            }
        }
        orderService.updateOrder(id, customerId, items, shippingAddress, status);
        return "redirect:/web/orders";
    }
}