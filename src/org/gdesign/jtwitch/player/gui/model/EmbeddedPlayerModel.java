package org.gdesign.jtwitch.player.gui.model;

//import javax.swing.event.SwingPropertyChangeSupport;

import org.gdesign.jtwitch.player.livestreamer.Livestreamer;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;


public class EmbeddedPlayerModel {

	private Livestreamer instance;	

	public EmbeddedPlayerModel() {
		//propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public String startInstance(String... args){
		try {			
			if (instance == null){
				instance = LivestreamerFactory.startInstance(args);
				if (instance != null) {
					return instance.getLocalhost()+":"+instance.getPort();
				}
			} else {
				instance.stopStream();
				instance = LivestreamerFactory.startInstance(args);
				if (instance != null) {
					return instance.getLocalhost()+":"+instance.getPort();
				}
			}
		} catch (LivestreamerAlreadyRunningException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void stop(){
		if (instance != null) instance.stopStream();
	}
	
}
