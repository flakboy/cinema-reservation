package org.bd.http;

import java.io.*;

public class HttpResponseBuilder {
    private static final String CRLF = "\n\r";
    
    public void serveFile(OutputStream outputStream, String path, String mimetype) throws IOException {
        File file = new File(path);

        outputStream.write((
            "HTTP/1.1 " + 200 + " OK" + CRLF +
            "Content-Type: " + mimetype + CRLF +
//            "Content-Length: " + (index.length()) + CRLF + //Content-Length jest opcjonalne, a w obecnej postaci nie działa
            CRLF).getBytes());

        FileInputStream reader = null;
        try {
            reader = new FileInputStream(file);
            int _byte = reader.read();
            while (_byte >= 0) {
                outputStream.write(_byte);
                _byte = reader.read();
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        outputStream.write((CRLF + CRLF).getBytes());
    }

    public void serve404(OutputStream outputStream) throws IOException {
        serveFile(outputStream, "src/main/resources/404.html", "text/html");
    }
}
