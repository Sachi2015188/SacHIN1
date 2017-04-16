package com.example.hp.myapplicationgw;

/**
 * Created by Thevaki on 4/15/2017.
 */

public class FirebaseDB {

    private String date;
    private String timeOn;
    private String tempOn;
    private String timeOff;
    private String tempOff;

    public FirebaseDB(){

    }

    public FirebaseDB(String date, String timeOn, String tempOn,String timeOff,String tempOff) {
        this.date = date;
        this.timeOn = timeOn;
        this.tempOn = tempOn;
        this.timeOff=timeOff;
        this.tempOff=tempOff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeOn() {
        return timeOn;
    }

    public void setTimeOn(String timeOn) {
        this.timeOn = timeOn;
    }

    public String getTempOn() {
        return tempOn;
    }

    public void setTempOn(String tempOn) {
        this.tempOn = tempOn;
    }

    public String getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(String timeOff) {
        this.timeOff = timeOff;
    }

    public String getTempOff() {
        return tempOff;
    }

    public void setTempOff(String tempOff) {
        this.tempOff = tempOff;
    }

}
