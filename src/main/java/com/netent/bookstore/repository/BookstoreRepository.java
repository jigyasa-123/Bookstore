package com.netent.bookstore.repository;

import static com.couchbase.client.java.kv.MutateInSpec.decrement;
import static com.couchbase.client.java.kv.MutateInSpec.increment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.LookupInSpec;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.netent.bookstore.exception.ResourceNotFoundException;

/**
 * Book repository class to persist data in db
 * @author jgarg
 *
 */
@Repository
public class BookstoreRepository {

  public static final String COUNT_KEY = "count";


  /**
   * book bucket instance
   */
  @Autowired
  @Qualifier("BookBucket")
  protected Bucket bookBucket;

  // Cluster Object
  @Autowired
  @Qualifier("couchbaseCluster")
  protected Cluster couchbaseCluster;

  // Default Collection Object
  @Autowired
  @Qualifier("defaultCollection")
  protected Collection defaultCollection;

  /**
   * Get specific field of book document (title/author/price)
   * @param isbn id of book document
   * @param key (title/author/price)
   * @return
   */
  public String getBookKey(String isbn, String key) {
    return defaultCollection
        .lookupIn(isbn, Arrays.asList(LookupInSpec.get(key)))
        .contentAs(0, String.class);
  }

  /**
   * Add book in books bucket
   * @param isbn id of book document
   * @param bookInfo book details
   * @return book document
   */
  public JsonObject addBook(String isbn, JsonObject bookInfo) {

    try {
      //insert new book with count 1
      bookInfo.put(COUNT_KEY, 1);
      defaultCollection.insert(isbn, bookInfo);
    } catch (DocumentExistsException e) {
      
      //if book already exists,increment the count
      int count = defaultCollection
          .mutateIn(isbn, Collections.singletonList(increment(COUNT_KEY, 1)))
          .contentAs(0, Integer.class);
      bookInfo.put(COUNT_KEY, count);
    }
    return defaultCollection.get(isbn).contentAsObject();
  }

  /**
   * buy book ,if count is zero add book(increment the count)
   * @param isbn id of book to buy
   * @return book doc
   */
  public JsonObject buyBook(String isbn) {
    
    //if book exist
    if (defaultCollection.exists(isbn).exists()) {
      //decrement the count
      int count = defaultCollection
          .mutateIn(isbn, Collections.singletonList(decrement(COUNT_KEY, 1)))
          .contentAs(0, Integer.class); 
      //if count=0, add a book i.e. increment the count
      if (count == 0) 
        defaultCollection.mutateIn(isbn,
            Collections.singletonList(increment(COUNT_KEY, 1)));    
    }
    
    //if book does not exist, throw exception
    else {
      throw new ResourceNotFoundException("books");
    }
    return defaultCollection.get(isbn).contentAsObject();

  }

  /**
   * Get book info by isbn
   * @param isbn 
   * @return book doc
   */
  public JsonObject getBookById(String isbn) {
    return defaultCollection.get(isbn).contentAsObject();
  }

  /**
   * Get book info by title
   * @param title
   * @return book doc
   */
  public List<JsonObject> getBookByTitle(String title) {
    QueryResult queryResult = couchbaseCluster.query(
        "select * from books where REGEXP_CONTAINS(title,$title)",
        QueryOptions.queryOptions()
            .parameters(JsonObject.create().put("title", title)));
    if (queryResult != null
        && !CollectionUtils.isEmpty(queryResult.rowsAsObject())) {
      return queryResult.rowsAsObject();
    }
    //throw exception if does not exist
    throw new ResourceNotFoundException("books");
  }

  /**
   * get book info by author
   * @param author
   * @return book doc
   */
  public List<JsonObject> getBookByAuthor(String author) {
    QueryResult queryResult = couchbaseCluster.query(
        "select * from books where REGEXP_CONTAINS(author,$author)",
        QueryOptions.queryOptions()
            .parameters(JsonObject.create().put("author", author)));
    if (queryResult != null
        && !CollectionUtils.isEmpty(queryResult.rowsAsObject())) {
      return queryResult.rowsAsObject();
    }
    
    //throw exception if does not exist
    throw new ResourceNotFoundException("books");
  }

}
