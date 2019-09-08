package net.marvk.ts4j.raildriver;

import java.nio.file.Path;

public interface PluginsDirectoryStep {
    BuildStep withDefaultPluginsDirectory();

    BuildStep withPluginsDirectory(final Path path);
}
