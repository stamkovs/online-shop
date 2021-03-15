package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.converter.ProductConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.model.*;
import com.stamkovs.online.shop.rest.repository.ProductRepository;
import com.stamkovs.online.shop.rest.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link ProductService}
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;
  @Mock
  private ProductConverter productConverter;
  @Mock
  private WishlistRepository wishlistRepository;
  @Mock
  private CustomUserDetailsService customUserDetailsService;

  @Test
  void shouldGetAllProductsWithWishlistDetailsForLoggedInUser() {
    // given
    Product product1 = new Product();
    Product product2 = new Product();
    List<Product> products = Arrays.asList(product1, product2);
    when(productRepository.findAll()).thenReturn(products);
    when(productConverter.toPresentationModel(any())).thenReturn(new ProductDto());
    Wishlist wishlist = new Wishlist();
    Optional<Wishlist> optionalWishlist = Optional.of(wishlist);
    when(wishlistRepository.findByUserAccountIdAndProductId(any(), any())).thenReturn(optionalWishlist);

    // when
    ProductsInfo productsInfo = productService.findAllProducts();

    // then
    assertThat(productsInfo, is(notNullValue()));
    assertThat(productsInfo.getNumberOfProducts(), is(2));
    assertThat(productsInfo.getProducts().size(), is(2));
  }

  @Test
  void shouldGetAllProductsWithoutWishlistDetailsForAnonymousUser() {
    // given
    Product product1 = new Product();
    Product product2 = new Product();
    List<Product> products = Arrays.asList(product1, product2);
    when(productRepository.findAll()).thenReturn(products);
    when(productConverter.toPresentationModel(any())).thenReturn(new ProductDto());
    when(customUserDetailsService.getUserAccountIdFromAuthentication())
      .thenThrow(new UnauthorizedShoptasticException("exception"));

    // when
    ProductsInfo productsInfo = productService.findAllProducts();

    // then
    assertThat(productsInfo, is(notNullValue()));
    assertThat(productsInfo.getNumberOfProducts(), is(2));
    assertThat(productsInfo.getProducts().size(), is(2));
    verify(wishlistRepository, times(0)).findByUserAccountIdAndProductId(any(), any());
  }
}