/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.job.probevent.client.twitter;

import com.job.probeevent.ProbeConnectionEvent;
import com.job.probeevent.ProbeDisconnectionEvent;
import com.job.probeevent.ProbeEvent;
import com.job.probeevent.ProbeEventListener;
import com.job.probeevent.ProbeEventXmlEncoder;
import com.job.probeevent.ProbeFailedEvent;
import com.job.probeevent.ProbeReadEvent;
import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author joel
 */
public class TwitterGraphLogger implements ProbeEventListener {

    private static final String HISTORY_FILENAME = "probeventhistory.xml";
    private static final Logger logger = Logger.getLogger(TwitterGraphLogger.class);
    
    ProbeEventXmlEncoder encoder = new ProbeEventXmlEncoder();
    ProbeEventHistory history = new ProbeEventHistory();
    
    public TwitterGraphLogger() {
        Timer timer = new Timer("GraphSender");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendChart();
                } catch (Exception ex) {
                    logger.error("Unable to send chart", ex);
                } finally {
                    history.clear();
                }
            }
        }, 1000, 24*60*60*1000);        
    }

       @Override
    public void probeConnected(ProbeConnectionEvent e) {
        logger.warn("Received an unsupported event "+e);
    }

    @Override
    public void probeDisonnected(ProbeDisconnectionEvent e) {
        logger.warn("Received an unsupported event "+e);
    }

    @Override
    public void probeReadingFailed(ProbeFailedEvent e) {
        logger.warn("Received an unsupported event "+e);
    }
    
    @Override
    public void probeRead(ProbeReadEvent e) {
        history.addProbeEvent(e);
    }

 
    public List<ProbeEvent> readProbeEvents() throws JAXBException, IOException, SecurityException {
        return history.getProbeEvents();
    }

    public void sendChart() throws Exception {
        // crée le graphique
        byte[] jpgImage = getJpg();
        
        // envoie le graphique sur twitter
        Twitter twitter = TwitterFactory.getSingleton();
        String message="releve de temperature";
        StatusUpdate update = new StatusUpdate(message);
        update.setMedia("Chart", new ByteArrayInputStream(jpgImage));
        Status status = twitter.updateStatus(update);
    }
    
    public byte[] getJpg() throws Exception {
        // Create Chart
        Chart chart = new ChartBuilder().width(800).height(600).title("Relevé de temperature").build();
        chart.getStyleManager().setLegendVisible(false);

        Map<String, Serie> series = new HashMap<>();

        List<ProbeEvent> events = readProbeEvents();
        for (Iterator<ProbeEvent> it = events.iterator(); it.hasNext();) {
            ProbeReadEvent event = (ProbeReadEvent)it.next();
            Serie serie = series.get(event.getProbeId());
            if(serie==null) {
                serie = new Serie(event.getProbeId());
                series.put(event.getProbeId(), serie);
            }
            serie.add(event);
        }

        series.keySet().stream().forEach((probeId) -> {
            Serie serie = series.get(probeId);
            chart.addSeries(probeId, serie.getxData(), serie.getyData());
        });
        
        byte jpgImage[] = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.JPG);
        return jpgImage;
    }
    
}

class Serie {
    private final String name;
    private final List<Date> xData = new ArrayList<>();
    private final List<Double> yData = new ArrayList<>();
    Serie(String name) {
        this.name = name;
    }

    List<Date> getxData() {
        return xData;
    }

    List<Double> getyData() {
        return yData;
    }

    void add(ProbeReadEvent event) {
        xData.add(event.getTime());
        yData.add((double)event.getTemperature());
    }
}
    
