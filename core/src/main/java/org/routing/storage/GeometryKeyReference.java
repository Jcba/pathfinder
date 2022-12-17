package org.routing.storage;

public class GeometryKeyReference implements KeyProvider {

    private final String type;
    private final long id;

    /**
     * Optional, position between 0 and 1. Indicates the relative position of the match along the geometry
     */
    private double position;

    public GeometryKeyReference(String type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GeometryKeyReference{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", position=" + position +
                '}';
    }
}
