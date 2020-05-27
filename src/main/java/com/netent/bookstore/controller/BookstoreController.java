package com.netent.bookstore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Represents the Controller layer to expose the rest endpoints.
 * @author jgarg
 *
 */
@RestController
@Validated
@RequestMapping("/books")
public class BookstoreController {

  @Autowired
  BookstoreService service;

  /**
   * Add a book
   * @param mapping
   * @return book doc
   */
  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addBook(
    @Valid @RequestBody BookMapping mapping) {
    try {
      return new ResponseEntity<>(service.addBook(mapping), HttpStatus.CREATED);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Buy a book
   * @param isbn
   * @return book doc
   */
  @GetMapping(value = "/buy/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> buyBook(@PathVariable final String isbn) {
    try {
      return new ResponseEntity<>(service.buyBook(isbn), HttpStatus.OK);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Search book media coverage
   * @param isbn
   * @return list of titles
   */
  @GetMapping(value = "/mediacoverage/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchBookMediaCoverage(
    @PathVariable final String isbn) {
    try {
      return new ResponseEntity<>(service.searchMediaCoverage(isbn),
          HttpStatus.OK);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Search book by isbn
   * @param isbn
   * @return book doc
   */
  @GetMapping(value = "/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByIsbn(@PathVariable final String isbn) {
    try {
      return new ResponseEntity<>(service.getBookById(isbn), HttpStatus.OK);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Search book by title
   * @param name
   * @return book doc
   */
  @GetMapping(value = "/title", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByTitle(@RequestParam final String name) {
    try {
      return new ResponseEntity<>(service.getBookByTitle(name), HttpStatus.OK);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Search book by author
   * @param name
   * @return book doc
   */
  @GetMapping(value = "/author", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchByAuthor(
    @RequestParam final String name) {
    try {
      return new ResponseEntity<>(service.getBookByAuthor(name), HttpStatus.OK);
    } catch (Exception e) {
      throw e;
    }
  }
}
