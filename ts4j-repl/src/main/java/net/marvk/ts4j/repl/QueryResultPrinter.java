package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.Controller;

import java.util.List;

public interface QueryResultPrinter {
    String name(final String name);

    String list(final List<Controller> controllers);

    String value(final float value);

    String min(final float min);

    String max(final float max);

    String exists(final boolean exists);

    String set(final float value);

    String error(Error error);

}
