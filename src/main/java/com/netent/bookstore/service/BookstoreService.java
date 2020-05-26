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
    JsonObject bookInfo = JsonObject.create().put("price", mapping.getPrice())
        .put("author", mapping.getAuthor()).put("title", mapping.getTitle());
    return repository.addBook(id, bookInfo).toString();
  }

  public String buyBook(String id) {
    return repository.buyBook(id).toString();

  }

  public Set<String> searchMediaCoverage(String id) {
    String response = externalCallservice.callMediaCoverageAPI();
    String title = repository.getBookByKey(id, "title");
    JSONArray jsonArray = new JSONArray(response);
    CompletableFuture<Set<String>> titleMatchList =
        CompletableFuture.supplyAsync(
            () -> commonUtil.searchInKey(jsonArray, "title", title), asyncTitleExecutor);
    CompletableFuture<Set<String>> bodyMatchList = CompletableFuture
        .supplyAsync(() -> commonUtil.searchInKey(jsonArray, "body", title),asyncBodyExecutor);
    // wait for all the async threads
    CompletableFuture.allOf(titleMatchList, bodyMatchList);
    return Stream
        .concat(titleMatchList.join().stream(), bodyMatchList.join().stream())
        .collect(Collectors.toSet());

  }


}
