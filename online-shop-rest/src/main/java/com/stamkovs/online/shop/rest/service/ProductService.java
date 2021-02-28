package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.converter.ProductConverter;
import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.model.ProductsInfo;
import com.stamkovs.online.shop.rest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for various products operations.
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductConverter productConverter;

  /**
   * Gets all products from db.
   *
   * @return {@link ProductsInfo}.
   */
  public ProductsInfo findAllProducts() {
    List<ProductDto> productDtoList =
      productRepository.findAll().stream().map(productConverter::toPresentationModel).collect(Collectors.toList());

    ProductsInfo productsInfo = new ProductsInfo();
    productsInfo.setNumberOfProducts(productDtoList.size());
    productsInfo.setProducts(productDtoList);

    return productsInfo;
  }

  /**
   * Gets all products per category.
   * @param category the product category.
   *
   * @return {@link ProductsInfo}.
   */
  public ProductsInfo findAllProductsByCategory(String category) {
    if (category.contains("-")) {
      category = category.replace("-", "_");
    }
    List<ProductDto> productDtoList = productRepository.findAllByProductCategoryCategory(category)
      .stream()
      .map(productConverter::toPresentationModel).collect(Collectors.toList());

    ProductsInfo productsInfo = new ProductsInfo();
    productsInfo.setNumberOfProducts(productDtoList.size());
    productsInfo.setProducts(productDtoList);

    return productsInfo;
  }


}
