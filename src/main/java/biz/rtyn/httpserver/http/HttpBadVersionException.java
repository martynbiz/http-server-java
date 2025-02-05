package biz.rtyn.httpserver.http;

public class HttpBadVersionException extends Exception {
    private final HttpStatusCode errorCode;

    public HttpBadVersionException(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
