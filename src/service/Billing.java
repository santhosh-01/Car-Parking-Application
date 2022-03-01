package service;

public class Billing {

    public int calculateBill(long seconds) {
        return (int)Math.round(seconds * 0.5);
    }

}
