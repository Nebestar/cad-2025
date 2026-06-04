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

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Integer customerId, List<OrderItemRequest> items, String shippingAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + customerId + " not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus("NEW");
        order.setShippingAddress(shippingAddress != null ? shippingAddress : customer.getAddress());

        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product with ID " + item.productId() + " not found"));

            OrderDetail detail = new OrderDetail();
            detail.setProduct(product);
            detail.setQuantity(item.quantity());
            detail.setPrice(product.getPrice());
            order.addOrderDetail(detail);
            log.info("Dobavlen tovar: '{}' ({} sht.) po cene {} RUB",
                    product.getName(), item.quantity(), product.getPrice());
        }

        order.calculateTotalPrice();
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}