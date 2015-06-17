package org.gdesign.jtwitch.player.livestreamer;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.gdesign.utils.Configuration;

public class Livestreamer extends Observable{
	
	public static enum Quality { AUDIO, LOW , MEDIUM, HIGH, SOURCE, MOBILE }; 
	private static Properties config;
	private static Random random;
	private Process proc = null;
	
	
	static {
		LogManager.getLogger().info("Initializing livestreamer library.");
		random = new Random();
		config = new Configuration("livestreamer.properties");
		LogManager.getLogger().info("Livestreamer natives: "+config.getProperty("livestreamer"));
		LogManager.getLogger().info("VLC natives: ");
	}
	
	public void openStream(final String channel){
		openStream(channel,
				Quality.valueOf(config.getProperty("livestreamer-quality").toUpperCase()));
	}
	
	public void openStream(final String channel, final Quality quality) {
		try {
			if (proc != null) {
				LogManager.getLogger().warn("Running instance of livestreamer found. Stopping playback...");
				setChanged();
				notifyObservers(channel+"§stop");
				Thread.sleep(5000);
			}
			startHTTP(channel, quality.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private void startHTTP(final String channel, final String quality){
		final String[] commands = { config.getProperty("livestreamer")+"\\livestreamer.exe","--hds-segment-threads"
				,"5","--player-no-close","--player-external-http","--player-external-http-port", generateRandomPort()
				,"twitch.tv/"+channel, quality};
		
		LogManager.getLogger().info("Running livestreamer http service for "+commands[7]+" on " + config.getProperty("livestreamer-http") +":" + commands[6]);
		new Thread(new Runnable() {		
			@Override
			public void run() {
				try {
					ProcessBuilder pb = new ProcessBuilder(commands);
					proc = pb.start();
					
			        Reader reader = new InputStreamReader(proc.getInputStream());
			        int ch;
			        String line = "";
			        while((ch = reader.read())!= -1){
			        	line += (char) ch;
			        	if (ch == '\n') {
			        		LogManager.getLogger("livestreamer.exe").info(line);
			        		if (line.contains("http://"+config.getProperty("livestreamer-http")+":"+commands[6])) {
			        			setChanged();
			        			notifyObservers(channel+"§play§http://"+config.getProperty("livestreamer-http")+":"+commands[6]);
			        		} else if (line.contains("HTTP connection closed") || line.contains("Stream ended") || line.contains("error") ) {
			        			break;
			        		}
			        		line = "";
			        	}
			        }			   
			        reader.close();
			        proc.destroy();
			        proc.waitFor();
				} catch (Exception e) {
        			e.printStackTrace();
				}
    			setChanged();
    			notifyObservers(channel+"§stopped");
				LogManager.getLogger().info("Stopped.");
			}}).start();

	}
	
	private String generateRandomPort(){
		int a = Integer.valueOf(config.getProperty("livestreamer-http-port")), b = Integer.valueOf(config.getProperty("livestreamer-http-port-range"));
		return String.valueOf(random.nextInt(b)+a);
	}
}
