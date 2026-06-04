package ru.bsuedu.cad.lab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepo;
    private final CustomerRepository custRepo;
    private final ProductRepository prodRepo;

    public OrderService(OrderRepository orderRepo, CustomerRepository custRepo, ProductRepository prodRepo) {
        this.orderRepo = orderRepo;
        this.custRepo = custRepo;
        this.prodRepo = prodRepo;
    }

    @Transactional
    public Order createOrder(Integer customerId, List<OrderItemRequest> items, String shippingAddress) {
        Customer customer = custRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress != null ? shippingAddress : customer.getAddress());
        for (OrderItemRequest item : items) {
            Product product = prodRepo.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));
            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(item.quantity());
            detail.setPrice(product.getPrice());
            order.addOrderDetail(detail);
        }
        order.calculateTotalPrice();
        return orderRepo.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Integer id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    @Transactional
    public void deleteOrder(Integer id) {
        orderRepo.deleteById(id);
    }

    @Transactional
    public Order updateOrder(Integer orderId, Integer customerId, List<OrderItemRequest> items, String shippingAddress, String status) {
        Order order = getOrderById(orderId);
        Customer customer = custRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setStatus(status != null ? status : "NEW");
        order.getOrderDetails().clear();
        for (OrderItemRequest item : items) {
            Product product = prodRepo.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));
            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(item.quantity());
            detail.setPrice(product.getPrice());
            order.addOrderDetail(detail);
        }
        order.calculateTotalPrice();
        return orderRepo.save(order);
    }
}