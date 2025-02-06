package biz.rtyn.http;

import biz.rtyn.http.token.HttpStatusCode;

public class HttpResponse extends HttpMessage {

    HttpStatusCode statusCode;

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion).append(" ").append(statusCode.STATUS_CODE).append(" ").append(statusCode.MESSAGE).append("\r\n");

        for (String fieldName : headers.keySet()) {
            String fieldValue = headers.get(fieldName);
            response.append(fieldName).append(": ").append(fieldValue).append("\r\n");
        }

        response.append("\r\n")
                .append(body);

        return String.valueOf(response);
    }
}
