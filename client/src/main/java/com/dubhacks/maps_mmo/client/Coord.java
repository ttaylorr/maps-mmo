package com.dubhacks.maps_mmo.client;

public class Coord {
    private final double x;
    private final double y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Coord other = (Coord) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }
}
