package com.job.temperatureapp;

import com.job.probemanager.ProbeEncoder;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @author joel
 */ 
public class MessageReceiver implements MqttCallback {

    String serverURI;
    String clientId;
    ProbeEncoder encoder;
    MqttClient mqttClient = null;

    public MessageReceiver(String serverURI, String clientId, ProbeEncoder encoder) {
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.encoder = encoder;
    }
            
    synchronized protected MqttClient getMqttClient() throws MqttException {
        if(mqttClient == null) {
            try  {
                    System.out.println("Trying to connect MQTT server at "+serverURI+" with clientId "+clientId);
                    mqttClient = new MqttClient(serverURI, clientId);
                    mqttClient.setCallback(this);
                    mqttClient.connect();
                    System.out.println("Connected to mqtt server");
            } catch (IllegalArgumentException iae) {
                System.out.println("Unable to connect MQTT server : Invalid parameters (serverURI="+serverURI+", clientId="+clientId+")"); 
                iae.printStackTrace();
            }    
        }
        return mqttClient;
    }

    public synchronized void start() {
        try {
            getMqttClient();
            mqttClient.subscribe("cafetiton/temperature");
        } catch (MqttException me) {
            System.out.println("Unable to start MQTT server : "+me); 
            me.printStackTrace();
        }    
    }

    public synchronized void stop() {
        try  {
            if(mqttClient!=null) {
                mqttClient.disconnect();
            }
        } catch (MqttException me) {
            System.out.println("Unable to stop MQTT server : "+me); 
            me.printStackTrace();
        } finally {
            mqttClient = null;
        }
    }

    @Override
    public synchronized void connectionLost(Throwable thrwbl) {
        System.out.println("Connection to mqtt server lost :"+thrwbl);
        mqttClient = null;
        start();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mm) throws Exception {
        //System.out.println("Message reçu : "+new String(mm.getPayload()));
        System.out.println(encoder.decode(mm.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }
    
}
