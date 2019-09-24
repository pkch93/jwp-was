package http.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeader {
    private static final String HEADER_LINE_DELIMITER = ": ";
    private static final String CONTENT_LENGTH = "Content-Length";

    private Map<String, String> headers;

    private HttpHeader(final Map<String, String> headers) {
        this.headers = Collections.unmodifiableMap(headers);
    }

    public static HttpHeader of(final List<String> headerLines) {
        return new HttpHeader(
                headerLines.stream()
                        .map(line -> line.split(HEADER_LINE_DELIMITER))
                        .collect(Collectors.toMap(token -> token[0], token -> token[1]))
        );
    }

    public static HttpHeader redirect(final String redirectUrl) {
        return HttpHeader.of(Collections.singletonList("Location: " + redirectUrl));
    }

    public String get(final String key) {
        return headers.get(key);
    }

    public int getContentLength() {
        if (headers.containsKey(CONTENT_LENGTH)) {
            return Integer.parseInt(headers.get(CONTENT_LENGTH));
        }

        return 0;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public String toString() {
        return "HttpHeader{" +
                "headers=" + headers +
                '}';
    }
}
