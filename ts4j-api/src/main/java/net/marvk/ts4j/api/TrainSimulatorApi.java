package net.marvk.ts4j.api;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface TrainSimulatorApi {
    boolean isConnected();

    void setConnected(final boolean connected);

    String getLocoName();

    List<Controller> getControllerList();

    float getControllerValue(final Controller controller);

    default float getControllerValue(final int id) {
        return getControllerValue(findControllerById(id).orElseThrow(() -> new ControllerNotFoundException(id)));
    }

    default float getControllerValue(final String name) {
        return getControllerValue(findControllerByName(name).orElseThrow(() -> new ControllerNotFoundException(name)));
    }

    float getControllerMinValue(final Controller controller);

    default float getControllerMinValue(final int id) {
        return getControllerMinValue(findControllerById(id).orElseThrow(() -> new ControllerNotFoundException(id)));
    }

    default float getControllerMinValue(final String name) {
        return getControllerMinValue(findControllerByName(name).orElseThrow(() -> new ControllerNotFoundException(name)));
    }

    float getControllerMaxValue(final Controller controller);

    default float getControllerMaxValue(final int id) {
        return getControllerMaxValue(findControllerById(id).orElseThrow(() -> new ControllerNotFoundException(id)));
    }

    default float getControllerMaxValue(final String name) {
        return getControllerMaxValue(findControllerByName(name).orElseThrow(() -> new ControllerNotFoundException(name)));
    }

    void setControllerValue(final Controller controller, final float value);

    default void setControllerValue(final int id, final float value) {
        setControllerValue(findControllerById(id).orElseThrow(() -> new ControllerNotFoundException(id)), value);
    }

    default void setControllerValue(final String name, final float value) {
        setControllerValue(findControllerByName(name).orElseThrow(() -> new ControllerNotFoundException(name)), value);
    }

    default Optional<Controller> findControllerById(final int id) {
        return getControllerList().stream()
                                  .filter(controller -> controller.getId() == id)
                                  .findFirst();
    }

    default Optional<Controller> findControllerByName(final String name) {
        return getControllerList().stream()
                                  .filter(controller -> Objects.equals(controller.getName(), name))
                                  .findFirst();
    }

    default boolean isControllerWithIdPresent(final int id) {
        return findControllerById(id).isPresent();
    }

    default boolean isControllerWithNamePresent(final String name) {
        return findControllerByName(name).isPresent();
    }
}
