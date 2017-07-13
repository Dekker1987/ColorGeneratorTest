package com.example.sergii.colorgeneratortest.models;

/**
 * Created by Sergii on 11.07.2017.
 */

public class ColorModel {

    private String hexColorValue;

    public String getHexColorValue() {
        return hexColorValue;
    }

    public void setHexColorValue(String hexColorValue) {
        this.hexColorValue = hexColorValue;
    }

    public String toString(){
        return hexColorValue;
    }
}
