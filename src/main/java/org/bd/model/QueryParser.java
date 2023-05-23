package org.bd.model;

import java.util.HashMap;
import java.util.Map;

public class QueryParser {
    public Map<String, String> parse(String queryString) {
        String[] params = queryString.split("&");

        Map<String, String> queryMap = new HashMap<>();

        for (String param : params) {
            String[] tokens = param.split("=");
            String key = tokens[0];
            String value = tokens[1];
            queryMap.put(key, value);
        }

        return queryMap;
    }
}
