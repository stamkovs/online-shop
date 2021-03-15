package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private int quantity;
  private boolean isWishlisted;
}
