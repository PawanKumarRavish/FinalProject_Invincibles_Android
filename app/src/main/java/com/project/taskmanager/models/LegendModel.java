package com.project.taskmanager.models;

/**
 * Created by Gaganjot Singh on 04/05/2019.
 */

public class LegendModel {

    String legendName;

    public LegendModel(String legendName) {
        this.legendName = legendName;
    }

    public String getLegendName() {
        return legendName;
    }

    public void setLegendName(String legendName) {
        this.legendName = legendName;
    }
}
