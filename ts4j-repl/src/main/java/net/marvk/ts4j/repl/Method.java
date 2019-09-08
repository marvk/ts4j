package net.marvk.ts4j.repl;

import com.google.gson.annotations.SerializedName;

public enum Method {
    @SerializedName("name")
    NAME("name"),
    @SerializedName("list")
    LIST("list"),
    @SerializedName("value")
    VALUE("value"),
    @SerializedName("min")
    MIN("min"),
    @SerializedName("max")
    MAX("max"),
    @SerializedName("exists")
    EXISTS("exists"),
    @SerializedName("set")
    SET("set");

    private final String name;

    Method(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
