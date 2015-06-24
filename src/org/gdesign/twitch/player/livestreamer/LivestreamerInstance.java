package org.gdesign.twitch.player.livestreamer;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;

public class LivestreamerInstance extends Thread implements Runnable{
	
	private Process process;
	private String localhost;
	private String quality;
	private int port;
	private String stream;
	private String[] cmd;
	private List<LivestreamerListener> listener;
	
	public LivestreamerInstance(String... cmd) {
		this.listener = new ArrayList<LivestreamerListener>();
		this.localhost 	= "http://127.0.0.1";
		this.cmd 		= cmd;
		this.stream 	= "";
		this.port		= 0;
		this.quality 	= "";
	}
	
	public void stopStream(){
		interrupt();
		process.destroy();
	}
	
	public void addListener(LivestreamerListener l){
		this.listener.add(l);
	}
	
	@Override
	public void run() {
		try {
			LogManager.getLogger().debug(ArrayUtils.toString(cmd));
			
			ProcessBuilder pb = new ProcessBuilder(cmd);
			process = pb.start();
	        InputStreamReader reader = new InputStreamReader(process.getInputStream());
	        int ch;
	        String line = "";
	        while(( (ch = reader.read()) != -1) && !isInterrupted()){
	        	line += (char) ch;
	        	if (line.startsWith("[cli]") && line.endsWith("\n")){
	        		String output = line.substring(5, line.length()-2);
	        		LogManager.getLogger().trace(output);	
	        		if (output.contains(this.localhost+":"+port)) for (LivestreamerListener l : listener) l.streamStarted(this);
	        		if (output.contains("HTTP connection closed") || output.contains("Stream ended")) {
	        			break;
	        		}
	        		line = "";
	        	}
	        }			   
	        reader.close();   
			LogManager.getLogger().debug("Stopped "+stream+" on http://127.0.0.1:"+port);
		} catch (Exception e) {
			LogManager.getLogger().error(">"+e);
		}
		for (LivestreamerListener l : listener) l.streamEnded(this);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}
	
	public String getLocalhost(){
		return localhost;
	}
	
	public void setLocalhost(String localhost){
		this.localhost = localhost;
	}
	
	public String getMRL() {
		return localhost+":"+port;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

}
