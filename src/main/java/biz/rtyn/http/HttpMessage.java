package biz.rtyn.http;

import biz.rtyn.http.token.HttpStatusCode;
import biz.rtyn.http.token.HttpVersion;

import java.util.HashMap;

public abstract class HttpMessage {

    protected String httpVersion;
    protected HashMap<String,String> headers = new HashMap<>();
    protected String body = "";

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    private String originalHttpVersion;
    private HttpVersion bestCompatibleHttpVersion;

    public void addHeaderLine(String header) {
        // TODO regex to check valid line
        String[] parts = header.split(":\\s+");
        addHeader(parts[0], parts[1].trim());
    }

    public void addHeader(String fieldName, String fieldValue) {
        headers.put(fieldName, fieldValue);
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
    }

    public void addBody(String body) {
        this.body = body;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String originalHttpVersion) throws HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        this.httpVersion = this.bestCompatibleHttpVersion.LITERAL;
        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }
}
