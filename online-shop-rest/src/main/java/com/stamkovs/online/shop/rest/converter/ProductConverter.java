package com.stamkovs.online.shop.rest.converter;

import com.stamkovs.online.shop.rest.model.Product;
import com.stamkovs.online.shop.rest.model.ProductDto;
import org.springframework.stereotype.Component;

/**
 * Converter for products.
 */
@Component
public class ProductConverter {

  /**
   * Converts domain {@link Product} to {@link ProductDto} presentation model.
   * @param product the product persistence data.
   * @return {@link ProductDto}.
   */
  public ProductDto toPresentationModel(Product product) {
    return ProductDto.builder()
      .id(product.getId())
      .category(product.getProductCategory().getCategory())
      .name(product.getName())
      .imageSrc(product.getImageUrl())
      .price(product.getPrice())
      .description(product.getDescription())
      .quantity(product.getQuantity())
      .build();
  }
}
