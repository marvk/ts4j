package net.marvk.ts4j.repl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.marvk.ts4j.api.Controller;
import net.marvk.ts4j.api.TrainSimulatorApi;

import java.io.PrintStream;
import java.util.Optional;

public class JsonRequestResponseHandler implements RequestResponseHandler {
    private static final Error MALFORMED_JSON = new Error("Malformed json", 1);
    private static final Error NO_CONTROLLER_SPECIFIED = new Error("No controller specified", 2);
    private static final Error CONTROLLER_DOES_NOT_EXISTS = new Error("No controller specified", 3);
    private static final Error NO_VALUE_SPECIFIED = new Error("No value specified", 4);
    private static final Error UNEXPECTED_METHOD = new Error("Unexpected method", 5);
    private static final Error UNEXPECTED_EXCEPTION = new Error("Unexpected exception", 6);

    private final QueryResultPrinter resultPrinter;
    private final Gson gson;
    private final PrintStream out;

    public JsonRequestResponseHandler(final PrintStream out) {
        this.resultPrinter = new JsonQueryResultPrinter();
        this.gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        this.out = out;
    }

    @Override
    public void handle(final String request, final TrainSimulatorApi api) {
        try {
            new Handler(request, api).handle();
        } catch (final JsonRequestResponseHandlerException e) {
            out.println(resultPrinter.error(e.error));
        } catch (final Exception e) {
            out.println(resultPrinter.error(UNEXPECTED_EXCEPTION));
        }
    }

    private final class Handler {
        private final MethodCall methodCall;
        private final TrainSimulatorApi api;

        private Handler(final String request, final TrainSimulatorApi api) throws JsonRequestResponseHandlerException {
            try {
                methodCall = gson.fromJson(request, MethodCall.class);
            } catch (final JsonSyntaxException e) {
                throw new JsonRequestResponseHandlerException(MALFORMED_JSON);
            }

            this.api = api;
        }

        private void handle() throws JsonRequestResponseHandlerException {
            final Method method = methodCall.method;

            if (method == null) {
                throw new JsonRequestResponseHandlerException(UNEXPECTED_METHOD);
            }

            switch (method) {
                case NAME:
                    name();
                    return;
                case LIST:
                    list();
                    return;
                case VALUE:
                    value();
                    return;
                case MIN:
                    min();
                    return;
                case MAX:
                    max();
                    return;
                case EXISTS:
                    exists();
                    return;
                case SET:
                    set();
                    return;
                default:
                    throw new JsonRequestResponseHandlerException(UNEXPECTED_METHOD);
            }
        }

        private void value() throws JsonRequestResponseHandlerException {
            final Controller controller = getController();

            out.println(resultPrinter.value(api.getControllerValue(controller)));
        }

        private void min() throws JsonRequestResponseHandlerException {
            final Controller controller = getController();

            out.println(resultPrinter.min(api.getControllerMinValue(controller)));
        }

        private void max() throws JsonRequestResponseHandlerException {
            final Controller controller = getController();

            out.println(resultPrinter.max(api.getControllerMinValue(controller)));
        }

        private void exists() throws JsonRequestResponseHandlerException {
            final Optional<Controller> controller = findController();

            out.println(resultPrinter.exists(controller.isPresent()));
        }

        private void set() throws JsonRequestResponseHandlerException {
            final Controller controller = getController();

            if (!methodCall.params.isValueInformationPresent()) {
                throw new JsonRequestResponseHandlerException(NO_VALUE_SPECIFIED);
            }

            api.setControllerValue(controller, methodCall.params.value);

            out.println(resultPrinter.set(methodCall.params.value));
        }

        private Controller getController() throws JsonRequestResponseHandlerException {
            final Optional<Controller> controller = findController();

            if (controller.isEmpty()) {
                throw new JsonRequestResponseHandlerException(CONTROLLER_DOES_NOT_EXISTS);
            }

            return controller.get();
        }

        private Optional<Controller> findController() throws JsonRequestResponseHandlerException {
            if (!methodCall.params.isControllerInformationPresent()) {
                throw new JsonRequestResponseHandlerException(NO_CONTROLLER_SPECIFIED);
            }

            if (methodCall.params.controllerName != null) {
                return api.findControllerByName(methodCall.params.controllerName);
            }

            if (methodCall.params.controllerId != null) {
                return api.findControllerById(methodCall.params.controllerId);
            }

            return Optional.empty();
        }

        private void list() {
            out.println(resultPrinter.list(api.getControllerList()));
        }

        private void name() {
            out.println(resultPrinter.name(api.getLocoName()));
        }
    }

    private static class MethodCall {
        private final Method method;

        private final Params params;

        private MethodCall(final Method method, final Params params) {
            this.method = method;
            this.params = params;
        }

        @Override
        public String toString() {
            return "Methods{" +
                    "method='" + method + '\'' +
                    ", params=" + params +
                    '}';
        }

        private static class Params {
            private final Integer controllerId;
            private final String controllerName;

            private final Float value;

            private Params(final Integer controllerId, final String controllerName, final Float value) {
                this.controllerId = controllerId;
                this.controllerName = controllerName;
                this.value = value;
            }

            @Override
            public String toString() {
                return "Params{" +
                        "controllerId=" + controllerId +
                        ", controllerName='" + controllerName + '\'' +
                        ", value=" + value +
                        '}';
            }

            private boolean isControllerInformationPresent() {
                return controllerId != null || controllerName != null;
            }

            private boolean isValueInformationPresent() {
                return value != null;
            }
        }
    }

    private static class JsonRequestResponseHandlerException extends Exception {
        private final Error error;

        private JsonRequestResponseHandlerException(final Error error) {
            super(error.getMessage());
            this.error = error;
        }
    }
}
