package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.EmailDto;
import com.stamkovs.online.shop.rest.model.ResetPasswordDto;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.service.RegisterService;
import com.stamkovs.online.shop.rest.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for registering and confirming accounts.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AccountController {

  private final RegisterService registerService;
  private final ResetPasswordService resetPasswordService;

  /**
   * Starts the registration process for user, which creates a token and sends an email for account confirmation.
   *
   * @param userRegisterDto {@link UserRegisterDto}.
   *
   * @return {@link ResponseEntity}.
   */
  @PostMapping(value = "/register")
  public ResponseEntity<Object> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
    registerService.registerUser(userRegisterDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Confirms the user account by validating the confirmation token the user clicked from his email.
   *
   * @param response {@link HttpServletResponse}.
   * @param confirmationToken the confirmation token.
   *
   * @return {@link UserRegisterDto}.
   */
  @GetMapping(value = "/confirm-account")
  public UserRegisterDto confirmUserAccount(HttpServletRequest request, HttpServletResponse response, @RequestParam String confirmationToken) {
    return registerService.getUserDetailsByConfirmationToken(request, response, confirmationToken);
  }

  /**
   * Starts the reset password process for user, which creates a token and sends an email for password reset.
   *
   * @param emailDto {@link EmailDto}.
   *
   * @return {@link ResponseEntity}.
   */
  @PostMapping(value = "/forgot-password")
  public ResponseEntity<Object> resetPassword(@RequestBody EmailDto emailDto) {
    resetPasswordService.resetPassword(emailDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Checks the if the reset password token is valid.
   *
   * @param resetPasswordToken the reset password token.
   *
   * @return {@link ResetPasswordDto}.
   */
  @GetMapping(value = "/check-reset-password-token-validity")
  public ResetPasswordDto checkResetPasswordTokenValidity(@RequestParam String resetPasswordToken) {
    return resetPasswordService.getUserDetailsByResetPasswordToken(resetPasswordToken);
  }

  /**
   * Updates the user account password.
   *
   * @param response {@link HttpServletResponse}.
   * @param resetPasswordDto {@link ResetPasswordDto}.
   * @param resetPasswordToken the reset password token.
   */
  @PutMapping(value = "/update-password")
  public ResponseEntity<Object> updatePassword(HttpServletRequest request, HttpServletResponse response,
                                               @RequestBody ResetPasswordDto resetPasswordDto,
                                               @RequestParam String resetPasswordToken) {
    resetPasswordService.updatePassword(request, response, resetPasswordToken, resetPasswordDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
