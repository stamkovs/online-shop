package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller for registering and confirming accounts.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AccountRegisterController {

  private final RegisterService registerService;

  @PostMapping(value = "/register")
  public ResponseEntity<Object> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
    registerService.registerUser(userRegisterDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(value = "/confirm-account")
  public UserRegisterDto confirmUserAccount(HttpServletResponse response, @RequestParam String confirmationToken) {
    return registerService.getUserDetailsByConfirmationToken(response, confirmationToken);
  }

}
