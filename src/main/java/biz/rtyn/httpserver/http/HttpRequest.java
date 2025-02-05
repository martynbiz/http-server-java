package biz.rtyn.httpserver.http;

import java.util.HashMap;

public class HttpRequest extends HttpMessage {
    private String method;
    private String requestTarget;
    private String httpVersion;

    private HashMap<String,String> headers = new HashMap<>();

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    private String originalHttpVersion;
    private HttpVersion bestCompatibleHttpVersion;

    public HttpRequest() {

    }

    public String getMethod() {
        return method;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method: HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = String.valueOf(method);
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        this.requestTarget = String.valueOf(requestTarget);;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    void setHttpVersion(String originalHttpVersion) throws HttpBadVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        this.httpVersion = this.bestCompatibleHttpVersion.LITERAL;
        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }

    void addHeader(String fieldName, String fieldValue) {
        headers.put(fieldName, fieldValue);
    }
}
