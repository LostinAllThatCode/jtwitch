package org.gdesign.jtwitch.player.gui.model;

import java.beans.PropertyChangeListener;

import javax.swing.event.SwingPropertyChangeSupport;

import org.gdesign.twitch.api.prototypes.TStream;
import org.gdesign.twitch.api.util.TwitchAPI;
import org.json.simple.parser.ParseException;

public class ChannelModel implements Comparable<ChannelModel>{
	
	private SwingPropertyChangeSupport propertyChange;
	private boolean online;
	private String name,game,action;
	private int viewers;
	private Thread thread;
	private long sleep;
	
	public ChannelModel(String channelName) {
		this.name = channelName;
		this.game = "";
		this.viewers = 0;
		this.action = "";
		propertyChange = new SwingPropertyChangeSupport(this);
		
		thread = new Thread() {
			public void run(){
				while (!isInterrupted()){
					try {
						TStream stream = new TwitchAPI().getStream(name);
						if (stream.isOnline()){
							setOnline(true);
							setGame(stream.get("game"));
							setViewers(Integer.valueOf(stream.get("viewers")));
							propertyChange.firePropertyChange("sortChannels", false , true );
							sleep = 5000;
						} else {
							setOnline(false);
							sleep = 30000;
						}
						Thread.sleep(sleep);
					} catch (ParseException | InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}

		};
		thread.start();
	}
	
	public boolean isOnline(){
		return this.online;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getGame(){
		return this.game;
	}

	public int getViewers(){
		return this.viewers;
	}
	
	public String getAction(){
		return this.action;
	}
	
	public void setOnline(boolean online){
		boolean oldVal = this.online;
		this.online = online;
		propertyChange.firePropertyChange("stateChanged", oldVal, this.online);
	}
	
	public void setGame(String game){
		String oldVal = this.game;
		this.game = game;
		propertyChange.firePropertyChange("gameChanged", oldVal, this.game);		
	}
	
	public void setViewers(int viewers){
		int oldVal = this.viewers;
		this.viewers = viewers;
		propertyChange.firePropertyChange("viewersChanged", oldVal, this.viewers);
	}
	
	public void setAction(String action){
		String oldVal = this.action;
		this.action = action;
		propertyChange.firePropertyChange("actionChanged", oldVal, this.action);		
	}
	
	public void addModelListener(PropertyChangeListener prop) {
		propertyChange.addPropertyChangeListener(prop);
    }
	
	@Override
	public int compareTo(ChannelModel o) {
		if (this.viewers > o.getViewers()) return -1;
		else if (this.viewers < o.getViewers()) return 1;
		else if (viewers == 0 && o.getViewers() == 0) {
			return name.compareTo(o.getName());
		}
		else if (this.viewers == o.getViewers()) return 0;
		else return 0;
	}
	
	@Override
	public String toString() {
		return "["+name+","+game+","+viewers+","+action+","+online+"]";
	}

}
