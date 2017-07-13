package com.example.sergii.colorgeneratortest.helpers;

import android.os.Handler;
import android.os.Message;
import com.example.sergii.colorgeneratortest.models.ColorModel;
import com.example.sergii.colorgeneratortest.utils.HexColorValuesGenerator;

/**
 * Created by Sergii on 12.07.2017.
 */

public class ColorGeneratorHelper implements Runnable {

    private HexColorValuesGenerator hexColorValuesGenerator;
    private Handler handler;
    private boolean suspended;
    private boolean stopped;
    private final int incrDecrValue;
    private int delayValue = 800;

    public ColorGeneratorHelper(){
        incrDecrValue = 50;
        stopped = false;
        suspended = false;
        hexColorValuesGenerator = new HexColorValuesGenerator();
    }

    public void addHandler(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        while(!stopped){
            generateHexColorNum();
            makeDelay(delayValue);
            synchronized(this) {
                while(suspended) {
                    try{
                        wait();
                    }catch(InterruptedException exc){
                        exc.printStackTrace();
                    }
                }
            }
        }
    }

    private void makeDelay(int delayVal){
        try {
            Thread.sleep(delayVal);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void generateHexColorNum(){
        Message msg = Message.obtain();
        msg.obj = new ColorModel() {{
            setHexColorValue(hexColorValuesGenerator.getRandomHexValue());
        }};

        if(handler!=null){
            handler.sendMessage(msg);
        }
    }

    public void stopGeneratingThread(){
        stopped = true;
    }

    public void suspend() {
        suspended = true;
    }

    public void incrementDelay(){
        if(delayValue!=3000){
            delayValue+=incrDecrValue;
        }
    }

    public void decrementDelay(){
        if(delayValue!=50){
            delayValue-=incrDecrValue;
        }
    }

    synchronized public void resume() {
        suspended = false;
        notify();
    }
}
