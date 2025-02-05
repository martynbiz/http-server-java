package biz.rtyn.httpserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpParser {

    private static final int SP = 0x20; // Space (32 in decimal)
    private static final int CR = 0x0D; // Carriage Return (13 in decimal)
    private static final int LF = 0x0A; // Line Feed (10 in decimal)

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException, IOException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException | HttpBadVersionException e) {
            throw new RuntimeException(e);
        }
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException, HttpBadVersionException {

        StringBuilder processingDataBuffer = new StringBuilder();

        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    if (request.getHttpVersion() == null) {
                        request.setHttpVersion(processingDataBuffer.toString());
                    }
                    return;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte == SP) {
                if (request.getMethod() == null) {
                    request.setMethod(processingDataBuffer.toString());
                } else if (request.getRequestTarget() == null) {
                    request.setRequestTarget(processingDataBuffer.toString());
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();
        int _byte;
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    processSingleHeaderField(processingDataBuffer.toString(), request);
                    processingDataBuffer.delete(0, processingDataBuffer.length());
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                processingDataBuffer.append((char) _byte);
            }
        }
    }

    private void processSingleHeaderField(String rawHeaderField, HttpRequest request) throws HttpParsingException {
        Pattern pattern = Pattern.compile("^(?<fieldName>[a-zA-Z0-9-]+):\\s*(?<fieldValue>.+)$");
        Matcher matcher = pattern.matcher(rawHeaderField);

        if (matcher.matches()) {
            String fieldName = matcher.group("fieldName").trim().toLowerCase();
            String fieldValue = matcher.group("fieldValue").trim();
            request.addHeader(fieldName, fieldValue);
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }
}
