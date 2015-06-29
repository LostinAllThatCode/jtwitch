package org.gdesign.twitch.player.gui.model;

import javax.swing.event.SwingPropertyChangeSupport;

import java.beans.PropertyChangeListener;

import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.twitch.player.livestreamer.LivestreamerListener;

public class EmbeddedPlayerModel implements LivestreamerListener{

	private SwingPropertyChangeSupport propertyChange;
	private LivestreamerInstance instance;	

	public EmbeddedPlayerModel() {
		propertyChange = new SwingPropertyChangeSupport(this);
	}
		
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
    }
	
	public LivestreamerInstance getInstance(){
		return instance;
	}

	public void streamStarted(LivestreamerInstance livestreamer) {
		propertyChange.firePropertyChange("streamStarted",null,instance = livestreamer);
	}
	
	public void streamEnded(LivestreamerInstance livestreamer) {
		propertyChange.firePropertyChange("streamEnded",null,livestreamer);
	}
}
