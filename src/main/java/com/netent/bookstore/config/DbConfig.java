package com.netent.bookstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
@Setter
public class DbConfig {
  /**
   * Db host.
   */
  @Value("${dbhosts}")
  private String couchBaseHost;

  /**
   * book bucket name.
   */
  @Value("${bucketname}")
  private String couchBaseBucketName;

  /**
   * password to couchbase 
   */
  @Value("${password}")
  private String couchBaseBucketPassword;

  /**
   * username to couchbase   */
  @Value("${user_name}")
  private String couchBaseBucketuserName;

  /**
   * Empty CouchbaseCluster object (will be initialized during bean creation).
   */
  private Cluster cluster;

  /**
   * To create singleton object to connect to CouchBase.
   *
   * @return CouchbaseCluster object for book bucket
   */
  public  @Bean("couchbaseCluster") Cluster getClusterInstance() {
    if (cluster == null) {
      cluster = Cluster.connect(couchBaseHost,couchBaseBucketuserName,couchBaseBucketPassword);
    }
    return cluster;
  }

  /**
   * A bean to expose the   bucket instance.
   *
   * @return Book bucket instance
   */
  public @Bean("BookBucket") Bucket bookBucket() {
    return getClusterInstance().bucket(couchBaseBucketName);
  }

  /**
   * A bean to expose the book  bucket couchbase collection instance.
   *
   * @return Default Collection Instance
   */
  public @Bean("defaultCollection") Collection defaultCollection() {
    return bookBucket().defaultCollection();
  }
}
