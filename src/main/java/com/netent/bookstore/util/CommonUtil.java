package com.netent.bookstore.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.springframework.stereotype.Component;
import static com.netent.bookstore.constants.CommonConstants.TITLE;;

/**
 * Util class to perform additional operations or business logic
 * @author jgarg
 *
 */
@Component
public class CommonUtil {

  /**
   * Search in title/body of posts whether it contains the book of title 
   * @param jsonArray Json array  of posts
   * @param key  title/body  key of posts 
   * @param title title of book
   * @return set of titlesof posts 
   */
  public Set<String> searchInPosts(JSONArray jsonArray, String key,
    String title) {
    Set<String> set = new HashSet<>();
    IntStream.range(0, jsonArray.length())
        .mapToObj(i -> jsonArray.getJSONObject(i)).forEach(x -> {
          if (x.getString(key).contains(title)) {
            set.add(x.getString(TITLE));
          }
        });
    return set;
  }

}
