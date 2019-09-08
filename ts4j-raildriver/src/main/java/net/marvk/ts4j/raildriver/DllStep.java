package net.marvk.ts4j.raildriver;

public interface DllStep {
    PluginsDirectoryStep withX86Dll();

    PluginsDirectoryStep withX64Dll();

    PluginsDirectoryStep withDllName(final String name);
}
