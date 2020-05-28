package com.netent.bookstore.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couchbase.client.java.json.JsonObject;
import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.repository.BookstoreRepository;
import com.netent.bookstore.util.CommonUtil;

@ExtendWith(MockitoExtension.class)
public class BookstoreServiceTest {

  @InjectMocks
  private BookstoreService service;

  @Mock
  private BookstoreRepository repository;

  @Mock
  private CommonUtil commonUtil;

  @Mock
  private ExternalCallService externalCallService;

  @Mock
  CompletableFuture<Object> compFuture;

  @Mock(name = "asyncTitleExecutor")
  private Executor asyncTitleExecutor;

  @Mock(name = "asyncBookExecutor")
  private Executor asynBookExecutor;

  @Test
  public void testAddBook() {
    JsonObject json = JsonObject.create().put("title", "sidney");
    Mockito.when(repository.addBook(Mockito.anyString(), Mockito.any()))
        .thenReturn(json);
    Assertions.assertNotNull(service.addBook(getBookMapping()));

  }

  @Test
  public void testBuyBook() {
    JsonObject json = JsonObject.create().put("title", "sidney");
    Mockito.when(repository.buyBook("1234")).thenReturn(json);
    JsonObject response = JsonObject.fromJson(service.buyBook("1234"));
    Assertions.assertEquals("sidney", response.getString("title"));
  }

  @Test
  public void testGetbookByAuthor() {
    JsonObject json = JsonObject.create().put("title", "sidney");
    List<JsonObject> listJson = new ArrayList<>();
    listJson.add(json);
    Mockito.when(repository.getBookByAuthor("sheldon")).thenReturn(listJson);
    Assertions.assertNotNull(service.getBookByAuthor("sheldon"));
  }

  @Test
  public void testGetbookByTitle() {
    JsonObject json = JsonObject.create().put("title", "sidney");
    List<JsonObject> listJson = new ArrayList<>();
    listJson.add(json);
    Mockito.when(repository.getBookByTitle("sheldon")).thenReturn(listJson);
    Assertions.assertNotNull(service.getBookByTitle("sheldon"));
  }

  @Test
  public void testGetBookById() {
    JsonObject json = JsonObject.create().put("title", "sidney");
    Mockito.when(repository.getBookById("1234")).thenReturn(json);
    JsonObject response = JsonObject.fromJson(service.getBookById("1234"));
    Assertions.assertEquals("sidney", response.getString("title"));

  }

  @Test
  public void testSearchMediaCoverage() {
    Set<String> set = new HashSet<>();
    set.add("hello");
    Mockito.when(externalCallService.callMediaCoverageAPI())
        .thenReturn(getAPIResponse());
    Mockito.when(repository.getBookKey("1234", "title")).thenReturn("investor");
    Mockito.when(commonUtil.searchInPosts(Mockito.any(), Mockito.anyString(),
        Mockito.anyString())).thenReturn(set);
    Mockito.doAnswer((InvocationOnMock invocation) -> {
      ((Runnable) invocation.getArguments()[0]).run();
      return null;
    }).when(asyncTitleExecutor).execute(Mockito.any(Runnable.class));

    Mockito.doAnswer((InvocationOnMock invocation) -> {
      ((Runnable) invocation.getArguments()[0]).run();
      return null;
    }).when(asynBookExecutor).execute(Mockito.any(Runnable.class));
    Assertions.assertNotNull(service.searchMediaCoverage("1234"));
  }

  private BookMapping getBookMapping() {
    BookMapping mapping = new BookMapping();
    mapping.setAuthor("sheldon");
    mapping.setIsbn("1234");
    mapping.setPrice(500);
    mapping.setTitle("sidney");
    return mapping;

  }

  private String getAPIResponse() {
    return "[\n" + "      {\n" + "      \"firstName\":\"Lokesh\",\n"
        + "      \"lastName\":\"Avichetty\",\n" + "      \"country\":\"\",\n"
        + "      \"deliveryIndicator\":false,\n" + "      \"phoneType\":\"\",\n"
        + "      \"city\":\"\",\n" + "      \"addressType\":\"SB\",\n"
        + "      \"validAddressIndicator\":false,\n"
        + "      \"created\":\"2020-02-04T20:55:41+00:00\",\n"
        + "      \"postalCode\":\"\",\n"
        + "      \"companyName\":\"ABC Llc\",\n"
        + "      \"phoneNumber\":\"2637568235623\",\n"
        + "      \"phoneExtension\":\"8788\",\n" + "      \"state\":\"\",\n"
        + "      \"lastModified\":\"2020-02-04T20:55:41+00:00\",\n"
        + "      \"residentialIndicator\":false,\n" + "      \"line2\":\"\",\n"
        + "      \"line1\":\"\",\n" + "      \"preferred\":true,\n"
        + "      \"legacy\":false\n" + "   }\n" + "]";
  }
}
