drop database Order_management;
create database Order_management;
use Order_management;

CREATE TABLE Users (
    userId INT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('Admin', 'User')) NOT NULL
);

CREATE TABLE Product (
    productId INT PRIMARY KEY,
    productName VARCHAR(100) NOT NULL,
    description TEXT,
    price DOUBLE PRECISION NOT NULL,
    quantityInStock INT NOT NULL,
    type VARCHAR(20) CHECK (type IN ('Electronics', 'Clothing')) NOT NULL
);

CREATE TABLE Electronics (
    productId INT PRIMARY KEY,
    brand VARCHAR(100),
    warrantyPeriod INT,
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

CREATE TABLE Clothing (
    productId INT PRIMARY KEY,
    size VARCHAR(10),
    color VARCHAR(50),
    FOREIGN KEY (productId) REFERENCES Product(productId)
);

CREATE TABLE Orders (
    orderId INT PRIMARY KEY auto_increment,
    userId INT,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

CREATE TABLE OrderDetails (
    orderId INT,
    productId INT,
    quantity INT,
    PRIMARY KEY (orderId, productId),
    FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES Product(productId) ON DELETE CASCADE
);