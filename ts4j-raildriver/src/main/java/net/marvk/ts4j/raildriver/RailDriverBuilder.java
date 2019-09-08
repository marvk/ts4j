package net.marvk.ts4j.raildriver;

import com.sun.jna.Native;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RailDriverBuilder implements DllStep, PluginsDirectoryStep, BuildStep {
    private static final Path DEFAULT_PLUGINS_DIRECTORY = Paths.get("C:\\Program Files (x86)\\Steam\\steamapps\\common\\RailWorks\\plugins");
    private static final String JNA_LIBRARY_PATH_KEY = "jna.library.path";

    private String dllName;
    private Path pluginsDirectory;

    @Override
    public PluginsDirectoryStep withX86Dll() {
        return withDllName("RailDriver.dll");
    }

    @Override
    public PluginsDirectoryStep withX64Dll() {
        return withDllName("RailDriver64.dll");
    }

    @Override
    public PluginsDirectoryStep withDllName(final String dllName) {
        this.dllName = dllName;
        return this;
    }

    @Override
    public BuildStep withDefaultPluginsDirectory() {
        return withPluginsDirectory(DEFAULT_PLUGINS_DIRECTORY);
    }

    @Override
    public BuildStep withPluginsDirectory(final Path path) {
        this.pluginsDirectory = path;
        return this;
    }

    @Override
    public RailDriver build() throws RailDriverInstantiationException {
        try {
            System.setProperty(JNA_LIBRARY_PATH_KEY, pluginsDirectory.toAbsolutePath().toString());

            return Native.load(dllName, RailDriver.class);
        } catch (final Exception e) {
            throw new RailDriverInstantiationException(e);
        }
    }
}
