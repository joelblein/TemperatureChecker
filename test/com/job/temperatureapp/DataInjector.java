/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.temperatureapp;

import com.job.probemanager.ProbeConnectionEvent;
import com.job.probemanager.ProbeDisconnectionEvent;
import com.job.probemanager.ProbeEvent;
import com.job.probemanager.ProbeEventsDataSet;
import com.job.probemanager.ProbeFailedEvent;
import com.job.probemanager.ProbeReadEvent;
import com.job.probemanager.XmlEncoder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author joel
 */
public class DataInjector {
    
    public DataInjector() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void injectDataToMQTT() throws Exception {
        String filePath = "./test1dataset.xml";
        URL url = this.getClass().getResource(filePath);
        System.out.println("Trying to open file "+url);
        InputStream input = url.openStream();

        JAXBContext jaxbcontext = JAXBContext.newInstance(
            ProbeEventsDataSet.class, 
            ProbeConnectionEvent.class,
            ProbeDisconnectionEvent.class,
            ProbeFailedEvent.class,
            ProbeReadEvent.class);
        Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
        ProbeEventsDataSet probeEventsDataSet = (ProbeEventsDataSet)unmarshaller.unmarshal(input);
        List<ProbeEvent> events = probeEventsDataSet.getEvents();
        assert events!=null;

        XmlEncoder encoder = new XmlEncoder();
        MqttClient mqttClient = new MqttClient("tcp://127.0.0.1:1883", "injecteurTest");
        mqttClient.connect();
        try { 
            for (Iterator<ProbeEvent> iterator = events.iterator(); iterator.hasNext();) {
                ProbeEvent next = iterator.next();
                System.out.println("Read "+next.getClass().getName()+" = "+next);
                mqttClient.publish("cafetiton/temperature", encoder.encode(next), 2, true);
            }
        } finally {
            mqttClient.disconnect();
        }
    }

    //@Test
    public void testMarshalling() throws Exception {
        ProbeEventsDataSet set = new ProbeEventsDataSet();
        List<ProbeEvent> events = new ArrayList<ProbeEvent>();
        events.add(new ProbeReadEvent("probe01", 12.3f, new Date()));
        events.add(new ProbeReadEvent("probe01", 12.4f, new Date()));
        events.add(new ProbeReadEvent("probe01", 12.8f, new Date()));
        events.add(new ProbeReadEvent("probe01", 13.2f, new Date()));
        set.setEvents(events);
        
        JAXBContext jaxbcontext = JAXBContext.newInstance(
                ProbeEventsDataSet.class,
                ProbeConnectionEvent.class,
                ProbeDisconnectionEvent.class,
                ProbeFailedEvent.class,
                ProbeReadEvent.class);
        Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        jaxbMarshaller.marshal(events, System.out);     
    }
}