package org.routing.model;

import org.routing.storage.KeyProvider;

import java.io.Serializable;

public class Edge implements Serializable, KeyProvider {
    static String KEY_TYPE = "edge";
    private final long id;
    private Node from;
    private Node to;
    private double cost;

    public Edge(long id, Node from, Node to, double cost) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String getType() {
        return KEY_TYPE;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return getId() == edge.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
