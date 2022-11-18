package com.ifuture.accountservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Error {
    private String message;
}
