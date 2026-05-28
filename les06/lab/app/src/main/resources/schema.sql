CREATE TABLE CATEGORIES (
    id INT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE PRODUCTS (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10,2),
    stock_quantity INT,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(id)
);