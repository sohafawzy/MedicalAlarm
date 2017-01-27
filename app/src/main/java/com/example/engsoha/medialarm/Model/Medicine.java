package com.example.engsoha.medialarm.Model;


public class Medicine {
    int id;
    private String medicine_name;
    private String dose_value;
    private String dose_unit;
    private String date_time;
    private String interval;
    private String note;
    private byte[] image;

    //Constructors
    public Medicine() {
    }

    public Medicine(String medicine_name, String dose_value, String dose_unit, String date_time, String interval, String note, byte[] image) {
        this.medicine_name = medicine_name;
        this.dose_value = dose_value;
        this.dose_unit = dose_unit;
        this.date_time = date_time;
        this.interval = interval;
        this.note = note;
        this.image = image;
    }

    public Medicine(int id, String medicine_name, String dose_value, String dose_unit, String date_time, String interval, String note, byte[] image) {
        this.id = id;
        this.medicine_name = medicine_name;
        this.dose_value = dose_value;
        this.dose_unit = dose_unit;
        this.date_time = date_time;
        this.interval = interval;
        this.note = note;
        this.image = image;
    }

    //getters

    public int getId() {
        return id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getDose_value() {
        return dose_value;
    }

    public String getDose_unit() {
        return dose_unit;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getInterval() {
        return interval;
    }

    public String getNote() {
        return note;
    }

    public byte[] getImage() {
        return image;
    }

    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public void setDose_value(String dose_value) {
        this.dose_value = dose_value;
    }

    public void setDose_unit(String dose_unit) {
        this.dose_unit = dose_unit;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

