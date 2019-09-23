package http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequestParams {
    private static final String QUERY_PARAM_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "=";
    private static final String REQUEST_LAST_LINE = "";
    private static final String NONE_VALUE = "";

    private final Map<String, String> queryParams;

    private HttpRequestParams(final Map<String, String> queryParams) {
        this.queryParams = Collections.unmodifiableMap(queryParams);
    }

    public static HttpRequestParams init() {
        return new HttpRequestParams(new HashMap<>());
    }

    public static HttpRequestParams of(final String queryString) {
        return new HttpRequestParams(extractQueryParams(queryString));
    }

    private static Map<String, String> extractQueryParams(final String queryString) {
        if (REQUEST_LAST_LINE.equals(queryString)) {
            return Collections.emptyMap();
        }
        return generateQueryParams(queryString);
    }

    private static Map<String, String> generateQueryParams(final String queryString) {
        Map<String, String> parameters = new HashMap<>();
        String[] params = queryString.split(QUERY_PARAM_DELIMITER);

        for (String param : params) {
            String[] tokens = param.split(KEY_VALUE_DELIMITER);
            parameters.put(tokens[0], parseParamValue(tokens));
        }

        return parameters;
    }

    private static String parseParamValue(final String[] tokens) {
        if (tokens.length == 1) {
            return NONE_VALUE;
        }

        return tokens[1];
    }

    public String get(final String key) {
        return queryParams.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequestParams{" +
                "queryParams=" + queryParams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestParams that = (HttpRequestParams) o;
        return Objects.equals(queryParams, that.queryParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryParams);
    }
}
