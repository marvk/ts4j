package net.marvk.ts4j.raildriver;

public interface BuildStep {
    RailDriver build() throws RailDriverInstantiationException;
}
