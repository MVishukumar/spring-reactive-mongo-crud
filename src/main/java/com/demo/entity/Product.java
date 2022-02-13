package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// for lombok
@Data
@AllArgsConstructor
@NoArgsConstructor

// for mongodb
@Document(collection = "Products")
public class Product {

    @Id
    private String id;
    private String name;
    private int quantity;
    private double price;
}
