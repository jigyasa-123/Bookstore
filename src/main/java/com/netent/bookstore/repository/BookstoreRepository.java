package com.netent.bookstore.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;

@Repository
public class BookstoreRepository {
  
  /**
   * book bucket instance
   */
  @Autowired
  @Qualifier("BookBucket")
  protected Bucket bookBucket;

  //Cluster Object
  @Autowired
  @Qualifier("couchbaseCluster")
  protected Cluster couchbaseCluster;

  //Default Collection Object
  @Autowired
  @Qualifier("defaultCollection")
  protected Collection defaultCollection;
  
  public String getBooks(String id) {
   return defaultCollection.get(id).toString();
  }

}
