Отчет по лабораторной работе №3: Технологии работы с базами данных. JDBC
Цель работы
Освоение инструментов Spring JDBC для интеграции реляционной базы данных в приложение, автоматизация создания схемы БД и реализация аналитических SQL-запросов с использованием JdbcTemplate.

Ход работы

В проект интегрирована встраиваемая база данных H2. Для ее инициализации в классе DatabaseConfig использован инструмент EmbeddedDatabaseBuilder.

Написан DDL-скрипт schema.sql, который автоматически выполняется при старте контейнера Spring и создает связанные таблицы CATEGORIES и PRODUCTS с использованием внешних ключей (Foreign Key).

Создан класс-сущность Category и соответствующий провайдер ConcreteCategoryProvider, обеспечивающий загрузку данных о категориях из файла category.csv.

Разработана новая приоритетная реализация интерфейса Renderer — DataBaseRenderer. Данный компонент отвечает за извлечение данных из CSV-файлов через провайдеры и их последовательную вставку в таблицы H2 с помощью метода update() класса JdbcTemplate.

Создан компонент CategoryRequest, выполняющий агрегирующий SQL-запрос с объединением таблиц и фильтрацией (JOIN, GROUP BY, HAVING). Запрос определяет категории, в которых числится более одного товара.

Вывод результатов аналитического запроса реализован не через стандартный поток вывода, а с использованием библиотеки логирования Logback (SLF4J) на уровне INFO.

Вывод: в результате работы были изучены механизмы абстракции Spring над традиционным JDBC. Использование бина JdbcTemplate позволило полностью исключить шаблонный код (управление соединениями, обработка SQLException) и существенно упростило маппинг результатов (RowMapper). Переход от файловой системы хранения к in-memory базе данных обеспечил возможность выполнения сложных реляционных запросов и контроля ссылочной целостности данных.

Диаграмма Mermaid:
```mermaid
classDiagram
class App {
+main(String[] args)
}
class DatabaseConfig {
<<Configuration>>
+dataSource() DataSource
+jdbcTemplate(DataSource) JdbcTemplate
}
class DataBaseRenderer {
<<Component>>
-JdbcTemplate jdbcTemplate
+render() void
}
class CategoryRenderer {
<<Component>>
-JdbcTemplate jdbcTemplate
+render() void
}
class CategoryRequest {
<<Component>>
-JdbcTemplate jdbcTemplate
+printCategoriesWithMoreThanOneProduct() void
}
class ConcreteCategoryProvider {
<<Component>>
+loadCategories() List~Category~
}
class ConcreteProductProvider {
<<Component>>
+getProducts() List~Product~
}
class Category {
-int id
-String name
}
class Product {
-long id
-String name
-BigDecimal price
-int stock
}

    App --> DatabaseConfig
    DataBaseRenderer ..|> Renderer
    DataBaseRenderer --> ConcreteProductProvider : @Autowired
    DataBaseRenderer --> CategoryRenderer : @Autowired
    CategoryRenderer --> ConcreteCategoryProvider : @Autowired
    DataBaseRenderer --> JdbcTemplate : @Autowired
    CategoryRequest --> JdbcTemplate : @Autowired
    CategoryRequest ..> Product
    CategoryRequest ..> Category