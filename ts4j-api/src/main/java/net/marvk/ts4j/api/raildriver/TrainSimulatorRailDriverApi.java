package net.marvk.ts4j.api.raildriver;

import net.marvk.ts4j.api.Controller;
import net.marvk.ts4j.api.SimpleController;
import net.marvk.ts4j.api.TrainSimulatorApi;
import net.marvk.ts4j.api.VirtualController;
import net.marvk.ts4j.raildriver.RailDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainSimulatorRailDriverApi implements TrainSimulatorApi {
    private static final List<VirtualController> VIRTUAL_CONTROLLERS = Arrays.asList(VirtualController.values());
    private static final Pattern DIVIDER = Pattern.compile("::");

    private final RailDriver railDriver;

    public TrainSimulatorRailDriverApi(final RailDriver railDriver) {
        this.railDriver = railDriver;
    }

    @Override
    public boolean isConnected() {
        return railDriver.GetRailDriverConnected() && railDriver.GetRailSimConnected();
    }

    @Override
    public void setConnected(final boolean connected) {
        railDriver.SetRailDriverConnected(connected);
        railDriver.SetRailSimConnected(connected);
    }

    @Override
    public String getLocoName() {
        return railDriver.GetLocoName();
    }

    @Override
    public List<Controller> getControllerList() {
        final String[] controllerNames = DIVIDER.split(railDriver.GetControllerList());

        if (controllerNames.length == 1) {
            return new ArrayList<>(List.of(new SimpleController(0, controllerNames[0])));
        }

        final int resultLength = controllerNames.length + VIRTUAL_CONTROLLERS.size();

        final List<Controller> result =
                IntStream.range(0, controllerNames.length)
                         .mapToObj(i -> new SimpleController(i, controllerNames[i]))
                         .collect(Collectors.toCollection(() -> new ArrayList<>(resultLength)));

        result.addAll(VIRTUAL_CONTROLLERS);

        return result;
    }

    @Override
    public float getControllerValue(final Controller controller) {
        return getControllerValue0(controller, GetType.CURRENT);
    }

    @Override
    public float getControllerMinValue(final Controller controller) {
        return getControllerValue0(controller, GetType.MINIMUM);
    }

    @Override
    public float getControllerMaxValue(final Controller controller) {
        return getControllerValue0(controller, GetType.MAXIMUM);
    }

    @Override
    public void setControllerValue(final Controller controller, final float value) {
        railDriver.SetControllerValue(controller.getId(), value);
    }

    private float getControllerValue0(final Controller controller, final GetType current) {
        return railDriver.GetControllerValue(controller.getId(), current.getValue());
    }

    private enum GetType {
        CURRENT(0),
        MINIMUM(1),
        MAXIMUM(2);

        private final int value;

        GetType(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}












