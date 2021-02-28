package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.ProductsInfo;
import com.stamkovs.online.shop.rest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for retrieving products.
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * Get all products.
   *
   * @return {@link ProductsInfo}.
   */
  @GetMapping("/products")
  public ProductsInfo getProducts() {
    return productService.findAllProducts();
  }

  /**
   * Get all products for given category.
   *
   * @param category the product category.
   * @return {@link ProductsInfo}.
   */
  @GetMapping("/products/{category}")
  public ProductsInfo getProductsByCategory(@PathVariable String category) {
    return productService.findAllProductsByCategory(category);
  }

}
