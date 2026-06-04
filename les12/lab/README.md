# Лабораторная работа №6: Spring MVC и Thymeleaf

## Ход работы
- Скопировал проект из ЛР5 в `les12/lab`
- Добавил зависимости Thymeleaf и Spring MVC
- Переделал `WebConfig` для работы с Thymeleaf
- Создал `OrderRestController` для REST API (GET, POST, PUT, DELETE)
- Создал `OrderWebController` для веб-интерфейса с Thymeleaf
- Добавил методы в `OrderService` (получение по id, удаление, обновление)
- Сделал HTML-шаблоны: список заказов, создание, редактирование
- Приложение собирается как WAR и деплоится на Tomcat 11
- REST проверен через Postman, создана коллекция запросов

## Проверка
- Веб-интерфейс: `http://localhost:8080/petstore/web/orders`
- REST API: `http://localhost:8080/petstore/api/orders`
1. Запустил Tomcat (`catalina.bat run`)
2. Открыл Postman (десктопную версию)
3. Создал новую коллекцию "Petstore API"
4. Проверил запросы:

    - **GET** `http://localhost:8080/petstore/api/orders`  
      Вернулся список заказов в JSON. В ответе есть все поля: id заказа, клиент, дата, сумма, статус, адрес, позиции с товарами и категориями. Даты выводятся как массивы (например, `[2026,6,4,7,12,56,278092000]`).

    - **GET** `http://localhost:8080/petstore/api/orders/1`  
      Вернулся один заказ с id=1.

    - **POST** `http://localhost:8080/petstore/api/orders`  
      В Body (raw, JSON) отправил:
      ```json
      {
        "customerId": 1,
        "items": [
          {"productId": 1, "quantity": 2},
          {"productId": 2, "quantity": 1}
        ],
        "shippingAddress": "ul. Testovaya, 5",
        "status": "NEW"
      }
      
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
    class OrderRestController {
        +getAll()
        +getById()
        +create()
        +update()
        +delete()
    }
    class OrderWebController {
        +list()
        +newForm()
        +create()
        +deleteOrder()
        +editForm()
        +update()
    }
    class OrderService {
        +getAllOrders()
        +getOrderById()
        +createOrder()
        +updateOrder()
        +deleteOrder()
    }

    Category "1" -- "0..*" Product
    Customer "1" -- "0..*" Order
    Order "1" -- "0..*" OrderDetail
    Product "1" -- "0..*" OrderDetail
    OrderRestController --> OrderService
    OrderWebController --> OrderService
    OrderWebController --> CustomerRepository
    OrderWebController --> ProductRepository