package br.com.senac.ui;

import br.com.senac.models.User;
import br.com.senac.services.AuthService;
import br.com.senac.validators.InputValidator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WebServerUI {

    private final AuthService authService;
    private final int port;

    public WebServerUI(AuthService authService, int port) {
        this.authService = authService;
        this.port = port;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);

        server.createContext("/", exchange -> serveStatic(exchange, "/web/index.html", "text/html; charset=UTF-8"));
        server.createContext("/style.css", exchange -> serveStatic(exchange, "/web/style.css", "text/css; charset=UTF-8"));
        server.createContext("/app.js", exchange -> serveStatic(exchange, "/web/app.js", "application/javascript; charset=UTF-8"));
        server.createContext("/api/login", this::handleLogin);
        server.createContext("/api/register", this::handleRegister);

        server.setExecutor(null);
        server.start();

        String url = "http://localhost:" + port;
        System.out.println("=========================================");
        System.out.println(" Servidor Web iniciado em: " + url);
        System.out.println("=========================================");

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception ignored) {
        }
    }

    private void serveStatic(HttpExchange exchange, String resourcePath, String contentType) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "text/plain; charset=UTF-8", "Método não permitido".getBytes(StandardCharsets.UTF_8));
            return;
        }
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                sendResponse(exchange, 404, "text/plain; charset=UTF-8", "Arquivo não encontrado".getBytes(StandardCharsets.UTF_8));
                return;
            }
            sendResponse(exchange, 200, contentType, is.readAllBytes());
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"ok\":false,\"message\":\"Método não permitido\"}");
            return;
        }
        try {
            Map<String, String> form = parseForm(exchange);
            String identifier = InputValidator.validateLoginIdentifier(form.get("identifier"));
            String password = form.get("password");

            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("A senha não pode ser vazia.");
            }

            User user = authService.login(identifier, password.toCharArray());

            String json = String.format(
                    "{\"ok\":true,\"message\":\"Autenticado com sucesso!\",\"user\":{\"id\":%d,\"username\":\"%s\",\"email\":\"%s\",\"role\":\"%s\"}}",
                    user.id(),
                    escapeJson(user.username()),
                    escapeJson(user.email()),
                    escapeJson(user.role())
            );
            sendJson(exchange, 200, json);
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"ok\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            sendJson(exchange, 500, "{\"ok\":false,\"message\":\"Erro interno ao autenticar.\"}");
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJson(exchange, 405, "{\"ok\":false,\"message\":\"Método não permitido\"}");
            return;
        }
        try {
            Map<String, String> form = parseForm(exchange);
            String username = InputValidator.validateUsername(form.get("username"));
            String email = InputValidator.validateEmail(form.get("email"));
            String password = form.get("password");
            String confirmPassword = form.get("confirmPassword");

            InputValidator.validatePassword(password);
            if (confirmPassword == null) {
                throw new IllegalArgumentException("Confirme a senha.");
            }

            authService.register(username, email, password.toCharArray(), confirmPassword.toCharArray());

            String json = String.format(
                    "{\"ok\":true,\"message\":\"Usuário '%s' cadastrado com sucesso!\"}",
                    escapeJson(username)
            );
            sendJson(exchange, 201, json);
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"ok\":false,\"message\":\"" + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            sendJson(exchange, 500, "{\"ok\":false,\"message\":\"Erro interno ao cadastrar.\"}");
        }
    }

    private Map<String, String> parseForm(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> params = new HashMap<>();
        if (body.isBlank()) {
            return params;
        }
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }

    private void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        sendResponse(exchange, status, "application/json; charset=UTF-8", json.getBytes(StandardCharsets.UTF_8));
    }

    private void sendResponse(HttpExchange exchange, int status, String contentType, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}
