package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.converter.ProductConverter;
import com.stamkovs.online.shop.rest.model.Product;
import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.model.ProductsInfo;
import com.stamkovs.online.shop.rest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Controller for retrieving products.
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductConverter productConverter;

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
  public ProductDto getProducts(@PathVariable Long id) {
    Optional<Product> optionalProduct = productService.findById(id);
    ProductDto productDto = new ProductDto();
    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      productDto = productConverter.toPresentationModel(product);
    }
    return productDto;
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


}
