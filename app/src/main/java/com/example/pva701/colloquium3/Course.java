package com.example.pva701.colloquium3;

/**
 * Created by pva701 on 23.12.14.
 */
public class Course {
    private int id;
    private String name;
    private double val;

    public Course(int id, String name, double val) {
        this.id = id;
        this.name = name;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getVal() {
        return val;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
