/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp.others;

import java.util.Stack;

/**
 *
 * @author joel
 */
public class EnceinteFrigorifique {
    private String nom = "";
    private float minTemperature;
    private float maxTemperature;
    private Stack releves = new Stack();
    public EnceinteFrigorifique(String nom, float minTemperature, float maxTemperature) {
        this.nom = nom;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the minTemperature
     */
    public float getMinTemperature() {
        return minTemperature;
    }

    /**
     * @param minTemperature the minTemperature to set
     */
    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * @return the maxTemperature
     */
    public float getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * @param maxTemperature the maxTemperature to set
     */
    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    
    public void ajouterReleve(ReleveTemperature rt) {
        releves.push(rt);
    }
    public ReleveTemperature dernierReleve() {
        return (ReleveTemperature)releves.peek();
    }
}
