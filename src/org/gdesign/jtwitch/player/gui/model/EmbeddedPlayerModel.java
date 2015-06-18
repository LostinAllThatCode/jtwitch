package org.gdesign.jtwitch.player.gui.model;

import javax.swing.event.SwingPropertyChangeSupport;

import java.beans.PropertyChangeListener;

import org.gdesign.jtwitch.player.livestreamer.Livestreamer;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;


public class EmbeddedPlayerModel {

	private SwingPropertyChangeSupport propertyChange;
	private Livestreamer instance;	

	public EmbeddedPlayerModel() {
		propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public String startInstance(String... args){
		try {			
			instance = LivestreamerFactory.startInstance(args);
			if (instance != null) {
				propertyChange.firePropertyChange("streamStarted", "null", instance.getStream());
				return instance.getMRL();
			}
		} catch (LivestreamerAlreadyRunningException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Livestreamer getInstance(){
		return instance;
	}	
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
    }
}
