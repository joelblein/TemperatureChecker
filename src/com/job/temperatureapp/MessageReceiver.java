/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp;

import com.job.probemanager.ProbeEncoder;
import com.job.probemanager.ProbeEvent;
import com.job.probemanager.ProbeEventListener;
import com.job.probemanager.XmlEncoder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
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
public abstract class MessageReceiver implements MqttCallback {
    private static final String PERSISTENCE_DIRECTORY = "XXXXXXXXXX";
    String serverURI;
    String clientId;
    ProbeEncoder encoder;
    MqttClient mqttClient = null;
    ProbeEventListener listener;
    MqttConnectOptions connOpts;
    
    public MessageReceiver(String serverURI, String clientId, ProbeEncoder encoder) throws MqttException, IllegalArgumentException {
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.encoder = encoder;
        this.listener = listener;
        
        MqttClientPersistence persistence = new MqttDefaultFilePersistence(PERSISTENCE_DIRECTORY);
        try {
            mqttClient = new MqttClient(serverURI, clientId, persistence);
            mqttClient.setCallback(this);
            System.out.println("Created MqttClient : "+clientId);
        } catch (MqttException me) {
            System.out.println("Unable to create MQTT client"+clientId); 
            throw me;
        }

        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false); // default is true
        connOpts.setKeepAliveInterval(60); // default is 60
        connOpts.setConnectionTimeout(30); // default is 30
        //connOpts.setServerURIs({"tcp://128.0.0.100:1883", "tcp://128.0.0.43:1883"}); // define one or more mqttserver uris
        //connOpts.setWill("", "".getBytes(), 2, true);
    }
            
    public synchronized void start() throws MqttException {
        System.out.print("Trying to connect to MqttServer "+serverURI+"... ");
        System.out.flush();
        do {
            try {
                mqttClient.connect(connOpts);
            } catch(Throwable t) {
                System.out.print("KO... ");
                System.out.flush();
            }
        } while(!mqttClient.isConnected());
        System.out.println("OK!");
        System.out.println("Connected to MqttServer : "+serverURI);
    }

    public synchronized void stop() {
        try  {
            if(mqttClient!=null) {
                System.out.print("Trying to disconnect from MqttServer "+serverURI+"... ");
                System.out.flush();
                mqttClient.disconnect();
                System.out.println("OK!");
            }
        } catch (MqttException me) {
            System.out.println("KO!");
            
            System.out.print("Trying to disconnect forcibly... "); 
            System.out.flush();
            try {
                mqttClient.disconnectForcibly();
                System.out.println("OK!");
            } catch(Throwable t) {
                System.out.println("KO!");
            }
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        System.out.println("Connection to mqtt server "+serverURI+" lost.");
        System.out.print("Trying to reconnect... ");
        do {
            try {
                start();
            } catch(Throwable t) {
                System.out.print("KO ");
                System.out.flush();
            }
        } while(!mqttClient.isConnected());
        System.out.print("OK!");
    }

    @Override
    public abstract void messageArrived(String topic, MqttMessage mm) throws Exception;

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }
}
