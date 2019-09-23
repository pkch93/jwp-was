package webserver;

import http.request.HttpRequest;
import http.request.HttpRequestParams;
import http.request.HttpRequestParser;
import http.request.Url;
import http.response.HttpResponseGenerator;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestParser.parse(in);
            DataOutputStream dos = new DataOutputStream(out);

            Url requestUrl = httpRequest.getRequestLine().getUrl();
            logger.info("request url: {}", requestUrl);

            if (requestUrl.getUrl().equals("/user/create")) {
                HttpRequestParams parameters = httpRequest.getHttpRequestParams();
                User user = new User(parameters.get("userId"), parameters.get("password"),
                        parameters.get("name"), parameters.get("email"));

                logger.info("user is {}", user);
                HttpResponseGenerator.redirect(dos, httpRequest.getHttpHeader().get("Origin"), "/index.html");
            } else {
                HttpResponseGenerator.forward(dos, requestUrl.getFullUrl());
            }
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }
}
