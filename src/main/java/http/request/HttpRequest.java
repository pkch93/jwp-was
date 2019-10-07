package http.request;

import http.common.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class HttpRequest {
    private static final String COOKIE_HEADER = "Cookie";
    private static final String COOKIE_DELIMITER = "; ";
    private static final String JSESSIONID = "JSESSIONID";
    private static final String COOKIE_KEY_VALUE_DELIMITER = "=";
    private final RequestLine requestLine;
    private final HttpRequestParams httpRequestParams;
    private final HttpHeader httpHeader;
    private final MessageBody messageBody;
    private HttpCookies cookies;
    private String jSessionId;

    public HttpRequest(final RequestLine requestLine,
                       final HttpRequestParams httpRequestParams,
                       final HttpHeader httpHeader,
                       final MessageBody messageBody) {
        this.requestLine = requestLine;
        this.httpRequestParams = httpRequestParams;
        this.httpHeader = httpHeader;
        this.messageBody = messageBody;
    }

    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
    }

    public HttpVersion getHttpVersion() {
        return requestLine.getHttpVersion();
    }

    public Url getUrl() {
        return requestLine.getUrl();
    }

    public HttpRequestParams getHttpRequestParams() {
        return httpRequestParams;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public String getJSessionId() {
        return jSessionId;
    }

    public HttpSession getHttpSession() {
        HttpCookie jSessionId = getCookies().get(JSESSIONID);

        HttpSession httpSession;
        if (jSessionId == null) {
            httpSession = SessionManager.getSession();
        } else {
            httpSession = SessionManager.getSession(UUID.fromString(jSessionId.getValue()));
        }

        this.jSessionId = httpSession.getSessionId();
        return httpSession;
    }

    public HttpCookies getCookies() {
        if (cookies == null) {
            cookies = cookieParse();
        }

        return cookies;
    }

    private HttpCookies cookieParse() {
        String rawCookies = httpHeader.get(COOKIE_HEADER);
        if (rawCookies == null) {
            return new HttpCookies(Collections.emptyMap());
        }

        return new HttpCookies(Arrays.stream(rawCookies.split(COOKIE_DELIMITER))
                .map(rawCookie -> rawCookie.split(COOKIE_KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(cookie -> cookie[0],
                        cookie -> HttpCookie.builder(cookie[0], cookie[1]).build())));
    }
}