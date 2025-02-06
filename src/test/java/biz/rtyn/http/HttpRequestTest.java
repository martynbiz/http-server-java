package biz.rtyn.http;

import biz.rtyn.http.token.HttpStatusCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    void testInstanceWithValidRequestLine() {
        try {
            HttpRequest request = new HttpRequest("GET /index.html HTTP/1.1");
        } catch (HttpParsingException e) {
            fail(e);
        }
    }

    @Test
    void testInstanceWithInvalidRequestLineInvalidMethod() {
        try {
            HttpRequest request = new HttpRequest("XXX /index.html HTTP/1.1");
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void testInstanceWithInvalidRequestLineWithInvalidNumberOfItems() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithInvalidRequestTarget() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithInvalidHttpVersion() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithEmptyLine() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithOnlyCarriageReturnMissingLineFeed() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithBadHttpVersion() {

    }

    @Test
    void testInstanceWithInvalidRequestLineWithUnsupportedHttpVersion() {

    }

}