package com.netent.bookstore.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.netent.bookstore.mapping.BookMapping;
import com.netent.bookstore.service.BookstoreService;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.path.json.JsonPath;

@ExtendWith(MockitoExtension.class)
public class BookstoreControllerTest {

  @Mock
  private BookstoreService service;

  @InjectMocks
  private BookstoreController controller;

  private MockMvcRequestSpecification mvcSpecs;

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @BeforeEach
  public void beforeTest() {
    mvcSpecs = getRawSpec(controller);
  }

  protected MockMvcRequestSpecification getRawSpec(final Object... beans) {
    return given().standaloneSetup(beans);
  }

  @Test
  public void testAddBook()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    BookMapping mapping = getBookMapping();
    Mockito.when(service.addBook(Mockito.any())).thenReturn(response);
    assertResource(mvcSpecs, "/books", HttpMethod.POST, mapping,
        toJson(mapping));
  }

  @Test
  public void testBuyBook()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    Mockito.when(service.buyBook("1234")).thenReturn(response);
    assertResource(mvcSpecs, "/books/buy/1234", HttpMethod.GET,
        toJsonFromJsonString(response));
  }

  @Test
  public void testSearchBookMediaCoverage()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    Set<String> set = new HashSet<>();
    Mockito.when(service.searchMediaCoverage("1234")).thenReturn(set);
    assertResource(mvcSpecs, "/books/mediacoverage/1234", HttpMethod.GET,
        toJsonFromJsonString(response));
  }

  @Test
  public void testSearchByIsbn()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    Mockito.when(service.getBookById("1234")).thenReturn(response);
    assertResource(mvcSpecs, "/books/1234", HttpMethod.GET,
        toJsonFromJsonString(response));
  }

  @Test
  public void testSearchByTitle()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    Mockito.when(service.getBookByTitle("sidney")).thenReturn(response);
    assertResource(mvcSpecs, "/books/title?name=sidney", HttpMethod.GET,
        toJsonFromJsonString(response));
  }

  @Test
  public void testSearchByAuthor()
    throws JsonSyntaxException, JsonProcessingException {
    String response = getBookInfo();
    Mockito.when(service.getBookByAuthor("sheldon")).thenReturn(response);
    assertResource(mvcSpecs, "/books/author?name=sheldon", HttpMethod.GET,
        toJsonFromJsonString(response));
  }

  protected JsonElement toJson(Object obj)
    throws JsonSyntaxException, JsonProcessingException {
    return JsonParser.parseString(OBJECT_MAPPER.writeValueAsString(obj));
  }

  protected JsonElement toJsonFromJsonString(String obj)
    throws JsonSyntaxException, JsonProcessingException {
    return JsonParser.parseString(obj);
  }

  protected void assertResource(MockMvcRequestSpecification spec, String uri,
    HttpMethod method, final Object successRequestBody,
    JsonElement expectedResponse) throws JsonProcessingException {
    if (method == HttpMethod.PUT) {
      spec.when().put(uri).then().statusCode(415);
      spec.contentType(ContentType.JSON).when().put(uri).then().statusCode(400);
      spec.contentType(ContentType.JSON).body(successRequestBody).when()
          .put(uri).then()
          .body("",
              equalTo(
                  new JsonPath(expectedResponse.toString()).getJsonObject("")))
          .statusCode(200);
    } else if (method == HttpMethod.POST) {
      spec.when().post(uri).then().statusCode(415);
      spec.contentType(ContentType.JSON).when().post(uri).then()
          .statusCode(400);
      spec.contentType(ContentType.JSON).body(successRequestBody).when()
          .post(uri).then()
          .body("",
              equalTo(
                  new JsonPath(expectedResponse.toString()).getJsonObject("")))
          .statusCode(201);
    }
  }

  protected void assertResource(MockMvcRequestSpecification spec, String uri,
    HttpMethod method, JsonElement expectedResponse) {
    if (method == HttpMethod.GET) {
      spec.contentType(ContentType.JSON).when().get(uri).then()
          .body("",
              equalTo(
                  new JsonPath(expectedResponse.toString()).getJsonObject("")))
          .statusCode(200);
    } else if (method == HttpMethod.DELETE) {
      if (expectedResponse == null) {
        spec.contentType(ContentType.JSON).when().delete(uri).then()
            .statusCode(204);
      } else {
        spec.contentType(ContentType.JSON).when().delete(uri).then()
            .body("", equalTo(
                new JsonPath(expectedResponse.toString()).getJsonObject("")))
            .statusCode(200);
      }
    }
  }

  protected String getBookInfo() {
    return "{\n" + "  \"isbn\": \"1234\",\n" + "    \"title\": \"sidney\",\n"
        + "    \"author\": \"sheldon\",\n" + "    \"price\": 500\n" + "}";
  }

  private BookMapping getBookMapping() {
    BookMapping mapping = new BookMapping();
    mapping.setAuthor("sheldon");
    mapping.setIsbn("1234");
    mapping.setPrice(500);
    mapping.setTitle("sidney");
    return mapping;

  }
}
