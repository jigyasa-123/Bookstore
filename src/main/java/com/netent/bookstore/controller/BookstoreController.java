package com.netent.bookstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.netent.bookstore.repository.BookstoreRepository;

@RestController
public class BookstoreController {

  @Autowired
  BookstoreRepository repository;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> test() {
    return new ResponseEntity<String>(repository.getBooks("1"),
        HttpStatus.ACCEPTED);
  }

}
