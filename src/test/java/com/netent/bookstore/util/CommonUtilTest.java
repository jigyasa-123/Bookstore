package com.netent.bookstore.util;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommonUtilTest {
  
  private CommonUtil commonUtil;
  
  @Test
  public void testSearchinPosts() {
    commonUtil = new CommonUtil();
    Set<String> expectedResult = new HashSet<>();
    expectedResult.add("fault book");
    expectedResult.add("sheldon book");
    Set<String> result =
        commonUtil.searchInPosts(createJsonArray(), "title", "book");
    Assertions.assertTrue(expectedResult.containsAll(result));
  }
  
  private JSONArray createJsonArray() {
    JSONArray jsonArray  =new JSONArray();
    JSONObject json1 = new JSONObject();
    json1.put("title", "fault book");
    json1.put("body","demo");
    
    JSONObject json2 = new JSONObject();
    json2.put("title", "sheldon book");
    json2.put("body","demo2");
    
    JSONObject json3 = new JSONObject();
    json3.put("title", "rain");
    json3.put("body","demo3");
    
    jsonArray.put(json1);
    jsonArray.put(json2);
    jsonArray.put(json3);
    return jsonArray;
    
  }

}

