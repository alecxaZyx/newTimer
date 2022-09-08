package com.example.newtimer;

public class PresetModel {
    int ID;
    int PreHrs;
    int PreMins;
    int PreSecs;

    public PresetModel(int ID, int preHrs, int preMins, int preSecs) {
        this.ID = ID;
        PreHrs = preHrs;
        PreMins = preMins;
        PreSecs = preSecs;
    }

    public PresetModel() {
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPreHrs() {
        return PreHrs;
    }

    public void setPreHrs(int preHrs) {
        PreHrs = preHrs;
    }

    public int getPreMins() {
        return PreMins;
    }

    public void setPreMins(int preMins) {
        PreMins = preMins;
    }

    public int getPreSecs() {
        return PreSecs;
    }

    public void setPreSecs(int preSecs) {
        PreSecs = preSecs;
    }
}
