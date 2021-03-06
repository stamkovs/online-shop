package com.stamkovs.online.shop.rest.converter;

import com.stamkovs.online.shop.rest.model.Product;
import com.stamkovs.online.shop.rest.model.ProductCategory;
import com.stamkovs.online.shop.rest.model.ProductDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

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
      .build();
  }

  /**
   * Converts domain {@link Product} to {@link ProductDto} for product details presentation model.
   *
   * @param product the product persistence data.
   * @return {@link ProductDto}.
   */
  public ProductDto toPresentationModelProductDetails(Product product, Integer totalQuantity,
                                                      Map<String, Map<String, String>> sizeQuantityInfo) {
    return ProductDto.builder()
      .id(product.getId())
      .category(product.getProductCategory().getCategory())
      .name(product.getName())
      .imageSrc(product.getImageUrl())
      .price(product.getPrice())
      .description(product.getDescription())
      .totalQuantity(totalQuantity)
      .sizeQuantityInfo(sizeQuantityInfo)
      .build();
  }

  /**
   * Converts domain {@link ProductDto} to {@link Product} persistance model.
   * @param productDto the product presentational data.
   * @return {@link ProductDto}.
   */
  public Product toPersistenceModel(ProductDto productDto, ProductCategory productCategory, Long userAccountId) {
    return Product.builder()
      .id(productDto.getId())
      .productCategory(productCategory)
      .userAccountId(String.valueOf(userAccountId))
      .name(productDto.getName())
      .description(productDto.getDescription())
      .imageUrl(productDto.getImageSrc())
      .price((float) productDto.getPrice())
      .createdOn(Date.from(Instant.now()))
      .updatedOn(null)
      .build();
  }
}
