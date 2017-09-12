package com.speedment.examples.socialserver;

import com.speedment.common.logger.Logger;
import com.speedment.common.logger.LoggerManager;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static java.util.Optional.ofNullable;
import java.util.stream.Collectors;

/**
 *
 * @author Emil Forslund
 */
public abstract class ServerBase extends NanoHTTPD implements ServerAPI {

    private static final Logger LOGGER = LoggerManager.getLogger(ServerBase.class);

    private static final int PORT = 8281;

    public ServerBase() {
        super(PORT);
        LOGGER.info(" running on http://127.0.0.1:" + PORT + ".");
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        final Map<String, String> files = new HashMap<>();
        final NanoHTTPD.Method method = session.getMethod();
        final String uri = session.getUri();
        final String command = uri.substring(uri.indexOf("/") + 1);

        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            try {
                session.parseBody(files);
            } catch (IOException ex) {
                return new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT,
                        "SERVER INTERNAL ERROR: IOException: " + ex.getMessage()
                );
            } catch (ResponseException ex) {
                return new Response(ex.getStatus(), MIME_PLAINTEXT, ex.getMessage());
            }
        }

        final Map<String, String> params = session.getParms();

        LOGGER.debug(method + " '" + uri + "' "
                + params.entrySet().stream()
                .map(e -> "\"" + e.getKey() + "\" = \"" + limitString(e.getValue()) + "\"")
                .collect(Collectors.joining(", ", "(", ")"))
                + " -> "
        );

        final long userId = parseLong(params, "userid");
        final String mail = parseString(params, "mail");
        final String password = parseString(params, "password");
        final String sessionKey = parseString(params, "sessionkey");
        final String title = parseString(params, "title");
        final String description = parseString(params, "description");
        final String imgData = parseString(params, "imgdata");
        final Optional<String> avatar = parseOptional(params, "avatar");
        final String freeText = parseString(params, "freetext");
        final String firstName = parseString(params, "firstname");
        final String lastName = parseString(params, "lastname");
        final Optional<Timestamp> from = parseTime(params, "from");
        final Optional<Timestamp> to = parseTime(params, "to");

        final String msg;
        switch (command) {
            case "register":
                msg = onRegister(mail, password);
                break;
            case "login":
                msg = onLogin(mail, password);
                break;
            case "self":
                msg = onSelf(sessionKey);
                break;
            case "upload":
                msg = onUpload(title, description, imgData, sessionKey);
                break;
            case "find":
                msg = onFind(freeText, sessionKey);
                break;
            case "follow":
                msg = onFollow(userId, sessionKey);
                break;
            case "browse":
                msg = onBrowse(sessionKey, from, to);
                break;
            case "update":
                msg = onUpdate(mail, firstName, lastName, avatar, sessionKey);
                break;
            default:
                msg = "Unknown command.";
                break;
        }

        LOGGER.debug("\"" + msg + "\"");

        return new NanoHTTPD.Response(msg);
    }

    private long parseLong(Map<String, String> params, String command) {
        return parseOptional(params, command).map(Long::parseLong).orElse(-1L);
    }

    private Optional<Timestamp> parseTime(Map<String, String> params, String command) {
        return parseOptional(params, command).map(s -> Timestamp.valueOf(s.replace("T", " ")));
    }

    private String parseString(Map<String, String> params, String command) {
        return parseOptional(params, command).orElse("");
    }

    private Optional<String> parseOptional(Map<String, String> params, String command) {
        return ofNullable(params.get(command)).map(String::trim).filter(p -> !p.isEmpty());
    }

    private String limitString(String s) {
        return limitString(s, 64);
    }

    private String limitString(String s, int len) {
        if (s.length() <= len) {
            return s;
        }
        return s.substring(0, len) + "...";
    }

}
