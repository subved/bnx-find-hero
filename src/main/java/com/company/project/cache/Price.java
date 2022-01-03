package com.company.project.cache;

public class Price {
    private static  Double BnxPrice;
    private static Double GoldPrice;
    private static Double CrystalPrice;

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

    public static Double getCrystalPrice() {
        return CrystalPrice;
    }

    public static void setCrystalPrice(Double crystalPrice) {
        CrystalPrice = crystalPrice;
    }
}
