package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.Controller;

import java.util.List;
import java.util.stream.Collectors;

public class JsonQueryResultPrinter implements QueryResultPrinter {
    @Override
    public String name(final String name) {
        return data(object("locoName", name));
    }

    @Override
    public String list(final List<Controller> controllers) {
        final String json =
                controllers.stream()
                           .map(JsonQueryResultPrinter::toJsonString)
                           .collect(Collectors.joining(", "));

        final String result = "[" + json + "]";
        return data(object("controllers", result));
    }

    @Override
    public String value(final float value) {
        return data(object("value", value));
    }

    @Override
    public String min(final float min) {
        return data(object("min", min));
    }

    @Override
    public String max(final float max) {
        return data(object("max", max));
    }

    @Override
    public String exists(final boolean exists) {
        return data(object("exists", exists));
    }

    @Override
    public String set(final float value) {
        return data(object("result", "OK"));
    }

    @Override
    public String error(final Error error) {
        return "{\"error\": {\"code\": " + error.getCode() + ",  \"message\": \"" + error.getMessage() + "\"}}";
    }

    private static String toJsonString(final Controller controller) {
        return "{\"name\": \"" + controller.getName() + "\"," +
                " \"id\": " + controller.getId() + "}";
    }

    private static String object(final String s, final Object value) {
        return "\"" + s + "\": \"" + value.toString() + "\"";
    }

    private static String data(final String data) {
        return "{\"data\": {" + data + "}}";
    }
}