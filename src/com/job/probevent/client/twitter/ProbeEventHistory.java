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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.enumeration;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/**
 *
 * @author joel
 */
public class ProbeEventHistory {
    private static final String HISTORY_FILENAME = "probeventhistory.xml";
    private static final Logger logger = Logger.getLogger(ProbeEventHistory.class);
    
    ProbeEventXmlEncoder encoder = new ProbeEventXmlEncoder();

    public void addProbeEvent(ProbeReadEvent e) {
        File history = new File(HISTORY_FILENAME);        
        try {
            FileOutputStream os = new FileOutputStream(history, true);
            try {
                ProbeEventXmlEncoder.encode(e, os);
            } catch(JAXBException je) {
                logger.error("Unable to decode history file", je);
            } catch(Exception se) {
                logger.fatal("Unable to create or write to history file", se);
            } finally {
                try {
                    os.close();
                } catch(Throwable t) {
                }
            }
        } catch(FileNotFoundException se) {
            logger.fatal("Unable to create or write to history file", se);
        }
    }
 
    public List<ProbeEvent> getProbeEvents() throws JAXBException, IOException, SecurityException {
        List<ProbeEvent> events = null;
        File history = new File(HISTORY_FILENAME);        
        try {
            FileInputStream is = new FileInputStream(history);
            try {
                events = new ArrayList<>();
                while(is.available()!=0) {
                    ProbeEvent e = ProbeEventXmlEncoder.decode(is);
                    events.add(e);
                }
            } catch(JAXBException je) {
                logger.warn("Unable to decode history file", je);
                throw je;
            } catch(SecurityException se) {
                logger.fatal("Unable to create or write to history file", se);
                throw se;
            } finally {
                try {
                    is.close();
                } catch(Throwable t) {
                }
            }
        } catch(FileNotFoundException fnfe) {
            logger.fatal("Unable to create or write to history file", fnfe);
            throw fnfe;
        }
        return events;
    }

    public void clear() {
        File history = new File(HISTORY_FILENAME);        
        boolean delete = history.delete();
    }
}
    
