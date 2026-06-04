package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(attributePaths = {"customer", "orderDetails", "orderDetails.product", "orderDetails.product.category"})
    List<Order> findAll();
}