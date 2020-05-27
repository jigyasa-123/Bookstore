package com.netent.bookstore.repository;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.LookupInResult;
import com.couchbase.client.java.kv.LookupInSpec;
import com.couchbase.client.java.kv.MutateInResult;
import com.couchbase.client.java.kv.MutateInSpec;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;

import static com.couchbase.client.java.kv.MutateInSpec.*;
import static com.netent.bookstore.constants.CommonConstants.ISBN;

@Repository
public class BookstoreRepository {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

  public static final String CREATED_KEY = "created";

  public static final String COUNT_KEY = "count";

  public static final String LAST_MODIFIED_KEY = "lastModified";

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

 

  public String getBookKey(String isbn,String key) {
    return defaultCollection.lookupIn(isbn, Arrays.asList(LookupInSpec.get(key))).contentAs(0, String.class);
  }

  public JsonObject addBook(String isbn, JsonObject bookInfo) {

    try {
      bookInfo.put(COUNT_KEY, 1);
      defaultCollection.insert(isbn, bookInfo);
    } catch (DocumentExistsException e) {
      int count = defaultCollection
          .mutateIn(isbn, Collections.singletonList(increment(COUNT_KEY, 1)))
          .contentAs(0, Integer.class);
      bookInfo.put(COUNT_KEY, count);
    }
    return defaultCollection.get(isbn).contentAsObject();
  }

  public JsonObject buyBook(String isbn) {
    if (defaultCollection.exists(isbn).exists()) {
      int count = defaultCollection
          .mutateIn(isbn, Collections.singletonList(decrement(COUNT_KEY, 1)))
          .contentAs(0, Integer.class);
      if (count == 0) {
        defaultCollection.mutateIn(isbn,
            Collections.singletonList(increment(COUNT_KEY, 1)));

      }
    }

    else {
      // throw exception
    }
    return defaultCollection.get(isbn).contentAsObject();

  }
  
  public JsonObject getBookById(String id) {
    return defaultCollection.get(id).contentAsObject();
  }
  
  public List<JsonObject> getBookByTitle(String title) {
    List<JsonObject> ans = null; 
    QueryResult queryResult = couchbaseCluster
        .query("select * from books where REGEXP_CONTAINS(title,$title)",
          QueryOptions.queryOptions().parameters(JsonObject.create().put("title",title)));
      //Extracting from the Query result
      if (queryResult!= null && !CollectionUtils.isEmpty(queryResult.rowsAsObject())) {
        // just return first matched result, since organization by proaccount id should also be unique.
         ans = queryResult.rowsAsObject();
      }
      return ans;
  }
  
  public List<JsonObject> getBookByAuthor(String author) {
    List<JsonObject> ans = null; 
    QueryResult queryResult = couchbaseCluster
        .query("select * from books where REGEXP_CONTAINS(author,$author)",
          QueryOptions.queryOptions().parameters(JsonObject.create().put("author",author)));
      //Extracting from the Query result
      if (queryResult!= null && !CollectionUtils.isEmpty(queryResult.rowsAsObject())) {
        // just return first matched result, since organization by proaccount id should also be unique.
         ans = queryResult.rowsAsObject();
      }
      return ans;
  }

 
}
