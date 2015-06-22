package org.gdesign.jtwitch.player.livestreamer;

import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerConnectionTimeoutException;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerCreateHostException;

public class LivestreamerInstance extends Thread implements Runnable{
	
	private Process process;
	private String localhost;
	private String quality;
	private int port;
	private String stream;
	private String[] cmd;
	private boolean ready;
	private boolean connected = false;
	
	public LivestreamerInstance(String... cmd) {
		this.localhost 	= "http://127.0.0.1";
		this.cmd 		= cmd;
		this.ready 		= false;
		this.connected 	= false;
		this.stream 	= "";
		this.port		= 0;
		this.quality 	= "";
	}
	
	public void stopStream(){
		interrupt();
		process.destroy();
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
	        while((ch = reader.read()) != -1 && !isInterrupted()){
	        	line += (char) ch;
	        	if (line.startsWith("[cli]") && line.endsWith("\n")){
	        		String output = line.substring(5, line.length()-2);
	        		LogManager.getLogger().trace(output);
	        		
	        		if (output.contains(this.localhost+":"+port)) ready = true;
	        		if (output.contains("Got HTTP request from")) connected = true;
	        		if (output.contains("HTTP connection closed") || output.contains("Stream ended")) {
	        			throw new LivestreamerCreateHostException();
	        		}
	        		line = "";
	        	}
	        }			   
	        reader.close();   
			LogManager.getLogger().debug("Stopped "+stream+" on http://127.0.0.1:"+port);			
		} catch (Exception e) {
			LogManager.getLogger().error(e);
		}
	}
	
	public boolean isConnected(){
		return connected;
	}
	
	public boolean waitForClientConnection() throws InterruptedException, LivestreamerConnectionTimeoutException{
		long maxtime 	= 5000;
		long time 		= 0;
		while (!connected) {
			if (time >= maxtime) throw new LivestreamerConnectionTimeoutException();
			Thread.sleep(250);
			time+=250;
		}
		return true;
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
	
	public String getMRL() throws InterruptedException{
		while (!ready) Thread.sleep(250);
		return localhost+":"+port;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	

}
