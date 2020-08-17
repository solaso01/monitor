package com.sjzy.data.monitor.pojo;

import java.util.Objects;

public class JobInfoDemo {

    String pid;
    String name;

    public JobInfoDemo(String pid, String name) {
        this.pid = pid;
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobInfoDemo that = (JobInfoDemo) o;
        return Objects.equals(pid, that.pid) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, name);
    }

    @Override
    public String toString() {
        return "JobInfoDemo{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
