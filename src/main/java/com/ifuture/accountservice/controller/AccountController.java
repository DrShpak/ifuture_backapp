package com.ifuture.accountservice.controller;

import com.ifuture.accountservice.model.Account;
import com.ifuture.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Long> getAmount(@PathVariable Integer id) throws EntityNotFoundException {
        return new ResponseEntity<>(service.getAmount(id), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<String> addAmount(@RequestBody Account account) {
        service.addAmount(account.getId(), account.getValue());
        return new ResponseEntity<>("Successfully changed", HttpStatus.OK);
    }
}
