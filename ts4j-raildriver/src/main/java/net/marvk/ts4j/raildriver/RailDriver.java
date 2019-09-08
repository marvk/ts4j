package net.marvk.ts4j.raildriver;

import com.sun.jna.Library;

@SuppressWarnings("ALL")
public interface RailDriver extends Library {
    String GetLocoName();

    String GetControllerList();

    float GetControllerValue(final int controllerId, final int getType);

    void SetControllerValue(final int controllerId, final float value);

    void SetRailDriverConnected(final boolean connected);

    boolean GetRailDriverConnected();

    void SetRailSimConnected(final boolean connected);

    boolean GetRailSimConnected();
}
