package ru.ifmo.md.colloquium3;

/**
 * Created by MSviridenkov on 23.12.2014.
 */
public class Value {
    private String name;
    private double count;

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public String getCount() {
        return String.valueOf(count);
    }

    Value(String name, Double count) {
        this.name = name;
        this.count = count;
    }
}
