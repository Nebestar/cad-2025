# Лабораторная работа №5: Web-приложение для магазина

## Ход работы
- Скопировал проект из ЛР4 в `les10/lab`
- Добавил в зависимости Spring MVC, сервлеты, Jackson
- Настроил сборку WAR (gradle war)
- Установил Tomcat 11, добавил пользователя admin/admin
- Написал контроллеры для веба:
    - `/orders` – показывает таблицу заказов
    - `/orders/new` – форма для нового заказа
    - `/api/products` – REST, возвращает JSON с товарами
- Загрузка CSV теперь происходит в DataLoader, который стартует сам
- Приложение деплоится копированием WAR в webapps Tomcat

## Проверка
- Открывал в браузере http://localhost:8080/petstore/orders
- Создал заказ – работает, редирект на список
- В Postman делал GET http://localhost:8080/petstore/api/products – возвращает продукты с категориями и остатками

## UML-диаграмма
```mermaid
classDiagram
    class Category {
        +Integer categoryId
        +String name
        +String description
    }
    class Product {
        +Integer productId
        +String name
        +String description
        +Category category
        +BigDecimal price
        +Integer stockQuantity
        +String imageUrl
        +LocalDateTime createdAt
        +LocalDateTime updatedAt
    }
    class Customer {
        +Integer customerId
        +String name
        +String email
        +String phone
        +String address
    }
    class Order {
        +Integer orderId
        +Customer customer
        +LocalDateTime orderDate
        +BigDecimal totalPrice
        +String status
        +String shippingAddress
    }
    class OrderDetail {
        +Integer orderDetailId
        +Order order
        +Product product
        +Integer quantity
        +BigDecimal price
    }
    class OrderController {
        +listOrders()
        +newOrderForm()
        +createOrder()
    }
    class ProductRestController {
        +getProducts()
    }
    class DataLoader {
        +init()
    }

    Category "1" -- "0..*" Product
    Customer "1" -- "0..*" Order
    Order "1" -- "0..*" OrderDetail
    Product "1" -- "0..*" OrderDetail
    OrderController --> OrderService
    OrderController --> CustomerRepository
    OrderController --> ProductRepository
    ProductRestController --> ProductRepository
    DataLoader --> CategoryRepository
    DataLoader --> CustomerRepository
    DataLoader --> ProductRepository