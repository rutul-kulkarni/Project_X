package com.example.project_x;

public class LiveDonors {
    private double Latitude,Longitude;
    private  String itemName,donorName,donorMobile,quantityQuantity;

    public LiveDonors() {
    }

    public LiveDonors(double latitude, double longitude,String itemName, String donorName, String donorMobile, String quantityQuantity) {
        Latitude = latitude;
        Longitude = longitude;

        this.itemName = itemName;
        this.donorName = donorName;
        this.donorMobile = donorMobile;
        this.quantityQuantity = quantityQuantity;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorMobile() {
        return donorMobile;
    }

    public void setDonorMobile(String donorMobile) {
        this.donorMobile = donorMobile;
    }

    public String getQuantityQuantity() {
        return quantityQuantity;
    }

    public void setQuantityQuantity(String quantityQuantity) {
        this.quantityQuantity = quantityQuantity;
    }
}
