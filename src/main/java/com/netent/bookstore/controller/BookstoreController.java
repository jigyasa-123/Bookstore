package com.netent.bookstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.service.BookstoreService;


@RestController
@Valid
@RequestMapping("/books")
public class BookstoreController {

  @Autowired
  BookstoreService service;

  
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addBook(@Valid @RequestBody BookMapping mapping){
    return new ResponseEntity<String>(service.addBook(mapping),HttpStatus.CREATED);
  }
} 
