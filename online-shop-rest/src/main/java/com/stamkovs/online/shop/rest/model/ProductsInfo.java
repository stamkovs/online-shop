package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Presentation model for the products data that is sent to the client-side.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsInfo {

  private int numberOfProducts;
  private List<ProductDto> products;
}
