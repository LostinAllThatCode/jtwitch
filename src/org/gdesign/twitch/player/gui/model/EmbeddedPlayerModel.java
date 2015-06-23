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

	@Override
	public void streamStarted(LivestreamerInstance livestreamer) {
		if (instance != null) propertyChange.firePropertyChange("streamEnded",null,instance);
		propertyChange.firePropertyChange("streamStarted",null,instance = livestreamer);
	}

	@Override
	public void streamEnded(LivestreamerInstance livestreamer) {
		propertyChange.firePropertyChange("streamEnded",null,instance);
	}
}
