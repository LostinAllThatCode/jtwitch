package org.gdesign.jtwitch.player.livestreamer;

import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;

public class Livestreamer extends Thread implements Runnable{
	
	private Process process;
	private String localhost;
	private int port;
	private String stream;
	private String[] cmd;
	
	public Livestreamer(String... cmd) {
		this.localhost = "http://127.0.0.1";
		this.cmd = cmd;
	}
	
	public void stopStream(){
		interrupt();
		process.destroy();
	}
	
	@Override
	public void run() {
		try {
			LogManager.getLogger().trace(ArrayUtils.toString(cmd));
			
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
	        		if (output.contains("HTTP connection closed") || output.contains("Stream ended") || output.contains("error") ) break;
	        		line = "";
	        	}
	        }			   
	        reader.close();
	        
			LogManager.getLogger().debug("Stopped "+stream+" on http://127.0.0.1:"+port);			
		} catch (Exception e) {
			LogManager.getLogger().error(e);
			e.printStackTrace();
		}
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
	
	public String getMRL(){
		return localhost+":"+port;
	}
	
	

}
