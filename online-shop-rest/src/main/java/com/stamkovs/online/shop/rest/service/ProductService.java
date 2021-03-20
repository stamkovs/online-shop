package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.converter.ProductConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.Product;
import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.model.ProductSizeQuantity;
import com.stamkovs.online.shop.rest.model.ProductsInfo;
import com.stamkovs.online.shop.rest.model.Wishlist;
import com.stamkovs.online.shop.rest.repository.ProductRepository;
import com.stamkovs.online.shop.rest.repository.ProductSizeAndQuantityRepository;
import com.stamkovs.online.shop.rest.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for various products operations.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

  private static final String MEN_SNEAKERS = "men_sneakers";
  private static final String WATCHES = "watches";
  private final ProductRepository productRepository;
  private final ProductSizeAndQuantityRepository productSizeQuantityRepository;
  private final ProductConverter productConverter;
  private final WishlistRepository wishlistRepository;
  private final CustomUserDetailsService customUserDetailsService;

  /**
   * Gets all products from db.
   *
   * @return {@link ProductsInfo}.
   */
  public ProductsInfo findAllProducts() {
    log.info("Retrieving all products");
    List<ProductDto> productDtoList;
    try {
      Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
      log.info("User is logged in. Will retrieve wishlist details for userAccountId {}", userAccountId);
      productDtoList = productRepository.findAll().stream().map(product -> {
        ProductDto productDto = productConverter.toPresentationModel(product);
        Optional<Wishlist> wishlist = wishlistRepository.findByUserAccountIdAndProductId(userAccountId,
          product.getId());
        productDto.setWishlisted(wishlist.isPresent());
        return productDto;
      }).collect(Collectors.toList());
    } catch (UnauthorizedShoptasticException | UserNotFoundException e) {
      log.info("Anonymous user. Products will be shown without wishlist details");
      productDtoList =
        productRepository.findAll().stream().map(productConverter::toPresentationModel).collect(Collectors.toList());
    }

    ProductsInfo productsInfo = new ProductsInfo();
    productsInfo.setNumberOfProducts(productDtoList.size());
    productsInfo.setProducts(productDtoList);

    return productsInfo;
  }

  /**
   * Gets all products per category.
   *
   * @param category the product category.
   * @return {@link ProductsInfo}.
   */
  public ProductsInfo findAllProductsByCategory(String category) {
    log.info("Retrieving all products for category {}", category);
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

  /**
   * Get product details by id.
   *
   * @param id the product id.
   * @return {@link Product}.
   */
  public ProductDto findById(Long id) {
    log.info("Retrieving details for product with id {}", id);
    List<ProductSizeQuantity> productSizeQuantityList = productSizeQuantityRepository.findByProductId(id);
    ProductDto productDto = new ProductDto();
    if (!productSizeQuantityList.isEmpty()) {
      Product product = productSizeQuantityList.get(0).getProduct();
      Map<String, Map<String, String>> productSizeAndQuantityInfo = new HashMap<>();
      Map<String, String> sizeQuantityPair = new HashMap<>();
      AtomicInteger quantity = new AtomicInteger();
      productSizeQuantityList.forEach(productSizeQuantity -> {
        quantity.addAndGet(productSizeQuantity.getQuantity());
        sizeQuantityPair.put(productSizeQuantity.getSize(), productSizeQuantity.getQuantity().toString());
      });
      productSizeAndQuantityInfo.put("size", sizeQuantityPair);

      Integer totalQuantity = quantity.get();
      productDto = productConverter.toPresentationModelProductDetails(product, totalQuantity,
        productSizeAndQuantityInfo);
      try {
        Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
        log.info("Retrieving wishlist for userAccountId: {}, and productId: {}", userAccountId, id);
        Optional<Wishlist> optionalWishlistProduct = wishlistRepository.findByUserAccountIdAndProductId(userAccountId,
          id);
        if (optionalWishlistProduct.isPresent()) {
          productDto.setWishlisted(true);
        }
      } catch (UnauthorizedShoptasticException | UserNotFoundException e) {
        log.info("Anonymous user. Product details will be shown without option to add it to wishlist");
      }

    }
    return productDto;
  }

  /**
   * Get newest products for the categories men_sneakers and watches.
   *
   * @return List of {@link ProductDto}.
   */
  public List<ProductDto> findNewestProducts() {
    log.info("Retrieving newest products");
    Pageable limit = PageRequest.of(0, 3);
    Page<Product> productPageMenSneakers = productRepository.findAllByProductCategoryCategoryOrderByCreatedOnDesc(
      MEN_SNEAKERS
      , limit);
    Page<Product> productPageWatches = productRepository.findAllByProductCategoryCategoryOrderByCreatedOnDesc(
      WATCHES, limit);
    List<Product> productListMenSneakers = productPageMenSneakers.getContent();
    List<Product> productListWatches = productPageWatches.getContent();

    List<Product> newestProducts = Stream.concat(productListMenSneakers.stream(), productListWatches.stream())
      .collect(Collectors.toList());
    List<ProductDto> productDtoList =
      newestProducts.stream().map((productConverter::toPresentationModel)).distinct().collect(Collectors.toList());
    try {
      Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
      log.info("Products will be shown with wishlist details for userAccountId {}", userAccountId);
      productDtoList.forEach(productDto -> {
        Optional<Wishlist> optionalWishlistProduct = wishlistRepository.findByUserAccountIdAndProductId(userAccountId,
          productDto.getId());
        if (optionalWishlistProduct.isPresent()) {
          productDto.setWishlisted(true);
        }
      });
    } catch (UnauthorizedShoptasticException | UserNotFoundException e) {
      log.info("Anonymous user. Products will be shown without wishlist details");
    }
    return productDtoList;
  }


  /**
   * Search products by search value
   *
   * @param searchValue the user input search value.
   * @return List of {@link ProductDto}.
   */
  public List<ProductDto> searchProducts(String searchValue) {
    log.info("Searching products by value: [{}]", searchValue);
    List<Product> productsFromSearch = productRepository.findByNameContaining(searchValue);
    List<Product> categoriesFromSearch = productRepository.findByProductCategoryCategoryContaining(searchValue);

    List<Product> searchResult = Stream.concat(productsFromSearch.stream(), categoriesFromSearch.stream())
      .collect(Collectors.toList());

    return searchResult.stream().map((productConverter::toPresentationModel)).distinct().collect(Collectors.toList());

  }

}
