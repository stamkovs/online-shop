package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.ProductDto;
import com.stamkovs.online.shop.rest.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for wishlist operations.
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
@Slf4j
public class WishlistController {

  private final WishlistService wishlistService;

  /**
   * Add a product to the wishlist persistance table for the authenticated user.
   * @param productId the product id.
   *
   */
  @PutMapping(value = "/add-to-wishlist")
  public void addProductToWishlist(@RequestBody String productId) {
    wishlistService.addProductToWishlist(productId);
  }

  /**
   * Retrieves all products in wishlist for the authenticated user.
   *
   * @return List of {@link ProductDto}.
   */
  @GetMapping(value = "/get-wishlist")
  public List<ProductDto> getProductsInWishlist() {
    return wishlistService.getProductsInWishList();
  }

  /**
   * Delete product from wishlist by product id for an authenticated user.
   * @param productId the product id.
   */
  @DeleteMapping(value = "/delete-from-wishlist/{productId}")
  public void deleteProductFromWishlistById(@PathVariable String productId) {
    wishlistService.deleteProductFromWishlistById(productId);
  }
}
