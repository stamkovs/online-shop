package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Product repository.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAll();

  List<Product> findAllByProductCategoryCategory(String category);

  Optional<Product> findById(String id);

  Page<Product> findAllByProductCategoryCategoryOrderByCreatedOnDesc(String category, Pageable limit);

  List<Product> findByNameContaining(String searchValue);

  List<Product> findByProductCategoryCategoryContaining(String searchValue);

}