package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAll();

  List<Product> findAllByProductCategoryCategory(String category);

}