/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp;

/**
 *
 * @author joel
 */
public interface TemperatureListener {
    public void temperatureChangee(EnceinteFrigorifique ef, ReleveTemperature rt) throws Exception;
}