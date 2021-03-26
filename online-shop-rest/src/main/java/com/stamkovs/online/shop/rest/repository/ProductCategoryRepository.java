package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Product category repository.
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

  Optional<ProductCategory> findById(Integer id);
  Optional<ProductCategory> findByCategory(String category);

}