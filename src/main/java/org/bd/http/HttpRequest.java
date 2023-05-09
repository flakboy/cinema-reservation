package org.bd.http;

public class HttpRequest {
    private final String method;
    private final String path;

    public HttpRequest(String method, String path){
        this.method = method;
        this.path = path;
    }

    public String toString() {
        return method + " " + path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
