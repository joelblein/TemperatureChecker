/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp.others;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 *
 * @author joel
 */
public class ReleveTemperature {
    private Date date;
    private float temperature;
            
    public ReleveTemperature(Date date, float temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the temperature
     */
    public float getTemperature() {
        return temperature;
    }
    
    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.HALF_EVEN);
        nf.setMaximumFractionDigits(1);

        return DateFormat.getInstance().format(date)+" "+nf.format(temperature)+"°C";        
    }
}
