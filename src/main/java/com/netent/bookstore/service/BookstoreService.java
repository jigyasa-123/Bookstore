package com.netent.bookstore.service;

import static com.netent.bookstore.constants.CommonConstants.AUTHOR;
import static com.netent.bookstore.constants.CommonConstants.BODY;
import static com.netent.bookstore.constants.CommonConstants.ISBN;
import static com.netent.bookstore.constants.CommonConstants.PRICE;
import static com.netent.bookstore.constants.CommonConstants.TITLE;

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
import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.repository.BookstoreRepositoryTest;
import com.netent.bookstore.util.CommonUtil;




/**
 * Service class to call repository and perform additional operations
 * @author jgarg
 *
 */
@Service
public class BookstoreService {

  @Autowired
  private BookstoreRepositoryTest repository;

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
  


  /**
   * Add  a book
   * @param mapping book info mapping
   * @return book doc
   */
  public String addBook(BookMapping mapping) {
    String id = mapping.getIsbn();
    JsonObject bookInfo = JsonObject.create().put(ISBN, mapping.getIsbn()).put(PRICE, mapping.getPrice())
        .put(AUTHOR, mapping.getAuthor()).put(TITLE, mapping.getTitle());
    return repository.addBook(id, bookInfo).toString();
  }

  /**
   * Buy a book
   * Throw exception if does not exist
   * @param isbn book isbn no
   * @return book doc
   */
  public String buyBook(String isbn) {
    return repository.buyBook(isbn).toString();

  }

  /**
   * Search book media coverage
   * 1. Get posts from external API
   * 2.Get  title of book from db for isbn given in request
   * 3. Async call - search in title of posts if it contains book title
   * 4. Async call - search in body  of posts if it contains book title
   * 5. Return set of title of posts that  has book title either in title or in body of posts
   * @param isbn isbn no
   * @return set of titles
   */
  public String searchMediaCoverage(String isbn) {
    // get posts from external api
    String response = externalCallservice.callMediaCoverageAPI();
    
    //get title of book from db 
    String title = repository.getBookKey(isbn, TITLE);
    
    JSONArray jsonArray = new JSONArray(response);
    
    //search in title of posts
    CompletableFuture<Set<String>> titleMatchList =
        CompletableFuture.supplyAsync(
            () -> commonUtil.searchInPosts(jsonArray, TITLE, title), asyncTitleExecutor);
    //search in body of posts
    CompletableFuture<Set<String>> bodyMatchList = CompletableFuture
        .supplyAsync(() -> commonUtil.searchInPosts(jsonArray,BODY , title),asyncBodyExecutor);
    // wait for all the async threads
    CompletableFuture.allOf(titleMatchList, bodyMatchList);
    
    titleMatchList.join().stream();
    
    //combine sets 
    return Stream
        .concat(titleMatchList.join().stream(), bodyMatchList.join().stream())
        .collect(Collectors.toSet()).toString();

  }
  
  /**
   * Search book by id
   * Throw exception if it does not exist
   * @param isbn
   * @return book doc
   */
  public String getBookById(String isbn) {
    return repository.getBookById(isbn).toString();
  }
  
  /**
   * Search book by  title
   * Throw exception if it does not exist
   * @param title
   * @return
   */
  public String getBookByTitle(String title) {
   return repository.getBookByTitle(title).toString();
  }
  
  /**
   * Search book by author
   * @param author
   * @return book doc
   */
  public String getBookByAuthor(String author) {
    return repository.getBookByAuthor(author).toString();
  }


}
