package com.netent.bookstore.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.context.ErrorContext;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.ExistsResult;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.LookupInResult;
import com.couchbase.client.java.kv.MutateInResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;
import com.netent.bookstore.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class BookstoreRepositoryTest {

  @InjectMocks
  private BookstoreRepository repository;

  @Mock
  private Collection defaultCollection;

  @Mock
  private MutationResult mutationResut;

  @Mock
  private GetResult getResponse;

  @Mock
  private ErrorContext errorCtx;

  @Mock
  private MutateInResult mutateResult;

  @Mock
  private LookupInResult lookupResult;

  @Mock
  private ExistsResult existResult;
  
  @Mock
  private Cluster cluster;
  
  @Mock
  private QueryResult queryResult;

  /**
   * Add book - new book document with isbn as doc id will be created
   */
  @Test
  public void testAddBook() {
    JsonObject json = getJsonRequest();
    Mockito.when(defaultCollection.insert("1234", json))
        .thenReturn(mutationResut);
    Mockito.when(defaultCollection.get("1234")).thenReturn(getResponse);
    Mockito.when(getResponse.contentAsObject()).thenReturn(json);
    Assertions.assertNotNull(repository.addBook("1234", json));
  }

  /**
   * Add book - increment the count if book already exists
   */
  @Test
  public void testAddBookWithDocumentExistException() {
    JsonObject json = getJsonRequest();
    Mockito.when(defaultCollection.insert("1234", json))
        .thenThrow(new DocumentExistsException(errorCtx));
    Mockito.when(defaultCollection.mutateIn(Mockito.anyString(), Mockito.any()))
        .thenReturn(mutateResult);
    Mockito.when(mutateResult.contentAs(0, Integer.class)).thenReturn(1);
    Mockito.when(defaultCollection.get("1234")).thenReturn(getResponse);
    Mockito.when(getResponse.contentAsObject()).thenReturn(json);
    Assertions.assertNotNull(repository.addBook("1234", json));
  }

  /**
   * Buy book - book exist with count 1 and more
   *
   */
  @Test
  public void testBuyBook() {
    Mockito.when(defaultCollection.exists("1234")).thenReturn(existResult);
    Mockito.when(existResult.exists()).thenReturn(true);
    Mockito.when(defaultCollection.mutateIn(Mockito.anyString(), Mockito.any()))
        .thenReturn(mutateResult);
    Mockito.when(mutateResult.contentAs(0, Integer.class)).thenReturn(1);
    Mockito.when(defaultCollection.get("1234")).thenReturn(getResponse);
    Mockito.when(getResponse.contentAsObject()).thenReturn(getJsonRequest());
    Assertions.assertNotNull(repository.buyBook("1234"));
  }

  /**
   * Buy book - Book does not exist . throw resource not found exception
   */
  @Test
  public void testBuyBookNotExist() {
    Mockito.when(defaultCollection.exists("1234")).thenReturn(existResult);
    Mockito.when(existResult.exists()).thenReturn(false);
    Assertions.assertThrows(ResourceNotFoundException.class,
        () -> repository.buyBook("1234"));
  }

  /**
   * Buy book - count becomes 0 , add a book i.e increment the book count
   */
  @Test
  public void testBuyBookWithOneBookLeft() {
    Mockito.when(defaultCollection.exists("1234")).thenReturn(existResult);
    Mockito.when(existResult.exists()).thenReturn(true);
    Mockito.when(defaultCollection.mutateIn(Mockito.anyString(), Mockito.any()))
        .thenReturn(mutateResult);
    Mockito.when(mutateResult.contentAs(0, Integer.class)).thenReturn(0);
    Mockito.when(defaultCollection.get("1234")).thenReturn(getResponse);
    Mockito.when(getResponse.contentAsObject()).thenReturn(getJsonRequest());
    Assertions.assertNotNull(repository.buyBook("1234"));
  }

  /** 
   * Get Book Key  value (title/author/price)
   * 
   */
  @Test
  public void testGetBookKey() {
    Mockito.when(defaultCollection.lookupIn(Mockito.anyString(), Mockito.any()))
        .thenReturn(lookupResult);
    Mockito.when(lookupResult.contentAs(0, String.class)).thenReturn("sidney");
    Assertions.assertEquals("sidney", repository.getBookKey("1234", "title"));
  }

  /**
   * Get book by Id (isbn)
   */
  @Test
  public void testGetBookById() {
    Mockito.when(defaultCollection.get("1234")).thenReturn(getResponse);
    Mockito.when(getResponse.contentAsObject()).thenReturn(getJsonRequest());
    Assertions.assertNotNull(repository.getBookById("1234"));
  }
  
  /**
   * Search book  by author name
   */
  @Test
  public void tesGetBookByAuthor() {
    List<JsonObject> response = new ArrayList<JsonObject>();
    response.add(getJsonRequest());
    Mockito.when(cluster.query(Mockito.anyString(),Mockito.any())).thenReturn(queryResult);
    Mockito.when(queryResult.rowsAsObject()).thenReturn(response);
    Assertions.assertEquals(1, repository.getBookByAuthor("sheldon").size());
  }
  
  /**
   * Search book  by title 
   */
  @Test
  public void tesGetBookByTitle() {
    List<JsonObject> response = new ArrayList<JsonObject>();
    response.add(getJsonRequest());
    Mockito.when(cluster.query(Mockito.anyString(),Mockito.any())).thenReturn(queryResult);
    Mockito.when(queryResult.rowsAsObject()).thenReturn(response);
    Assertions.assertEquals(1, repository.getBookByTitle("sheldon").size());
  }
  
  /**
   * Search book  by author name - resource not found exception
   */
  @Test
  public void tesGetBookByAuthorException() {
    Mockito.when(cluster.query(Mockito.anyString(),Mockito.any())).thenThrow(new ResourceNotFoundException("books"));
    Assertions.assertThrows(ResourceNotFoundException.class,()  ->  repository.getBookByAuthor("sheldon"));
  }
  
  /**
   * Search book  by title  - resource not found exception
   */
  @Test
  public void tesGetBookByTitleException() {
    Mockito.when(cluster.query(Mockito.anyString(),Mockito.any())).thenThrow(new ResourceNotFoundException("books"));
    Assertions.assertThrows(ResourceNotFoundException.class,()  ->  repository.getBookByTitle("sheldon"));
  }
  
  private JsonObject getJsonRequest() {
    return JsonObject.create().put("isbn", "1234").put("title", "sidney")
        .put("price", 500).put("author", "sheldon").put("count", 1);
  }

}
