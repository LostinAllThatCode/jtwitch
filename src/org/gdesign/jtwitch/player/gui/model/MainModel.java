package org.gdesign.jtwitch.player.gui.model;

import java.beans.PropertyChangeListener;

import javax.swing.event.SwingPropertyChangeSupport;

public class MainModel {
	
	private SwingPropertyChangeSupport propertyChange;
	private ChannelListModel channelListModel;
	private EmbeddedPlayerModel embeddedPlayerModel;
	
	public MainModel(String username) {
		channelListModel = new ChannelListModel(username);
		embeddedPlayerModel = new EmbeddedPlayerModel();
		propertyChange = new SwingPropertyChangeSupport(this);
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
		channelListModel.addModelListener(prop);
		embeddedPlayerModel.addModelListener(prop);
    }
	
	public EmbeddedPlayerModel getEmbeddedPlayerModel(){
		return embeddedPlayerModel;
	}
	
	public ChannelListModel getChannelListModel(){
		return channelListModel;
	}
}
