package com.example.sushiplatebillcalculator;

public class SushiRow {

    private int colorSelected;
    private int numPlates;
    private double price;
    private double rowTotal;

    public SushiRow() {

        this.colorSelected = 0;
        this.numPlates = 0;
        this.price = 0;
        this.rowTotal = 0;
    }

    public SushiRow(int color, double price) {

        this.colorSelected = color;
        this.numPlates = 0;
        this.price = price;
        this.rowTotal = 0;
    }

    public int getColorSelected() {
        return colorSelected;
    }

    public int getNumPlates() {
        return numPlates;
    }

    public double getPrice() {
        return price;
    }

    public double getRowTotal() {
        return rowTotal;
    }

    public void setColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
    }

    public void setNumPlates(int numPlates) {
        this.numPlates = numPlates;
        updateRowTotal();
    }

    public void setPrice(double price) {
        this.price = price;
        updateRowTotal();
    }

    // Since rowTotal is exclusively based on numPlates * price, it gets updated every time that
    // price or numPlates are updated.
    private void updateRowTotal() {
        rowTotal = numPlates * price;
    }
}
