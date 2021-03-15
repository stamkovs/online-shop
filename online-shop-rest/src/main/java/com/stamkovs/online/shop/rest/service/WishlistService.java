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
    Wishlist wishlist = new Wishlist();
    wishlist.setUserAccountId(userAccountId);
    wishlist.setProductId(Long.valueOf(productId));
    wishlist.setAddedAt(Date.from(Instant.now()));
    wishlistRepository.save(wishlist);
  }

  public List<ProductDto> getProductsInWishList() {
    List<Wishlist> wishlistList =
      wishlistRepository.findAllByUserAccountId(customUserDetailsService.getUserAccountIdFromAuthentication());
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
    wishlistRepository.deleteByUserAccountIdAndProductId(userAccountId, Long.valueOf(productId));
  }
}
