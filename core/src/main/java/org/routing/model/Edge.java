package org.routing.model;

import org.routing.geometries.LineString;

import java.io.Serializable;

public class Edge implements Serializable {
    private Node from;
    private Node to;
    private double cost;

    private LineString geometry;

    public Edge(Node from, Node to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public Edge(Node from, Node to, double cost, LineString geometry) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.geometry = geometry;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (Double.compare(edge.getCost(), getCost()) != 0) return false;
        if (getFrom() != null ? !getFrom().equals(edge.getFrom()) : edge.getFrom() != null) return false;
        return getTo() != null ? getTo().equals(edge.getTo()) : edge.getTo() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        temp = Double.doubleToLongBits(getCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public LineString getGeometry() {
        return geometry;
    }
}
