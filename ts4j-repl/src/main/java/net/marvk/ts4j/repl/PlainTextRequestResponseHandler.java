package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.Controller;
import net.marvk.ts4j.api.TrainSimulatorApi;

import java.util.List;
import java.util.Optional;

public class PlainTextRequestResponseHandler implements RequestResponseHandler {
    private final QueryResultPrinter resultPrinter;

    public PlainTextRequestResponseHandler() {
        this.resultPrinter = new PlainTextQueryResultPrinter();
    }

    @Override
    public void handle(final String request, final TrainSimulatorApi api) {
        new Handler(request, api).handle();
    }

    private class Handler {
        private final Method method;
        private final String[] request;
        private final TrainSimulatorApi api;
        private final String operation;

        private Handler(final String request, final TrainSimulatorApi api) {
            this.request = request.split(" ");
            this.api = api;
            this.operation = this.request[0].trim();
            this.method = Method.valueOf(operation);

        }

        private void handle() {
            if (Method.NAME == method || Method.LIST == method) {
                simpleQuery(method);
            } else {
                if (!verifyLength(request, 2)) {
                    System.err.println("Must specify controller by id or name");
                    return;
                }

                final String controllerIdentification = request[1];
                final Optional<Controller> maybeController = findController(controllerIdentification);

                if (Method.EXISTS == method) {
                    existsQuery(maybeController.isPresent());
                }

                if (maybeController.isEmpty()) {
                    System.err.println("Specified controller " + controllerIdentification + " does not exist");
                    return;
                }

                if (List.of(Method.VALUE, Method.MIN, Method.MAX).contains(method)) {
                    controllerBasedQuery(method, maybeController.get());
                } else if (Method.SET == method) {
                    setOperation(request, maybeController.get(), request[2]);
                } else {
                    System.err.println("Invalid operation " + operation);
                }
            }
        }

        private void setOperation(final String[] fragments, final Controller controller, final String valueString) {
            if (!verifyLength(fragments, 3)) {
                System.err.println("Must specify controller by id or name and value to be set to");
                return;
            }

            final float value;
            try {
                value = Float.parseFloat(valueString);
            } catch (final NumberFormatException e) {
                System.err.println("Third argument must be a valid float, was " + valueString);
                return;
            }

            api.setControllerValue(controller, value);
        }

        private void existsQuery(final boolean controllerExists) {
            System.out.println(resultPrinter.exists(controllerExists));
        }

        private void controllerBasedQuery(final Method operation, final Controller controller) {
            switch (operation) {
                case VALUE:
                    System.out.println(resultPrinter.value(api.getControllerValue(controller)));
                    break;
                case MIN:
                    System.out.println(resultPrinter.min(api.getControllerMinValue(controller)));
                    break;
                case MAX:
                    System.out.println(resultPrinter.max(api.getControllerMaxValue(controller)));
                    break;
            }
        }

        private void simpleQuery(final Method operation) {
            switch (operation) {
                case NAME:
                    System.out.println(resultPrinter.name(api.getLocoName()));
                    break;
                case LIST:
                    System.out.println(resultPrinter.list(api.getControllerList()));
                    break;
            }
        }

        private boolean verifyLength(final String[] fragments, final int length) {
            return fragments.length >= length;
        }

        private Optional<Controller> findController(final String controllerIdentification) {
            try {
                final int id = Integer.parseInt(controllerIdentification);
                return api.findControllerById(id);
            } catch (final NumberFormatException ignored) {
                return api.findControllerByName(controllerIdentification);
            }
        }
    }
}
