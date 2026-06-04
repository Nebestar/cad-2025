package ru.bsuedu.cad.lab;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final CategoryRepository catRepo;
    private final CustomerRepository custRepo;
    private final ProductRepository prodRepo;
    private final OrderService orderService;

    public DataLoader(CategoryRepository catRepo, CustomerRepository custRepo,
                      ProductRepository prodRepo, OrderService orderService) {
        this.catRepo = catRepo;
        this.custRepo = custRepo;
        this.prodRepo = prodRepo;
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        if (catRepo.count() == 0) {
            loadCsv("/category.csv", cols -> {
                Category c = new Category();
                c.setCategoryId(Integer.parseInt(cols[0]));
                c.setName(cols[1]);
                c.setDescription(cols[2]);
                catRepo.save(c);
            });
            log.info("Zagruzheny categorii");
        }
        if (custRepo.count() == 0) {
            loadCsv("/customer.csv", cols -> {
                Customer c = new Customer();
                c.setCustomerId(Integer.parseInt(cols[0]));
                c.setName(cols[1]);
                c.setEmail(cols[2]);
                c.setPhone(cols[3]);
                c.setAddress(cols[4]);
                custRepo.save(c);
            });
            log.info("Zagruzheny pokupateli");
        }
        if (prodRepo.count() == 0) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            loadCsv("/product.csv", cols -> {
                Product p = new Product();
                p.setProductId(Integer.parseInt(cols[0]));
                p.setName(cols[1]);
                p.setDescription(cols[2]);
                p.setCategory(catRepo.findById(Integer.parseInt(cols[3])).orElseThrow());
                p.setPrice(new BigDecimal(cols[4]));
                p.setStockQuantity(Integer.parseInt(cols[5]));
                p.setImageUrl(cols[6]);
                p.setCreatedAt(LocalDateTime.parse(cols[7], fmt));
                p.setUpdatedAt(LocalDateTime.parse(cols[8], fmt));
                prodRepo.save(p);
            });
            log.info("Zagruzheny tovary");
        }

        if (orderService.getAllOrders().isEmpty()) {
            List<OrderItemRequest> items = List.of(
                    new OrderItemRequest(1, 2),
                    new OrderItemRequest(2, 1)
            );
            orderService.createOrder(1, items, "ul. Testovaya, 10");
            log.info("Sozdan testovyj zakaz");
        }
    }

    private void loadCsv(String path, java.util.function.Consumer<String[]> handler) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                getClass().getResourceAsStream(path), java.nio.charset.StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                handler.accept(rows.get(i));
            }
        } catch (Exception e) {
            log.error("Oshibka zagruzki " + path, e);
        }
    }
}