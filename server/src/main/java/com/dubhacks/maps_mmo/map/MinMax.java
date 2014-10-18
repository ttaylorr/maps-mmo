package com.dubhacks.maps_mmo.map;

public class MinMax {
    private Double min;
    private Double max;

    public MinMax() {
        this.min = null;
        this.max = null;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public double getRange() {
        return this.max - this.min;
    }

    public void put(double d) {
        if (this.min == null) {
            this.min = d;
        } else if (d < this.min) {
            this.min = d;
        }

        if (this.max == null) {
            this.max = d;
        } else if (d > this.max) {
            this.max = d;
        }
    }
}
