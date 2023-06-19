package org.bd;

import java.util.HashMap;
import java.util.Map;

public class QueryParser {
    public Map<String, String> parse(String queryString) throws IllegalArgumentException {
        String[] params = queryString.split("&");

        Map<String, String> queryMap = new HashMap<>();

        try {
            for (String param : params) {
                String[] tokens = param.split("=");
                String key = tokens[0];
                String value = tokens[1];
                queryMap.put(key, value);
            }
        } catch (IndexOutOfBoundsException e) {
            throw  new IllegalArgumentException("Given query string has invalid format!");
        }

        return queryMap;
    }
}
