package com.sjzy.data.monitor.pojo;

import java.util.Objects;

public class SHDemo {

    Integer roboot;
    Integer threshold;


    public SHDemo(Integer roboot, Integer threshold) {
        this.roboot = roboot;
        this.threshold = threshold;
    }

    public Integer getRoboot() {
        return roboot;
    }

    public void setRoboot(Integer roboot) {
        this.roboot = roboot;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SHDemo shDemo = (SHDemo) o;
        return Objects.equals(roboot, shDemo.roboot) &&
                Objects.equals(threshold, shDemo.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roboot, threshold);
    }

    @Override
    public String toString() {
        return "SHDemo{" +
                "roboot=" + roboot +
                ", threshold=" + threshold +
                '}';
    }
}
