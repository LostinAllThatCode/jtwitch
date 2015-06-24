package org.gdesign.twitch.player.gui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.json.type.TChannel;
import org.gdesign.twitch.api.json.type.TStream;
import org.gdesign.twitch.api.json.utils.HttpRequest;
import org.gdesign.twitch.player.gui.listener.ChannelMouseListener;
import org.gdesign.twitch.player.gui.model.ChannelListModel;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.model.EmbeddedPlayerModel;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.json.simple.parser.ParseException;

public class MainController implements PropertyChangeListener{
	
	private MainView view;
	private MainModel model;
	private HttpRequest twitch;
	private ChannelMouseListener listener;
	
	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;
		this.twitch = new HttpRequest();
		
		listener = new ChannelMouseListener(this);
		
		this.view.addMouseListener(listener);
		this.view.addMouseWheelListener(listener);
		
		this.model.addModelListener(this);
		this.view.getChannelListView().setEnabled(false);
	}
	
	private void updateChannelModel(ChannelModel m) throws ParseException {
		if (m != null){
			TStream stream = twitch.getStream(m.getName());
			if (m.getGame().compareTo(stream.getString("game")) != 0) m.setGame(stream.getString("game"));
			if (m.getViewers() != Integer.valueOf(stream.getInt("viewers"))) m.setViewers(Integer.valueOf(stream.getInt("viewers")));
			if (m.isOnline() != stream.isOnline()) m.setOnline(stream.isOnline());
			m.fireUpdate();
			view.getChannelListView().getChannel(m.getName()).setEnabled(true);
		}
	}

	public void update(final long interval) throws ParseException{
		new Thread(new Runnable() {			
			long time = 0;
			
			@Override
			public void run() {
				while (true) {
					try {
						if (model.getChannelListModel().getChannelCount() == 0) {
								for (TChannel channel : twitch.getFollows(model.getChannelListModel().getUsername()).getChannels())
									updateChannelModel(model.getChannelListModel().createChannel(channel.getString("name"),channel.getString("display_name")));
						} else {
							for (ChannelModel m : model.getChannelListModel().getSortedChannels()) if (m.isOnline()) updateChannelModel(m);
							if (time / interval >= 4) {
								List<ChannelModel> copy = new ArrayList<>(model.getChannelListModel().getSortedChannels());
								for (TChannel channel : twitch.getFollows(model.getChannelListModel().getUsername()).getChannels()){
									ChannelModel channelModel = model.getChannelListModel().getChannel(channel.getString("name"));
									if (channelModel == null){
										updateChannelModel(model.getChannelListModel().createChannel(channel.getString("name"),channel.getString("display_name")));					
										view.getChannelListView().setEnabled(true);
									} else {
										updateChannelModel(channelModel);
										copy.remove(channelModel);
									}
								}
								for (ChannelModel rem : copy) model.getChannelListModel().removeChannel(rem);
								time = 0;
							}
						}
						Thread.sleep(interval);
						time+=interval;
					} catch (ParseException | InterruptedException e) {
						LogManager.getLogger().error(e.getMessage());
					}
				}
			}
		}).start();
	}

	
	@Override
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
					cv.addMouseListener(listener);
					view.getChannelListView().addChannel(cv);
					view.getChannelListView().sortChannels(model.getChannelListModel().getSortedChannels());
				}
			} else if (evt.getPropertyName().compareTo("removeChannel") == 0){
				ChannelModel cm = (ChannelModel) evt.getNewValue();
				ChannelView cv = view.getChannelListView().getChannel(cm.getName());
				if (cm != null && cv != null) {
					view.getChannelListView().remove(cv);
					view.getChannelListView().sortChannels(model.getChannelListModel().getSortedChannels());
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
					view.getChannelListView().sortChannels(model.getChannelListModel().getSortedChannels());
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
						pv.setDescription(instance.getStream());
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).setAction("PLAYING");
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).fireUpdate();
					} else if (evt.getPropertyName().equals("streamEnded")){
						LivestreamerInstance instance = (LivestreamerInstance) evt.getNewValue();
						//pv.stopMedia();
						//pv.setDescription("");				
						instance.stopStream();
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).setAction("");
						cm.getChannel(instance.getStream().substring(10, instance.getStream().length())).fireUpdate();
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
