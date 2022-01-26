package org.routing.model;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // assuming coordinates in RD
//    public double distance(Point other) {
//        return Math.sqrt(((this.x - other.x) * (this.x - other.x)) + ((this.y - other.y) * (this.y - other.y)));
//    }

    public double distance(Point other) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(other.y-this.y);
        double dLng = Math.toRadians(other.x-this.x);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(this.y)) * Math.cos(Math.toRadians(other.y)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    @Override
    public String toString() {
        return "{" +
                "x: " + x +
                ", y: " + y +
                '}';
    }
}
