package biz.rtyn.httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser();
    }

    @Test
    void testParseHttpRequestWhenValidStartLine() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(
                    generateGETTestCaseOK()
            );
        } catch (IOException | HttpParsingException e) {
            fail(e);
        }

        assertNotNull(request);
        assertEquals(HttpMethod.GET.toString(), request.getMethod());
        assertEquals("/", request.getRequestTarget());
        assertEquals("HTTP/1.1", request.getHttpVersion());
    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithInvalidMethod() throws IOException {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(
                    generateGETTestCaseBadMethod()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithInvalidNumberOfItems() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithInvalidRequestTarget() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithInvalidHttpVersion() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithEmptyLine() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithOnlyCarriageReturnMissingLineFeed() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithBadHttpVersion() {

    }

    @Test
    void testParseHttpRequestWhenInvalidStartLineWithUnsupportedHttpVersion() {

    }

    private InputStream generateGETTestCaseOK() {
        final String CRLF = "\r\n"; // 10, 13?
        String rawData = "GET / HTTP/1.1" + CRLF +
                "Host: localhost:8080" + CRLF +
                "Connection: keep-alive" + CRLF +
                "Cache-Control: max-age=0" + CRLF +
                "Upgrade-Insecure-Requests: 1" + CRLF +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 Edg/132.0.0.0" + CRLF +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7" + CRLF +
                "Accept-Encoding: gzip, deflate, br, zstd" + CRLF +
                "Accept-Language: en-US,en;q=0.9,en-GB;q=0.8" + CRLF +
                CRLF;

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.UTF_8
                )
        );

        return inputStream;
    }

    private InputStream generateGETTestCaseBadMethod() {
        final String CRLF = "\r\n"; // 10, 13?
        String rawData = "BADDMETHODD / HTTP/1.1" + CRLF +
                "Host: localhost:8080" + CRLF +
                "Connection: keep-alive" + CRLF +
                "Cache-Control: max-age=0" + CRLF +
                "Upgrade-Insecure-Requests: 1" + CRLF +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36 Edg/132.0.0.0" + CRLF +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7" + CRLF +
                "Accept-Encoding: gzip, deflate, br, zstd" + CRLF +
                "Accept-Language: en-US,en;q=0.9,en-GB;q=0.8" + CRLF +
                CRLF;

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.UTF_8
                )
        );

        return inputStream;
    }

    // TODO test headers
}