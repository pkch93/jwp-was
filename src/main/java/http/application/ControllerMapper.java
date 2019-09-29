package http.application;

import http.application.controller.BasicController;
import http.application.controller.CreateUserController;
import http.application.controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
    }

    public static Controller controllerMapping(String url) {
        Controller controller = controllers.get(url);

        if (controller == null) {
            return new BasicController();
        }
        return controller;
    }
}
