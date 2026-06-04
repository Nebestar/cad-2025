package ru.bsuedu.cad.lab.controller;

import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request.customerId(), request.items(), request.shippingAddress());
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Integer id, @RequestBody CreateOrderRequest request) {
        return orderService.updateOrder(id, request.customerId(), request.items(), request.shippingAddress(), request.status());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
    }

    public record CreateOrderRequest(Integer customerId, List<OrderItemRequest> items, String shippingAddress, String status) {}
}