package ru.ifmo.md.colloquium3;

/**
 * Created by sultan on 23.12.14.
 */
public class CurrencyItem {
    private long id;
    private String currency;
    private double rate;
    private double sum;

    public CurrencyItem(String currency, double rate, double sum) {
        this.id = 0;
        this.currency = currency;
        this.rate = rate;
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
