package org.gdesign.twitch.player.gui.controller;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.follows.Follows;
import org.gdesign.twitch.api.resource.streams.StreamInfo;
import org.gdesign.twitch.api.resource.users.UserFollowedChannels;
import org.gdesign.twitch.player.gui.listener.PlayerMouseListener;
import org.gdesign.twitch.player.gui.listener.PlayerHotkeyListener;
import org.gdesign.twitch.player.gui.model.ChannelListModel;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.model.EmbeddedPlayerModel;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.utils.Configuration;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;


public class MainController implements PropertyChangeListener{
	
	private MainView view;
	private MainModel model;
	private PlayerMouseListener mouseListener;
	private PlayerHotkeyListener hotkeyListener;
	private Properties config;
	
	public MainController(MainView view, MainModel model) {	
		this.config = new Configuration("jtwitch.properties");
		
		this.view = view;
		this.model = model;

		this.mouseListener = new PlayerMouseListener(this);
		
		this.hotkeyListener = new PlayerHotkeyListener(this);
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.ALT_DOWN_MASK));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ADD,0));
		
		this.view.getEmbeddedPlayerView().setVolume(Integer.valueOf(config.getProperty("volume")));
		this.view.addMouseListener(mouseListener);
		this.view.addMouseWheelListener(mouseListener);
		
		this.model.addModelListener(this);
		this.view.getChannelListView().setEnabled(false);
	}
	
	private void updateChannelModel(ChannelModel m) throws ParseException {
		if (m != null){
			StreamInfo streamInfo = new Gson().fromJson(new TwitchAPI().request("https://api.twitch.tv/kraken/streams/"+m.getName(), null),StreamInfo.class);
			if (streamInfo.stream != null){
				if (m.getGame().compareTo(streamInfo.stream.game) != 0) m.setGame(streamInfo.stream.game);
				if (m.getViewers() != streamInfo.stream.viewers) m.setViewers(streamInfo.stream.viewers);
				if (!m.isOnline()) m.setOnline(true);
			} else {
				m.setGame("");
				m.setViewers(0);
				m.setOnline(false);
			}
			m.fireUpdate();
		}
	}

	public void update(final long interval) throws ParseException{
		new Thread(new Runnable() {			
			long time = interval * 4;
			public void run() {
				while (true) {
					try {
						if (time / interval >= 4) {
							List<ChannelModel> copy = new ArrayList<ChannelModel>(model.getChannelListModel().getChannels());
							UserFollowedChannels channels =  new Gson().fromJson(new TwitchAPI().request("https://api.twitch.tv/kraken/users/"+config.getProperty("username")+"/follows/channels", null),UserFollowedChannels.class);
							for (Follows f : channels.follows){
								ChannelModel channelModel = model.getChannelListModel().getChannel(f.channel.name);	
								if (channelModel == null){
									updateChannelModel(model.getChannelListModel().createChannel(f.channel.name, f.channel.display_name));				
								} else {
									updateChannelModel(channelModel);
									copy.remove(channelModel);
								}
							}
							for (ChannelModel rem : copy) model.getChannelListModel().removeChannel(rem);
							view.getChannelListView().setEnabled(true);
							view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
							time = 0;
						} else {
							for (ChannelModel m : model.getChannelListModel().getChannels()) if (m.isOnline()) updateChannelModel(m);
						}
						view.revalidate();
						Thread.sleep(interval);
						time+=interval;
						
					} catch (Exception e) {
						e.printStackTrace();
						LogManager.getLogger().error(e);
					}
				}
			}
		}).start();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		LogManager.getLogger().trace(evt);
		if (evt.getSource().getClass().equals(ChannelListModel.class)){
			if (evt.getPropertyName().compareTo("addChannel") == 0){
				ChannelModel cm = (ChannelModel) evt.getNewValue();
				if (cm != null) {
					ChannelView cv = new ChannelView(cm.getName());
					cv.setChannelName(cm.getDisplayname());
					cv.setChannelGame(cm.getGame());
					cv.setChannelViewers(String.valueOf(cm.getViewers()));
					cv.setChannelAction(cm.getAction());
					cv.setOnline(cm.isOnline());
					cv.addMouseListener(mouseListener);
					view.getChannelListView().addChannel(cv);
					view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
				}
			} else if (evt.getPropertyName().compareTo("removeChannel") == 0){
				ChannelModel cm = (ChannelModel) evt.getNewValue();
				ChannelView cv = view.getChannelListView().getChannel(cm.getName());
				if (cm != null && cv != null) {
					view.getChannelListView().remove(cv);
					view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
				}
			}
		} else if (evt.getSource().getClass().equals(ChannelModel.class)){
			if (evt.getNewValue() != null && evt.getPropertyName().compareTo("updatedChannel") == 0){
				ChannelModel cm = (ChannelModel) evt.getSource();
				ChannelView cv 	= view.getChannelListView().getChannel(cm.getName());
				if (cm != null && cv != null) {
					cv.setChannelGame(cm.getGame());
					cv.setChannelViewers(String.valueOf(cm.getViewers()));
					cv.setChannelAction(cm.getAction());
					cv.setOnline(cm.isOnline());
					view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
				}
			}
		} else if (evt.getSource().getClass().equals(EmbeddedPlayerModel.class)){
			if (evt.getNewValue() != null){
				ChannelListModel cm		= model.getChannelListModel();
				EmbeddedPlayerModel pm 	= model.getEmbeddedPlayerModel();
				EmbeddedPlayerView pv 	= view.getEmbeddedPlayerView();
				if (pv != null && pm != null){
					if (evt.getPropertyName().equals("streamStarted")) {
						LivestreamerInstance instance = (LivestreamerInstance) evt.getNewValue();
						pv.playMedia(instance.getMRL(), "");
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).setAction("PLAYING");
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).fireUpdate();
						pv.setControlValue("STATUS", "["+instance.getMRL()+"] ["+instance.getQuality()+"] Fetching data from "+instance.getStream()+"...");
					} else if (evt.getPropertyName().equals("streamEnded")){
						LivestreamerInstance instance = (LivestreamerInstance) evt.getNewValue();
						pv.stopMedia();
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).setAction("");
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).fireUpdate();
						pv.setControlValue("STATUS", "["+instance.getMRL()+"] Stream closed.");
					}
				}
			}
		} 
		view.repaint();
	}
		
	public MainView getView(){
		return view;
	}
	
	public MainModel getModel(){
		return model;
	}

}
