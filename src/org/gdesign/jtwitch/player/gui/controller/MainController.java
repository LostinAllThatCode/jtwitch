package org.gdesign.jtwitch.player.gui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.gui.listener.ChannelMouseListener;
import org.gdesign.jtwitch.player.gui.model.ChannelListModel;
import org.gdesign.jtwitch.player.gui.model.ChannelModel;
import org.gdesign.jtwitch.player.gui.model.EmbeddedPlayerModel;
import org.gdesign.jtwitch.player.gui.model.MainModel;
import org.gdesign.jtwitch.player.gui.view.ChannelView;
import org.gdesign.jtwitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.jtwitch.player.gui.view.MainView;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.twitch.api.prototypes.TChannel;
import org.gdesign.twitch.api.prototypes.TStream;
import org.gdesign.twitch.api.util.TwitchAPI;
import org.json.simple.parser.ParseException;

public class MainController implements PropertyChangeListener{
	
	private MainView view;
	private MainModel model;
	private TwitchAPI twitch;
	private ChannelMouseListener listener;
	
	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;
		this.twitch = new TwitchAPI();
		
		listener = new ChannelMouseListener(this);
		
		this.view.addMouseListener(listener);
		this.view.addMouseWheelListener(listener);
		
		this.model.addModelListener(this);
		this.view.getChannelListView().setEnabled(false);
	}

	public void update(final long interval) throws ParseException{
		new Thread(new Runnable() {			
			@Override
			public void run() {
				while (true) {
					try {
						if (model.getChannelListModel().getChannelCount() == 0) {
								for (TChannel channel : twitch.getFollows(model.getChannelListModel().getUsername()).getChannels()){
									String channelName = channel.getString("display_name");
									if (channelName.length() != 0){
										ChannelModel m = new ChannelModel(channelName);
										m.addModelListener(MainController.this);
										model.getChannelListModel().addChannel(m);
														
										TStream stream = twitch.getStream(m.getName());
										if (m.getGame().compareTo(stream.getString("game")) != 0) m.setGame(stream.getString("game"));
										if (m.getViewers() != Integer.valueOf(stream.getInt("viewers"))) m.setViewers(Integer.valueOf(stream.getInt("viewers")));
										if (m.isOnline() != stream.isOnline()) m.setOnline(stream.isOnline());
										m.fireUpdate();
									}
								}
								view.getChannelListView().setEnabled(true);
						} else {
							for (ChannelModel m : model.getChannelListModel().getChannels()){
								TStream stream = twitch.getStream(m.getName());
								if (m.getGame().compareTo(stream.getString("game")) != 0) m.setGame(stream.getString("game"));
								if (m.getViewers() != Integer.valueOf(stream.getInt("viewers"))) m.setViewers(Integer.valueOf(stream.getInt("viewers")));
								if (m.isOnline() != stream.isOnline()) m.setOnline(stream.isOnline());
								m.fireUpdate();
							}
						}
						Thread.sleep(interval);
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
				ChannelView cv = new ChannelView();
				if (cm != null && cv != null) {
					cv.setChannelName(cm.getName());
					cv.setChannelGame(cm.getGame());
					cv.setChannelViewers(String.valueOf(cm.getViewers()));
					cv.setChannelAction(cm.getAction());
					cv.setOnline(cm.isOnline());
					cv.addMouseListener(listener);
					view.getChannelListView().addChannel(cv);
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
				EmbeddedPlayerModel pm = model.getEmbeddedPlayerModel();
				EmbeddedPlayerView pv = view.getPlayerView();
				if (pv != null && pm != null){
					if (evt.getPropertyName().equals("streamStarted")) {
						LivestreamerInstance oldInstance = (LivestreamerInstance) evt.getOldValue();
						LivestreamerInstance newInstance = (LivestreamerInstance) evt.getNewValue();
						LivestreamerFactory.removeInstance(oldInstance);
						pv.setDescription(newInstance.getStream());
						pv.setQuality(newInstance.getQuality());
						
						if (oldInstance != null) {
							model.getChannelListModel().getChannel(oldInstance.getStream().substring(10, oldInstance.getStream().length())).setAction("");
							model.getChannelListModel().getChannel(oldInstance.getStream().substring(10, oldInstance.getStream().length())).fireUpdate();
						}
						
						model.getChannelListModel().getChannel(newInstance.getStream().substring(10, newInstance.getStream().length())).setAction("PLAYING");
						model.getChannelListModel().getChannel(newInstance.getStream().substring(10, newInstance.getStream().length())).fireUpdate();
						
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
