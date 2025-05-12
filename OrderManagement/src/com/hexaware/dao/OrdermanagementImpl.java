package com.hexaware.dao;

import com.hexaware.entity.*;
import com.hexaware.myexception.OrderNotFoundException;
import com.hexaware.myexception.UserNotFoundException;
import com.hexaware.util.DBConnection;

import java.sql.*;
import java.util.*;

public class OrdermanagementImpl implements Ordermanagement {

    private Connection conn;

    public OrdermanagementImpl()
    {
        this.conn = DBConnection.getConnection();
    }

    @Override
    public void createUser(User user) throws UserNotFoundException {
        try {
    	String sql = "INSERT INTO Users (userId, username, password, role) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, user.getUserId());
        stmt.setString(2, user.getUsername());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getRole());
        stmt.executeUpdate();
    }catch (SQLException e) //sql exception in built
        {
        e.printStackTrace();
    }
    return;
    }
    @Override
    public void createProduct(User user, Product product) throws SQLException {
        if (!user.getRole().equalsIgnoreCase("Admin")) {
            throw new SQLException("Only admin can create products.");
        }
        String sql = "INSERT INTO Product (productId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, product.getProductId());
        stmt.setString(2, product.getProductName());
        stmt.setString(3, product.getDescription());
        stmt.setDouble(4, product.getPrice());
        stmt.setInt(5, product.getQuantityInStock());
        stmt.setString(6, product.getType());
        stmt.executeUpdate();

        // Insert into subclass tables
        if (product instanceof Electronics) {
            Electronics e = (Electronics) product;
            PreparedStatement subStmt = conn.prepareStatement("INSERT INTO Electronics (productId, brand, warrantyPeriod) VALUES (?, ?, ?)");
            subStmt.setInt(1, e.getProductId());
            subStmt.setString(2, e.getBrand());
            subStmt.setInt(3, e.getWarrantyPeriod());
            subStmt.executeUpdate();
        } else if (product instanceof Clothing) {
            Clothing c = (Clothing) product;
            PreparedStatement subStmt = conn.prepareStatement("INSERT INTO Clothing (productId, size, color) VALUES (?, ?, ?)");
            subStmt.setInt(1, c.getProductId());
            subStmt.setString(2, c.getSize());
            subStmt.setString(3, c.getColor());
            subStmt.executeUpdate();
        }
    }

    @Override
    public void createOrder(User user, List<Product> products) throws OrderNotFoundException {
        
        try {
            PreparedStatement orderStmt = conn.prepareStatement("INSERT INTO Orders (userId) VALUES (?)",Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, user.getUserId());
            orderStmt.executeUpdate();
            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId=-1;
            if (rs.next()) {
            	orderId=rs.getInt(1);
            }
            if (orderId == -1) {
                throw new OrderNotFoundException("Failed to generate order ID.");
            }
            PreparedStatement detailStmt = conn.prepareStatement("INSERT INTO OrderDetails (orderId, productId, quantity) VALUES (?, ?, ?)");
            for (Product p : products) {
                detailStmt.setInt(1, orderId);
                detailStmt.setInt(2, p.getProductId());
                detailStmt.setInt(3, 1); // default quantity = 1
                detailStmt.addBatch();
            }
            detailStmt.executeBatch();
        } catch (SQLException ex) {
            
            ex.printStackTrace();
        }
        return;
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws SQLException,UserNotFoundException,OrderNotFoundException {
        String checkSQL = "SELECT * FROM Orders WHERE orderId = ? AND userId = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
        checkStmt.setInt(1, orderId);
        checkStmt.setInt(2, userId);
        ResultSet rs = checkStmt.executeQuery();
        if (!rs.next()) {
            throw new SQLException("User or Order not found.");
        }
        PreparedStatement deleteDetails = conn.prepareStatement("DELETE FROM OrderDetails WHERE orderId = ?");
        deleteDetails.setInt(1, orderId);
        deleteDetails.executeUpdate();
        PreparedStatement deleteOrder = conn.prepareStatement("DELETE FROM Orders WHERE orderId = ?");
        deleteOrder.setInt(1, orderId);
        deleteOrder.executeUpdate();
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
        while (rs.next()) {
            Product p = new Product(
                rs.getInt("productId"),
                rs.getString("productName"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantityInStock"),
                rs.getString("type")
            );
            products.add(p);
        }
        return products;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.* FROM Product p JOIN OrderDetails od ON p.productId = od.productId " +
                     "JOIN Orders o ON o.orderId = od.orderId WHERE o.userId = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, user.getUserId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Product p = new Product(
                rs.getInt("productId"),
                rs.getString("productName"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("quantityInStock"),
                rs.getString("type")
            );
            products.add(p);
        }
        return products;
    }
}
