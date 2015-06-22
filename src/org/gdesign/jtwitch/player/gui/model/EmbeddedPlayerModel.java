package org.gdesign.jtwitch.player.gui.model;

import javax.swing.event.SwingPropertyChangeSupport;

import java.beans.PropertyChangeListener;

import org.gdesign.jtwitch.player.livestreamer.LivestreamerInstance;

public class EmbeddedPlayerModel {

	private SwingPropertyChangeSupport propertyChange;
	private LivestreamerInstance instance;	

	public EmbeddedPlayerModel() {
		propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public void set(LivestreamerInstance instance){
		LivestreamerInstance oldInstance = this.instance;
		this.instance = instance;
		propertyChange.firePropertyChange("streamStarted",oldInstance,instance);
	}
	
	public LivestreamerInstance getInstance(){
		return instance;
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
    }
	
	public boolean isPlaying() {
		return instance.isConnected();
	}
}
