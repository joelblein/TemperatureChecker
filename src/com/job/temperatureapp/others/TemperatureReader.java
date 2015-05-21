/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp.others;

import com.job.probemanager.ProbeException;
import com.job.probemanager.ProbeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author joel
 */
public class TemperatureReader {
    private final EnceinteFrigorifique enceinte;
    private final String probe;
     
    public TemperatureReader(EnceinteFrigorifique enceinte, String probe) {
        this.enceinte = enceinte;
        this.probe = probe;
    }
    
    public void read() throws ProbeException {
        ReleveTemperature rt = new ReleveTemperature(new Date(),ProbeManager.getInstance().readProbe(probe));
        enceinte.ajouterReleve(rt);
    }
    
    public void log() {
        ReleveTemperature rt = enceinte.dernierReleve();
        twitt(enceinte.getNom()+" "+rt.getDate()+" "+rt.getTemperature()+"°C");
    }
    public void twitt(String message) {
   
    }
    
    
    
    
    
    private final Collection<TemperatureListener> temperatureListeners = new ArrayList<>();

    public void addTemperatureListener(TemperatureListener listener) {
        temperatureListeners.add(listener);
    }
 
    public void removeTemperatureListener(TemperatureListener listener) {
        temperatureListeners.remove(listener);
    }
 
    public TemperatureListener[] getTemperatureListeners() {
        return temperatureListeners.toArray(new TemperatureListener[0]);
    } 

     void fireTemperatureChanged(EnceinteFrigorifique ef, ReleveTemperature rt) throws Exception {
        for(TemperatureListener listener : temperatureListeners) {            listener.temperatureChangee(ef, rt);
        }
    }

}
