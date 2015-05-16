package com.job.temperatureapp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author joel
 */
public class TemperatureTwitter implements TemperatureListener {

    @Override
    public void temperatureChangee(EnceinteFrigorifique ef, ReleveTemperature rt) throws Exception {
		Twitter twitter = TwitterFactory.getSingleton();
		String message=ef.getNom()+" : "+rt;
		Status status = twitter.updateStatus(message);    }
    
}
