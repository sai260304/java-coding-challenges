package com.hexaware.main;

import com.hexaware.dao.*;
import com.hexaware.entity.*;
import com.hexaware.myexception.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            OrdermanagementImpl repo = new OrdermanagementImpl();

            while (true) {
                System.out.println("\n==== Order Management System ====");
                System.out.println("1. Create User");
                System.out.println("2. Create Product");
                System.out.println("3. Create Order");
                System.out.println("4. Cancel Order");
                System.out.println("5. View All Products");
                System.out.println("6. View My Orders");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (choice) {
                    case 1: {
                        System.out.print("User ID: ");
                        int uid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Username: ");
                        String uname = sc.nextLine();
                        System.out.print("Password: ");
                        String pass = sc.nextLine();
                        System.out.print("Role (Admin/User): ");
                        String role = sc.nextLine();

                        User u = new User(uid, uname, pass, role);
                        repo.createUser(u);
                        System.out.println("User created successfully!");
                        break;
                    }

                    case 2: {
                        System.out.print("User ID: ");
                        int adminId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Username: ");
                        String adminName = sc.nextLine();
                        User admin = new User(adminId, adminName, "", "Admin");

                        System.out.print("Product ID: ");
                        int pid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Name: ");
                        String pname = sc.nextLine();
                        System.out.print("Description: ");
                        String desc = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Type (Electronics/Clothing): ");
                        String type = sc.nextLine();

                        Product product;
                        if (type.equalsIgnoreCase("Electronics")) {
                            System.out.print("Brand: ");
                            String brand = sc.nextLine();
                            System.out.print("Warranty Period (months): ");
                            int warranty = sc.nextInt();
                            sc.nextLine();
                            product = new Electronics(pid, pname, desc, price, qty, type, brand, warranty);
                        } else if (type.equalsIgnoreCase("Clothing")) {
                            System.out.print("Size: ");
                            String size = sc.nextLine();
                            System.out.print("Color: ");
                            String color = sc.nextLine();
                            product = new Clothing(pid, pname, desc, price, qty, type, size, color);
                        } else {
                            System.out.println("Invalid product type.");
                            break;
                        }

                        repo.createProduct(admin, product);
                        System.out.println("Product created successfully!");
                        break;
                    }

                    case 3: {
                        System.out.print("User ID: ");
                        int uid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Username: ");
                        String uname = sc.nextLine();
                        User user = new User(uid, uname, "", "User");

                        List<Product> selectedProducts = new ArrayList<>();
//                        repo.getAllProducts().forEach(System.out::println);

                        System.out.print("Enter number of products to order: ");
                        int count = sc.nextInt();
                        sc.nextLine();
                        for (int i = 0; i < count; i++) {
                            System.out.print("Enter Product ID: ");
                            int pid = sc.nextInt();
                            sc.nextLine();
                            // Fetch product by ID (simplified here — ideally use a method)
                            for (Product p : repo.getAllProducts()) {
                            	 System.out.println("Available: " + p.getProductId() + " - " + p.getProductName());
                                if (p.getProductId() == pid) {
                                    selectedProducts.add(p);  
                                    break;
                                }
                            }
                        }

                        repo.createOrder(user, selectedProducts);
                        System.out.println("Order placed successfully!");
                        break;
                    }

                    case 4: {
                        System.out.print("User ID: ");
                        int uid = sc.nextInt();
                        System.out.print("Order ID: ");
                        int oid = sc.nextInt();
                        try {
                            repo.cancelOrder(uid, oid);
                            System.out.println("Order cancelled successfully.");
                        } catch (UserNotFoundException | OrderNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }

                    case 5: {
                        List<Product> products = repo.getAllProducts();
                        System.out.println("\n--- All Products ---");
                        for (Product p : products) {
                            System.out.println(p.getProductId() + " | " + p.getProductName() + " | " + p.getType() + " | " + p.getPrice());
                        }
                        break;
                    }

                    case 6: {
                        System.out.print("User ID: ");
                        int uid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Username: ");
                        String uname = sc.nextLine();
                        User user = new User(uid, uname, "", "User");

                        List<Product> orders = repo.getOrderByUser(user);
                        System.out.println("\n--- Your Orders ---");
                        for (Product p : orders) {
                            System.out.println(p.getProductId() + " | " + p.getProductName() + " | " + p.getType());
                        }
                        break;
                    }

                    case 7:
                        System.out.println("Exiting... Goodbye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
