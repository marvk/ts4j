package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.Controller;

import java.util.List;
import java.util.stream.Collectors;

public class PlainTextQueryResultPrinter implements QueryResultPrinter {
    @Override
    public String name(final String name) {
        return name;
    }

    @Override
    public String list(final List<Controller> controllers) {
        return controllers.stream()
                          .map(controller -> controller.getName() + " (" + controller.getId() + ")")
                          .collect(Collectors.joining("\n"));
    }

    @Override
    public String value(final float value) {
        return Float.toString(value);
    }

    @Override
    public String min(final float min) {
        return Float.toString(min);
    }

    @Override
    public String max(final float max) {
        return Float.toString(max);
    }

    @Override
    public String exists(final boolean exists) {
        return Boolean.toString(exists);
    }

    @Override
    public String error(final Error error) {
        return error.getMessage();
    }

    @Override
    public String set(final float value) {
        return "Value set";
    }
}
