package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.model.ProductsInfo;
import com.stamkovs.online.shop.rest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  /**
   * Get product by id.
   * @param id the id
   *
   * @return {@link ProductDto}.
   */
  @GetMapping("/product/{id}")
  public ProductDto getProductById(@PathVariable Long id) {
    return productService.findById(id);
  }

  /**
   * Get newest products.
   *
   * @return {@link ProductsInfo}.
   */
  @GetMapping("/products/newest")
  public List<ProductDto> getNewestProducts() {
    return productService.findNewestProducts();
  }

  @GetMapping("/products/search")
  public List<ProductDto> searchProducts(@RequestParam String searchValue) {
    return productService.searchProducts(searchValue);
  }

  /**
   * Add new product.
   *
   * @param productDto {@link ProductDto}.
   */
  @PostMapping("/products/add")
  public ProductDto addProduct(@RequestBody ProductDto productDto) {
   return productService.addProduct(productDto);
  }

}
