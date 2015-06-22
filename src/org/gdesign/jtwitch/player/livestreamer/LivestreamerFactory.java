package org.gdesign.jtwitch.player.livestreamer;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;
import org.gdesign.utils.Configuration;

public class LivestreamerFactory {
	
	private static Random random;
	private static Properties config;
	
	private static String quality;
	
	private static HashMap<String, LivestreamerInstance> instances;
	
	/** 
	 * 	Its possible to run multiple livestreamer instances at once.
	 */
	
	static {
		random 			= new Random();
		config 			= new Configuration("livestreamer.properties");		
		quality 		= config.getProperty("livestreamer-quality");
		instances 		= new HashMap<>();
	}
	
	public static LivestreamerInstance startInstance(String... args) throws LivestreamerAlreadyRunningException{
		final String stream = args[0];
		final String quality = args[1];
		final int randomPort = generateRandomPort(
				Integer.valueOf(config.getProperty("livestreamer-http-port")), 
				Integer.valueOf(config.getProperty("livestreamer-http-port-range")));
		final String[] cmd = (String[]) ArrayUtils.addAll((runtimeExecutable()+" "+randomPort).split(" "), args);
		
		if (isRunning(stream)) {
			LogManager.getLogger().error("There is an existing instance for "+stream+" with threadID: "+instances.get(stream).getId());
			throw new LivestreamerAlreadyRunningException();
		}
			
		LivestreamerInstance instance = new LivestreamerInstance(cmd);
		instances.put(stream, instance);
		
		instance.setStream(stream);
		instance.setPort(randomPort);
		instance.setQuality(quality);
		instance.start();
		return instance;
	}
	
	private static int generateRandomPort(int start, int range){
		return random.nextInt(range)+start;
	}
	
	private static String runtimeExecutable(){
		String path = config.getProperty("livestreamer");
		String args = config.getProperty("livestreamer-args");
		return path + args;
	}
	
	public static LivestreamerInstance getInstance(String name) {
		return instances.get(name);
	}
	
	public static void removeInstance(LivestreamerInstance instance){
		if (instance != null) {
			instance.stopStream();
			instances.remove(instance.getStream());
		}
	}
	
	protected static boolean isRunning(String stream){
		return instances.containsKey(stream); 
	}
	
	public static String getDefaultQuality(){
		return quality;
	}
	
}
