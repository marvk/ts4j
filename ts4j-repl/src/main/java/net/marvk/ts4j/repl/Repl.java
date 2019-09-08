package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.Ts4j;
import net.marvk.ts4j.api.TrainSimulatorApi;
import net.marvk.ts4j.api.TrainSimulatorApiInstantiationException;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public final class Repl {
    private static final String HELP = "Available operations\n" +
            "\n" +
            Method.NAME.getName() + "\n" +
            "    name of the current locomotive\n" +
            "\n" +
            Method.LIST.getName() + "\n" +
            "    list of all controllers\n" +
            "\n" +
            Method.VALUE.getName() + " <controller>\n" +
            "    value of this controller, specified by name or id\n" +
            "\n" +
            Method.MIN.getName() + " <controller>\n" +
            "    minimum value of this controller, specified by name or id\n" +
            "\n" +
            Method.MAX.getName() + " <controller>\n" +
            "    maximum value of this controller, specified by name or id\n" +
            "\n" +
            Method.EXISTS.getName() + " <controller>\n" +
            "    if this controller, specified by name or id, exists\n" +
            "\n" +
            Method.SET.getName() + " <controller> <value>\n" +
            "    sets the value of the controller specified by name or id to the float value  ";

    public static void main(final String[] args) throws TrainSimulatorApiInstantiationException {
        final TrainSimulatorApi api;

        if (args.length == 0) {
            api = Ts4j.createX64();
        } else {
            api = Ts4j.createX64(Paths.get(args[0]));
        }

        new Repl(api, Mode.JSON).start();
    }

    private final TrainSimulatorApi api;
    private final Mode mode;
    private final RequestResponseHandler requestResponseHandler;

    private Repl(final TrainSimulatorApi api, final Mode mode) {
        this.api = Objects.requireNonNull(api);
        this.mode = Objects.requireNonNull(mode);

        if (mode == Mode.JSON) {
            this.requestResponseHandler = new JsonRequestResponseHandler(System.out);
        } else if (mode == Mode.PLAIN_TEXT) {
            this.requestResponseHandler = new PlainTextRequestResponseHandler();
        } else {
            throw new AssertionError();
        }
    }

    private void start() {
        try {
            loop();
        } finally {
            api.setConnected(false);
        }
    }

    private void loop() {
        while (!connect()) ;

        println(System.out, HELP);

        final Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            final boolean connected = connect();

            if (!connected) {
                continue;
            }

            requestResponseHandler.handle(scanner.nextLine(), api);
        }
    }

    /**
     * Try to connect RailDriver if it is not connected.
     *
     * @return {@code true} if RailDriver is connected, {@code false} otherwise
     */
    private boolean connect() {
        if (!api.isConnected()) {
            println(System.out, "RailDriver is not connected, trying to connect...");
            api.setConnected(true);

            if (!api.isConnected()) {
                println(System.err, "RailDriver failed to connect, trying again in 1 second...");
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }

            println(System.out, "RailDriver connected successfully");
        }

        return true;
    }

    private void println(final PrintStream printStream, final String s) {
        if (mode == Mode.PLAIN_TEXT) {
            printStream.println(s);
        }
    }

    private enum Mode {
        JSON,
        PLAIN_TEXT;
    }
}
