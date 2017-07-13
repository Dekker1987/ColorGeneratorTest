package com.example.sergii.colorgeneratortest.utils;

import java.util.ArrayList;
import java.util.List;

public class HexColorValuesGenerator {

    private StringBuilder hexColorValue;
    private int charOrInt;
    private List<Character> charList;
    private List<Integer> integersList;

    public HexColorValuesGenerator(){
        hexColorValue = new StringBuilder();
        charList = new ArrayList<>();
        integersList = new ArrayList<>();

        initCharList();
        initIntegersList();

    }

    private void initCharList(){
        for(char i = 'a';i<='f';i++){
            charList.add(i);
        }
    }

    private void initIntegersList(){
        for(int i=0;i<=9;i++){
            integersList.add(i);
        }
    }

    public String getRandomHexValue(){
        resetValue();
        hexColorValue.append("#");

        for(int j=0;j<6;j++){
            charOrInt = (int)Math.round(Math.random()*1);
            switch(charOrInt){
                case 0:
                    hexColorValue.append(integersList.get(getRandomInt(integersList.size()-1)));
                    break;
                case 1:
                    hexColorValue.append(charList.get(getRandomInt(charList.size()-1)));
                    break;
                default:
                    break;
            }
        }
        return hexColorValue.toString();
    }

    private void resetValue(){
        if(hexColorValue.length()>0){
            hexColorValue.setLength(0);
        }
    }

    private int getRandomInt(int intTo){
        return (int)Math.round(Math.random()*intTo);
    }
}
