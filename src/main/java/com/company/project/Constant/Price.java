package com.company.project.Constant;

public class Price {
    private static  Double BnxPrice;
    private static Double GoldPrice;

    public static Double getBnxPrice() {
        return BnxPrice;
    }

    public static void setBnxPrice(Double bnxPrice) {
        BnxPrice = bnxPrice;
    }

    public static Double getGoldPrice() {
        return GoldPrice;
    }

    public static void setGoldPrice(Double goldPrice) {
        GoldPrice = goldPrice;
    }
}
