/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probevent.client;

import com.job.probeevent.ProbeEventEncoder;
import com.job.probeevent.ProbeEvent;
import com.job.probeevent.ProbeEventListener;
import com.job.probeevent.ProbeEventXmlEncoder;
import com.job.probeevent.mqtt.ProbeEventMqttReceiver;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;


/**
 * @author joel
 */ 
public class ProbeEventClientApp {
    private static final String PERSISTENCE_DIRECTORY = "XXXXXXXXXX";
    String serverURI;
    String clientId;
    ProbeEventEncoder encoder;
    MqttClient mqttClient = null;
    ProbeEventListener listener;
    MqttConnectOptions connOpts;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if(args.length<3) {
            throw new IllegalArgumentException("Needs args : serverUri clientId listenerClassName");
        }
        String serverURI = args[0];
        String clientId = args[1];
        String listenerClassName = args[2];
        
        Class listenerClass = Class.forName(listenerClassName);
        ProbeEventListener l = (ProbeEventListener)listenerClass.newInstance();
                
        ProbeEventMqttReceiver receiver = new ProbeEventMqttReceiver(serverURI, clientId, new ProbeEventXmlEncoder(), l);

        new Object().wait(); // keep alive...
    }

}
