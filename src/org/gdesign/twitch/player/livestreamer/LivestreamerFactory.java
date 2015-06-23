package org.gdesign.twitch.player.livestreamer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;
import org.gdesign.twitch.player.livestreamer.exception.LivestreamerMaxInstancesException;
import org.gdesign.utils.Configuration;
import org.gdesign.utils.SystemInfo;
import org.gdesign.utils.SystemInfo.OperatingSystem;

public class LivestreamerFactory {
	
	protected static String LIVESTREAMER_PATH;
	
	private static Random 		random;
	private static Properties 	config;
	private static HashMap<String, LivestreamerInstance> instances;
		
	static {
		random 			= new Random();
		config 			= new Configuration("livestreamer.properties");		
		instances 		= new HashMap<>();
	}
	
	public static LivestreamerInstance startInstance(String... args) throws LivestreamerAlreadyRunningException, LivestreamerMaxInstancesException{
		final String stream = args[0];
		final String quality = args[1];
		final int randomPort = generateRandomPort(
				Integer.valueOf(config.getProperty("livestreamer-http-port")), 
				Integer.valueOf(config.getProperty("livestreamer-http-port-range")));
		
		final String[] cmd = (String[]) ArrayUtils.addAll(runtimeExecutable(randomPort), args);
		
		if (instances.size() >= Integer.valueOf(config.getProperty("livestreamer-http-max-instances"))+1) {
			throw new LivestreamerMaxInstancesException();
		}
		
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
	
	private static String[] runtimeExecutable(int port){
		String[] path = { LIVESTREAMER_PATH+"livestreamer" };
		String[] args = (config.getProperty("livestreamer-args")+" "+String.valueOf(port)).split(" ");
		return (String[]) ArrayUtils.addAll(path,args);
	}
	
	private static LivestreamerInstance getInstance(String name) {
		return instances.get(name);
	}
	
	public static void removeInstance(String instanceName){
		LivestreamerInstance instance = getInstance(instanceName);
		if (instance != null) {
			instance.stopStream();
			instances.remove(instanceName);
		}
	}
	
	protected static boolean isRunning(String stream){
		return instances.containsKey(stream); 
	}
	
	public static String getDefaultQuality(){
		return config.getProperty("livestreamer-quality");
	}
	
	private static boolean checkLivestreamer(String cmd) throws IOException, InterruptedException{
		ProcessBuilder pb = new ProcessBuilder(cmd+"livestreamer","--version-check");
		Process process = pb.start();
		if (process.waitFor() == 0) return true; else return false;
	}
	
	public static boolean discover(){
		boolean found = false;
		try {
			if (checkLivestreamer("")) {
				LogManager.getLogger().debug("Livestreamer is nativly installed.");
				LIVESTREAMER_PATH = "";
				found = true;
			}
		} catch (IOException | InterruptedException e) {
			String configPath = config.getProperty("livestreamer");
			try {
				if (checkLivestreamer(configPath)) {
					LogManager.getLogger().debug("Livestreamer location: "+configPath);
					LIVESTREAMER_PATH = configPath;
					found = true;
				}
			} catch (IOException | InterruptedException e1) {
				if (SystemInfo.getOS().equals(OperatingSystem.WIN)){
					File file = new File("C:/Program Files (x86)/livestreamer/livestreamer.exe");
					if (file.exists()) {
						LogManager.getLogger().debug("[WIN] Livestreamer location: C:/Program Files (x86)/livestreamer/");
						LIVESTREAMER_PATH = "C:/Program Files (x86)/livestreamer/";
						found = true;
					}
				} else if (SystemInfo.getOS().equals(OperatingSystem.UNIX)){
					File file = new File("/usr/local/bin/livestreamer");
					if (file.exists()) {
						LogManager.getLogger().debug("[UNIX] Livestreamer location: /usr/local/bin/");
						LIVESTREAMER_PATH = "/usr/local/bin/";
						found = true;
					}
				}
			}
		}
		if (!found) {
			LogManager.getLogger().debug("Livestreamer is not installed or can't be found on this system.");
			LogManager.getLogger().debug("Please set 'livestreamer=' variable in 'livestreamer.properties' to your livestreamer installation path.");
		}
		return found;
	}
	
}