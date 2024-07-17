package com.example.p1_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class HandleExceptions {

	@ExceptionHandler(value = { AccountNotFoundException.class })
	protected ResponseEntity<?> acountNotFoundException(Exception e) {
		return ResponseEntity.status(404).body(e.getMessage());
	}

	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<?> illegalArgumentException(Exception e) {
		return ResponseEntity.status(401).body(e.getMessage());
	}

	@ExceptionHandler(value = { KeyAlreadyExistsException.class })
	protected ResponseEntity<?> keyAlreadyExistsException(Exception e) {
		return ResponseEntity.status(401).body(e.getMessage());
	}

	@ExceptionHandler(value = { NoSuchElementException.class })
	protected ResponseEntity<?> noSuchElementException(Exception e) {
		return ResponseEntity.status(404).body(e.getMessage());
	}

}
