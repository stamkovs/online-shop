package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link WishlistService}
 */
@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

  @InjectMocks
  private WishlistService wishlistService;

  @Mock
  private WishlistRepository wishlistRepository;

  @Mock
  private CustomUserDetailsService customUserDetailsService;

  @Test
  void shouldAddProductToWishlistIfUserIsAuthenticated() {
    // given

    // when
    wishlistService.addProductToWishlist("1");

    // then
    verify(wishlistRepository).save(any());
  }

  @Test
  void shouldNotAddProductToWishlistIfUserIsAnonymous() {
    // given
    when(customUserDetailsService.getUserAccountIdFromAuthentication())
      .thenThrow(new UnauthorizedShoptasticException("exception"));

    // when
    assertThrows(UnauthorizedShoptasticException.class, () -> wishlistService.addProductToWishlist("1"));

    // then
    verify(wishlistRepository, times(0)).save(any());
  }

  @Test
  void shouldDeleteProductFromWishlistIfUserIsAuthenticated() {
    // given

    // when
    wishlistService.deleteProductFromWishlistById("1");

    // then
    verify(wishlistRepository).deleteByUserAccountIdAndProductId(any(), eq(1L));
  }

  @Test
  void shouldNotDeleteProductFromWishlistIfUserIsAnonymous() {
    // given
    when(customUserDetailsService.getUserAccountIdFromAuthentication())
      .thenThrow(new UnauthorizedShoptasticException("exception"));

    // when
    assertThrows(UnauthorizedShoptasticException.class, () -> wishlistService.addProductToWishlist("1"));


    // then
    verify(wishlistRepository, times(0)).deleteByUserAccountIdAndProductId(any(), eq(1L));
  }

  @Test
  void shouldGetProductsInWishList() {
    // ToDO: Implement integration tests with h2 db
  }

}