package net.marvk.ts4j.api;

public class ControllerNotFoundException extends RuntimeException {
    public ControllerNotFoundException(final String name) {
        this("name", "'" + name + "'");
    }

    public ControllerNotFoundException(final int id) {
        this("id", String.valueOf(id));
    }

    private ControllerNotFoundException(final String type, final String value) {
        super("Controller with " + type + " " + value + " does not currently exist");
    }
}
