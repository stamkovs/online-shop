package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.converter.ProductConverter;
import com.stamkovs.online.shop.rest.model.Product;
import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.model.Wishlist;
import com.stamkovs.online.shop.rest.repository.ProductRepository;
import com.stamkovs.online.shop.rest.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for wishlist page.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final ProductConverter productConverter;
  private final ProductRepository productRepository;
  private final CustomUserDetailsService customUserDetailsService;

  public void addProductToWishlist(String productId) {
    Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
    log.info("Adding product to wishlist, [userAccountId = {}, productId = {}]", userAccountId, productId);
    Wishlist wishlist = new Wishlist();
    wishlist.setUserAccountId(userAccountId);
    wishlist.setProductId(Long.valueOf(productId));
    wishlist.setAddedAt(Date.from(Instant.now()));
    wishlistRepository.save(wishlist);
    log.info("Successfully added product to wishlist, [userAccountId = {}, productId = {}]", userAccountId, productId);
  }

  public List<ProductDto> getProductsInWishList() {
    Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
    log.info("Retrieving products from wishlist, [userAccountId = {}]", userAccountId);
    List<Wishlist> wishlistList =
      wishlistRepository.findAllByUserAccountId(userAccountId);
    return wishlistList.stream().map(wishlist -> {
      Optional<Product> product = productRepository.findById(wishlist.getProductId());
      ProductDto productDto = new ProductDto();
      if (product.isPresent()) {
        productDto = productConverter.toPresentationModel(product.get());
        productDto.setWishlisted(true);
      }
      return productDto;
    }).collect(Collectors.toList());
  }

  public void deleteProductFromWishlistById(String productId) {
    Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
    log.info("Deleting product from wishlist, [userAccountId = {}, productId = {}]", userAccountId, productId);
    wishlistRepository.deleteByUserAccountIdAndProductId(userAccountId, Long.valueOf(productId));
    log.info("Successfully deleted product from wishlist, [userAccountId = {}, productId = {}]", userAccountId,
      productId);
  }

  public void deleteAllProductsFromWishlistById() {
    Long userAccountId = customUserDetailsService.getUserAccountIdFromAuthentication();
    log.info("Deleting all products from wishlist, [userAccountId = {}]", userAccountId);
    wishlistRepository.deleteByUserAccountId(userAccountId);
    log.info("Successfully deleted all products from wishlist, [userAccountId = {}]", userAccountId);
  }
}
