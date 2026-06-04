# Лабораторная работа №4: JPA, Hibernate, Spring Data

## Цель
Переход от Spring JDBC к ORM Hibernate и Spring Data JPA. Реализация слоистой архитектуры: entity, repository, service, app.

## Ход работы
1. В проект добавлены зависимости Spring Data JPA, Hibernate, HikariCP, H2.
2. Созданы JPA-сущности, соответствующие ER-диаграмме: `Category`, `Product`, `Customer`, `Order`, `OrderDetail`.
3. Для каждой сущности написан интерфейс-репозиторий, наследующий `JpaRepository`.
4. Сервис `OrderService` реализует транзакционное создание заказа и получение списка заказов.
5. Конфигурация вынесена в класс `AppConfig`. DataSource создан на базе HikariCP.
6. Настройки подключения к H2 и параметры Hibernate заданы в `application.properties`.
7. Схема БД автоматически создаётся Hibernate (`hibernate.hbm2ddl.auto=update`).
8. При запуске (`gradle run`) данные загружаются из CSV-файлов (`category.csv`, `customer.csv`, `product.csv`) через репозитории.
9. Создаётся тестовый заказ с двумя позициями, пересчитывается сумма.
10. Лог подтверждает сохранение: общее количество заказов в БД — 1.

## Результат
- В логе присутствуют SQL-запросы Hibernate (создание таблиц, вставки).
- Сообщение: `Zakaz #1 sozdan. Summa: 3300.00 RUB`
- Вывод `Vsego zakazov v BD: 1` доказывает успешное сохранение заказа.

## UML-диаграмма классов
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
        +addOrderDetail()
        +calculateTotalPrice()
    }
    class OrderDetail {
        +Integer orderDetailId
        +Order order
        +Product product
        +Integer quantity
        +BigDecimal price
    }

    Category "1" -- "0..*" Product : содержит
    Customer "1" -- "0..*" Order : размещает
    Order "1" -- "0..*" OrderDetail : содержит
    Product "1" -- "0..*" OrderDetail : включён в