package net.marvk.ts4j.api;

public class SimpleController implements Controller {
    private final int id;
    private final String name;

    public SimpleController(final int id, final String name) {
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
        return "SimpleController{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
