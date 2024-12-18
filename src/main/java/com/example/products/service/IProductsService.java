package com.example.products.service;

import com.example.products.model.Products;

import java.sql.SQLException;
import java.util.List;

public interface IProductsService {
    List<Products> findAll();

    void add(Products products);

    Products getProductById(int id) throws SQLException;

    boolean update(Products products);

    boolean delete(int id) throws SQLException;
}
