package com.example.products.controller;

import com.example.products.model.Products;
import com.example.products.service.IProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private IProductsService productsService;

    @GetMapping
    public String getProductList(Model model) {
        List<Products> products = productsService.findAll();
        model.addAttribute("products", products);
        return "/index";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Products());
        return "/create";
    }

    @PostMapping("/save")
    public String createProduct(Products products) {
        productsService.add(products);
        return "redirect:/products";
    }

}
