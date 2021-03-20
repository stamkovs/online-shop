package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Presentation model for the products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private Long id;
  private String category;
  private String name;
  private String description;
  private String imageSrc;
  private double price;
  private Integer totalQuantity;
  private Map<String, Map<String, String>> sizeQuantityInfo;
  private boolean isWishlisted;
}
