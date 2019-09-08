package net.marvk.ts4j.api;

public enum VirtualController implements Controller {
    LATITUDE_OF_TRAIN(400, "LatitudeOfTrain"),
    LONGITUDE_OF_TRAIN(401, "LongitudeOfTrain"),
    FUEL_LEVEL(402, "FuelLevel"),
    IS_IN_A_TUNNEL(403, "IsInATunnel"),
    GRADIENT(404, "Gradient"),
    HEADING(405, "Heading"),
    TIME_OF_DAY_HOURS(406, "TimeOfDayHours"),
    TIME_OF_DAY_MINUTES(407, "TimeOfDayMinutes"),
    TIME_OF_DAY_SECONDS(408, "TimeOfDaySeconds");

    private final int id;
    private final String name;

    VirtualController(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VirtualController{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
