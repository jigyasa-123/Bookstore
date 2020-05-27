package com.netent.bookstore.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.service.BookstoreService;


@RestController
@Validated
@RequestMapping("/books")
public class BookstoreController {

  @Autowired
  BookstoreService service;

  
  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addBook(@Valid @RequestBody BookMapping mapping){
    return new ResponseEntity<>(service.addBook(mapping),HttpStatus.CREATED);
  }
  
  @GetMapping(value = "/buy/{isbn}",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> buyBook(@PathVariable final String isbn){
    return new ResponseEntity<>(service.buyBook(isbn),HttpStatus.OK);
  }
  
  @GetMapping(value = "/mediacoverage/{isbn}",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Set<String>> searchBookMediaCoverage(@PathVariable final String isbn){
    return new ResponseEntity<>(service.searchMediaCoverage(isbn),HttpStatus.OK);
  }
  
  @GetMapping(value = "/{isbn}",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByIsbn(@PathVariable final String isbn){
    return new ResponseEntity<>(service.getBookById(isbn),HttpStatus.OK);
  }
  
  @GetMapping(value = "/title",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByTitle(@RequestParam final String name){
    return new ResponseEntity<>(service.getBookByTitle(name),HttpStatus.OK);
  }
  
  
  @GetMapping(value = "/author",  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByAuthor(@RequestParam final String name){
    return new ResponseEntity<>(service.getBookByAuthor(name),HttpStatus.OK);
  }
} 
