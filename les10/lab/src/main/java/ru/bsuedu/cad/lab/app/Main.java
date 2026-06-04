package ru.bsuedu.cad.lab.app;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.AppConfig;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;
import ru.bsuedu.cad.lab.service.OrderService;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        log.info("Magazin zootovarov zapuschen");

        loadCategories(context);
        loadCustomers(context);
        loadProducts(context);

        OrderService orderService = context.getBean(OrderService.class);
        CustomerRepository customerRepo = context.getBean(CustomerRepository.class);

        List<Customer> customers = customerRepo.findAll();
        if (customers.isEmpty()) {
            log.error("Net pokupateley v baze. Zavershenie.");
            context.close();
            return;
        }
        Customer customer = customers.get(0);
        log.info("Vybran pokupatel: {}", customer.getName());

        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(1, 2),
                new OrderItemRequest(2, 1)
        );

        log.info("Sozdanie zakaza...");
        Order order = orderService.createOrder(customer.getCustomerId(), items, "ul. Primernaya, d. 5");
        log.info("Zakaz #{} sozdan. Summa: {} RUB", order.getOrderId(), order.getTotalPrice());

        List<Order> allOrders = orderService.getAllOrders();
        log.info("Vsego zakazov v BD: {}", allOrders.size());
        for (Order o : allOrders) {
            log.info("Zakaz #{} ot {}, status: '{}', summa: {} RUB",
                    o.getOrderId(), o.getOrderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    o.getStatus(), o.getTotalPrice());
        }

        log.info("Prilozhenie zaversheno.");
        context.close();
    }

    private static void loadCategories(AnnotationConfigApplicationContext context) {
        CategoryRepository repo = context.getBean(CategoryRepository.class);
        if (repo.count() > 0) return;

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Main.class.getResourceAsStream("/category.csv")))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] cols = rows.get(i);
                Category c = new Category();
                c.setCategoryId(Integer.parseInt(cols[0]));
                c.setName(cols[1]);
                c.setDescription(cols[2]);
                repo.save(c);
            }
            log.info("Zagruzheno categoriy: {}", rows.size() - 1);
        } catch (Exception e) {
            log.error("Oshibka zagruzki category.csv", e);
        }
    }

    private static void loadCustomers(AnnotationConfigApplicationContext context) {
        CustomerRepository repo = context.getBean(CustomerRepository.class);
        if (repo.count() > 0) return;

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Main.class.getResourceAsStream("/customer.csv")))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] cols = rows.get(i);
                Customer c = new Customer();
                c.setCustomerId(Integer.parseInt(cols[0]));
                c.setName(cols[1]);
                c.setEmail(cols[2]);
                c.setPhone(cols[3]);
                c.setAddress(cols[4]);
                repo.save(c);
            }
            log.info("Zagruzheno pokupateley: {}", rows.size() - 1);
        } catch (Exception e) {
            log.error("Oshibka zagruzki customer.csv", e);
        }
    }

    private static void loadProducts(AnnotationConfigApplicationContext context) {
        ProductRepository productRepo = context.getBean(ProductRepository.class);
        CategoryRepository categoryRepo = context.getBean(CategoryRepository.class);
        if (productRepo.count() > 0) return;

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                Main.class.getResourceAsStream("/product.csv")))) {
            List<String[]> rows = reader.readAll();
            for (int i = 1; i < rows.size(); i++) {
                String[] cols = rows.get(i);
                Product p = new Product();
                p.setProductId(Integer.parseInt(cols[0]));
                p.setName(cols[1]);
                p.setDescription(cols[2]);
                Integer catId = Integer.parseInt(cols[3]);
                p.setCategory(categoryRepo.findById(catId).orElseThrow());
                p.setPrice(new BigDecimal(cols[4]));
                p.setStockQuantity(Integer.parseInt(cols[5]));
                p.setImageUrl(cols[6]);
                p.setCreatedAt(LocalDateTime.parse(cols[7], DT_FORMATTER));
                p.setUpdatedAt(LocalDateTime.parse(cols[8], DT_FORMATTER));
                productRepo.save(p);
            }
            log.info("Zagruzheno tovarov: {}", rows.size() - 1);
        } catch (Exception e) {
            log.error("Oshibka zagruzki product.csv", e);
        }
    }
}