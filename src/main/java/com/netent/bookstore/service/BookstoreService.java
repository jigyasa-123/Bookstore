package com.netent.bookstore.service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.json.JsonObject;
import com.google.gson.Gson;
import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.repository.BookstoreRepository;
import com.netent.bookstore.util.CommonUtil;
import static com.netent.bookstore.constants.CommonConstants.PRICE;
import static com.netent.bookstore.constants.CommonConstants.TITLE;
import static com.netent.bookstore.constants.CommonConstants.AUTHOR;
import static com.netent.bookstore.constants.CommonConstants.BODY;
import static com.netent.bookstore.constants.CommonConstants.ISBN;




@Service
public class BookstoreService {

  @Autowired
  private BookstoreRepository repository;

  @Autowired
  private ExternalCallService externalCallservice;

  @Autowired
  @Qualifier("asyncTitleExecutor")
  private  Executor asyncTitleExecutor;

  @Autowired
  @Qualifier("asyncBodyExecutor")
  private Executor asyncBodyExecutor;
  
  @Autowired
  private CommonUtil commonUtil;
  

  Gson gson = new Gson();

  public String addBook(BookMapping mapping) {
    String id = mapping.getIsbn();
    JsonObject bookInfo = JsonObject.create().put(ISBN, mapping.getIsbn()).put(PRICE, mapping.getPrice())
        .put(AUTHOR, mapping.getAuthor()).put(TITLE, mapping.getTitle());
    return repository.addBook(id, bookInfo).toString();
  }

  public String buyBook(String isbn) {
    return repository.buyBook(isbn).toString();

  }

  public Set<String> searchMediaCoverage(String isbn) {
    String response = externalCallservice.callMediaCoverageAPI();
    String title = repository.getBookKey(isbn, TITLE);
    JSONArray jsonArray = new JSONArray(response);
    CompletableFuture<Set<String>> titleMatchList =
        CompletableFuture.supplyAsync(
            () -> commonUtil.searchInKey(jsonArray, TITLE, title), asyncTitleExecutor);
    CompletableFuture<Set<String>> bodyMatchList = CompletableFuture
        .supplyAsync(() -> commonUtil.searchInKey(jsonArray,BODY , title),asyncBodyExecutor);
    // wait for all the async threads
    CompletableFuture.allOf(titleMatchList, bodyMatchList);
    return Stream
        .concat(titleMatchList.join().stream(), bodyMatchList.join().stream())
        .collect(Collectors.toSet());

  }
  
  public String getBookById(String isbn) {
    return repository.getBookById(isbn).toString();
  }
  
  public String getBookByTitle(String title) {
   return repository.getBookByTitle(title).toString();
  }
  
  public String getBookByAuthor(String author) {
    return repository.getBookByAuthor(author).toString();
  }


}
