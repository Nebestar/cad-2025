package ru.bsuedu.cad.lab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {

    private final ProductRepository prodRepo;

    public ProductRestController(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    @GetMapping("/api/products")
    public List<ProductInfo> getProducts() {
        return prodRepo.findAll().stream()
                .map(p -> new ProductInfo(p.getName(), p.getCategory().getName(), p.getStockQuantity()))
                .collect(Collectors.toList());
    }

    public record ProductInfo(String productName, String categoryName, int stockQuantity) {}
}