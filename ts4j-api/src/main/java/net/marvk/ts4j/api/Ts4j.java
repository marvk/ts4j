package net.marvk.ts4j.api;

import net.marvk.ts4j.api.raildriver.TrainSimulatorRailDriverApi;
import net.marvk.ts4j.raildriver.BuildStep;
import net.marvk.ts4j.raildriver.RailDriverBuilder;
import net.marvk.ts4j.raildriver.RailDriverInstantiationException;

import java.nio.file.Path;

public final class Ts4j {
    private Ts4j() {
        throw new AssertionError("No instances of utility class " + Ts4j.class);
    }

    public static TrainSimulatorApi create(final Path pluginsDirectory, final Path dll) throws TrainSimulatorApiInstantiationException {
        return create(new RailDriverBuilder().withDllName(dll.toString()).withPluginsDirectory(pluginsDirectory));
    }

    public static TrainSimulatorApi createX86(final Path pluginsDirectory) throws TrainSimulatorApiInstantiationException {
        return create(new RailDriverBuilder().withX86Dll().withPluginsDirectory(pluginsDirectory));
    }

    public static TrainSimulatorApi createX86() throws TrainSimulatorApiInstantiationException {
        return create(new RailDriverBuilder().withX86Dll().withDefaultPluginsDirectory());
    }

    public static TrainSimulatorApi createX64(final Path pluginsDirectory) throws TrainSimulatorApiInstantiationException {
        return create(new RailDriverBuilder().withX64Dll().withPluginsDirectory(pluginsDirectory));
    }

    public static TrainSimulatorApi createX64() throws TrainSimulatorApiInstantiationException {
        return create(new RailDriverBuilder().withX64Dll().withDefaultPluginsDirectory());
    }

    private static TrainSimulatorApi create(final BuildStep railDriverBuilder) throws TrainSimulatorApiInstantiationException {
        try {
            return new TrainSimulatorRailDriverApi(railDriverBuilder.build());
        } catch (final RailDriverInstantiationException e) {
            throw new TrainSimulatorApiInstantiationException(e);
        }
    }
}
