package ru.ifmo.md.colloquium3;

/**
 * Created by anton on 23/12/14.
 */
public class Rate {
    public static final Rate[] PREINSTALLED_RATES = new Rate[] {
        new Rate("USD", 35.0),
        new Rate("EUR", 51.2),
        new Rate("GBP", 55.6)
    };

    public String curr;
    public double rate;

    public Rate(String curr, double rate) {
        this.curr = curr;
        this.rate = rate;
    }
}
