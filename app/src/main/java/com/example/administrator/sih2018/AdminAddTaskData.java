package com.example.administrator.sih2018;

/**
 * Created by Administrator on 7/15/2017.
 */

public class AdminAddTaskData {
    public String description;
    public String deadLine;
    public String phaseSelected;
    // String address;
    public String type;
    public String status;
    public String adminuid;
    public int threshold;
    public String quarter;
    public AdminAddTaskData() {
        //Emptyconstructor
    }

    public AdminAddTaskData(String description,String deadLine,String phaseSelected,String quarter,int threshold, String type, String status,String adminuid) {
        this.description = description;
        this.deadLine=deadLine;
        this.phaseSelected=phaseSelected;
//        this.geolong = geolong;
//        this.address = address;
        this.type = type;
        this.status = status;
        this.adminuid=adminuid;
        this.quarter=quarter;
        this.threshold=threshold;
    }

    public String getDescription() {
        return this.description;
    }

  //  public String getGeolat() {
        //return this.geolat;
    //}

//    public String getGeolong() {
//        return this.geolong;
//    }
//
//    public String getAddress() {
//        return this.address;
//    }

    public String getType() {
        return this.type;
    }

    public String getStatus() {
        return this.status;
    }

    public String getAdminuid(){
        return this.adminuid;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getPhaseSelected() {
        return phaseSelected;
    }

    public int getThreshold() {
        return threshold;
    }

    public String getQuarter() {
        return quarter;
    }
}
