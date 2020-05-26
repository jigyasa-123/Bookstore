package com.netent.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.json.JsonObject;
import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.repository.BookstoreRepository;

@Service
public class BookstoreService {

  @Autowired
  BookstoreRepository repository;

  public String addBook(BookMapping mapping) {
    String id = mapping.getIsbn();
    JsonObject bookInfo = JsonObject.create().put("price", mapping.getPrice())
        .put("author", mapping.getAuthor()).put("title", mapping.getTitle());
    return repository.addBook(id, bookInfo).toString();
  }

  
  public String buyBook(String id) {
    return repository.buyBook(id).toString();
  }
}
