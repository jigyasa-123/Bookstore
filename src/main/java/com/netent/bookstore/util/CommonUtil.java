package com.netent.bookstore.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
  
  

  public Set<String> searchInKey(JSONArray jsonArray, String key,
    String title) {
    Set<String> set = new HashSet<>();
    IntStream.range(0, jsonArray.length())
        .mapToObj(i -> jsonArray.getJSONObject(i))
        .forEach(x -> {
          if (x.getString(key).contains(title)) {
            set.add(x.getString("title"));
          }
        });
    return set;
  }

}



//
//public Set<String> searchInKey(JSONArray jsonArray, String key,
//  String title) {
//  Set<String> set = new HashSet<>();
//  IntStream.range(0, jsonArray.length())
//      .mapToObj(i -> jsonArray.getJSONObject(i))
//      .map(json -> json.getString("title")).forEach(x -> {
//        if (x.contains(title)) {
//          set.add(title);
//        }
//      });
//  return set;
//}
//
//}
