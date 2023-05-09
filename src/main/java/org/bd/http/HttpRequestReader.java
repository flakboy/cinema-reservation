package org.bd.http;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequestReader {

    private static final int SPACE = 0x20;
//    private static final int CR = 0x0D;
//    private static final int LF = 0x0A;


    public HttpRequestReader() {}

    public HttpRequest read(InputStream stream) throws IOException {
//        InputStream inputStream = stream;
//        int currChar = inputStream.read();
//        int i = 0;
//        while (currChar > 0 && i < 600) {
//            System.out.print((char) currChar);
//            currChar = inputStream.read();
//            i++;
//        }


        String method = null;
        String path = null;

        StringBuilder token = new StringBuilder();

        int _byte = stream.read();
        int i = 0;
        while (path == null && _byte >= 0 && i < 200) {
            i++;
            if (_byte == SPACE) {
                if (method == null) {
                    method = token.toString();
                    token.delete(0, token.length());
                } else if (path == null) {
                    path = token.toString();
                    token.delete(0, token.length());
                    break;
                }
            } else {
                token.append((char) _byte);
            }
            _byte =stream.read();
        }
        return new HttpRequest(method, path);
    }
}
