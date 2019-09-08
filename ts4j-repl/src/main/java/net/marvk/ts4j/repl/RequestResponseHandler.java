package net.marvk.ts4j.repl;

import net.marvk.ts4j.api.TrainSimulatorApi;

public interface RequestResponseHandler {
    void handle(final String request, final TrainSimulatorApi api);
}
