package com.example.products.service;

import com.example.products.model.Products;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsService implements IProductsService {

    private static final String URLDB = "jdbc:mysql://localhost:3306/store";
    private static final String USERDB = "root";
    private static final String PASSWORD = "123456";

    private static final String INSERT_PRODUCTS_SQL = "INSERT INTO products (name, price, color) VALUES (?, ?, ?);";
    private static final String SELECT_PRODUCTS_BY_ID = "select id,name,price,color from products where id =?";
    private static final String SELECT_ALL_PRODUCTS = "select * from products";
    private static final String DELETE_PRODUCTS_SQL = "delete from products where id = ?;";
    private static final String UPDATE_PRODUCTS_SQL = "update products set name = ?,price= ?, color =? where id = ?;";


    private Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URLDB, USERDB, PASSWORD);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Products> findAll() {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Products> products = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = Double.parseDouble(resultSet.getString("price"));
                String color = resultSet.getString("color");
                Products product = new Products(id, name, price, color);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Products products) {
        System.out.println(INSERT_PRODUCTS_SQL);
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCTS_SQL)) {
            preparedStatement.setString(1, products.getName());
            preparedStatement.setDouble(2, products.getPrice());
            preparedStatement.setString(3, products.getColor());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public Products getProductById(int productId) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCTS_BY_ID);
        statement.setInt(1, productId);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return new Products(

                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getString("color")
            );
        }
        return null;
    }


    @Override
    public boolean update(Products products) {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCTS_SQL);) {
            statement.setString(1, products.getName());
            statement.setDouble(2, products.getPrice());
            statement.setString(3, products.getColor());
            statement.setInt(4, products.getId());

            rowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowUpdated;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCTS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
