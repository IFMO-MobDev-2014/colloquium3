package ru.ifmo.mobdev.colloquium3;

/**
 * @author sugakandrey
 */
public class Currency {
    private double rate;
    private String name;
    private Integer amount;

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Currency(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }

    public Currency(double rate, String name, Integer amount) {
        this.rate = rate;
        this.name = name;
        this.amount = amount;
    }

    public Currency(double rate, String name) {
        this.rate = rate;
        this.name = name;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Integer getAmount() {
        return amount;
    }

    public double getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

