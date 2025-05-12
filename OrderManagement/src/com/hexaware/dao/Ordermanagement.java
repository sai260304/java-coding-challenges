package com.hexaware.dao;

import com.hexaware.entity.*;
import com.hexaware.myexception.OrderNotFoundException;
import com.hexaware.myexception.UserNotFoundException;

import java.util.List;

public interface Ordermanagement {

    void createUser(User user) throws UserNotFoundException;

    void createProduct(User user, Product product) throws Exception;

    void createOrder(User user, List<Product> products) throws  OrderNotFoundException;

    void cancelOrder(int userId, int orderId) throws Exception,UserNotFoundException,OrderNotFoundException;

    List<Product> getAllProducts() throws Exception;

    List<Product> getOrderByUser(User user) throws Exception;
}
