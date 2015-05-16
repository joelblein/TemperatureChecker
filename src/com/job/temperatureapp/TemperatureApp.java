/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp;

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
        if(args.length<1) {
            throw new RuntimeException("Needs MQTTBroker ip as an argument");
        }
        String mqttBroketIP = args[0];

        String hostname = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        try  {
            MqttClient mqttClient = new MqttClient(mqttBroketIP, hostname);
            mqttClient.setCallback(new MessageReceiver());
            mqttClient.connect();    
            mqttClient.subscribe("cafetiton/temperature");
            mqttClient.disconnect();
        } catch (IllegalArgumentException iae) {
            System.out.println("Invalid parameters to connect MQTT server (serverURI="+mqttBroketIP+", clientId="+hostname+")"); 
            iae.printStackTrace();
        } catch (MqttException me) {
            System.out.println("Unable to connect MQTT server (serverURI="+mqttBroketIP+", clientId="+hostname+")"); 
            me.printStackTrace();
        }

    }
    
}
