package biz.rtyn.http;

import biz.rtyn.http.token.HttpMethod;
import biz.rtyn.http.token.HttpStatusCode;
import biz.rtyn.http.token.HttpVersion;

import java.util.HashMap;

public class HttpRequest extends HttpMessage {
    private String method;
    private String requestTarget;

    public HttpRequest(String requestLine) throws HttpParsingException {

        // Split the string by one or more whitespace characters (spaces, tabs, etc.)
        String[] parts = requestLine.split("\\s+");

        // Check if we have the expected number of components
        if (parts.length == 3) {
            setMethod(parts[0]);
            setRequestTarget(parts[1]);
            setHttpVersion(parts[2]);
        } else {
            System.out.println("Invalid request line format");
        }

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
                HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST
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
}
