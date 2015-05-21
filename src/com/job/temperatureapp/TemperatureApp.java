/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp;

import com.job.probemanager.XmlEncoder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author joel
 */
public class TemperatureApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length<2) {
            throw new RuntimeException("Needs MQTTBroker brokeruri and clientid as arguments");
        }
        String mqttBroketIP = args[0];
        String hostname = args[1];
        
        MessageReceiver messageReceiver = new MessageReceiver(mqttBroketIP, hostname, new XmlEncoder());
        try  {
            messageReceiver.start();

            while(true)  // Endors le thread principal (le timer prend la suite)
                try {
                    Thread.sleep(60*60*1000);
                } catch(InterruptedException e) {
                } 
        } finally {
            messageReceiver.stop(); // se délogue du serveur mqtt
        }
    }
}
    
