package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.ProductSizeQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product size and quantity repository.
 */
@Repository
public interface ProductSizeAndQuantityRepository extends JpaRepository<ProductSizeQuantity, Long> {

  List<ProductSizeQuantity> findAll();

  List<ProductSizeQuantity> findByProductId(Long productId);

}