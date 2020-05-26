package com.netent.bookstore.repository;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
import static com.couchbase.client.java.kv.MutateInSpec.*;

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

  public String getBooks(String id) {
    return defaultCollection.get(id).toString();
  }

  public JsonObject addBook(String id, JsonObject bookInfo) {

    try {
      bookInfo.put(COUNT_KEY, 0);
      defaultCollection.insert(id, bookInfo);
    } catch (DocumentExistsException e) {
      int count = defaultCollection
          .mutateIn(id, Collections.singletonList(increment(COUNT_KEY, 1)))
          .contentAs(0, Integer.class);
      bookInfo.put(COUNT_KEY, count);
    }
    return bookInfo.put("id", id);
  }
  
  public JsonObject buyBook(String id) {
    if(defaultCollection.exists(id).exists()) {
      int count = defaultCollection
          .mutateIn(id, Collections.singletonList(decrement(COUNT_KEY, 1)))
          .contentAs(0, Integer.class);
      if(count  == 0) {
        defaultCollection
        .mutateIn(id, Collections.singletonList(increment(COUNT_KEY, 1)));

      }
    }
    
    else {
      // throw exception
    }
    return defaultCollection.get(id).contentAsObject();

  }

}
