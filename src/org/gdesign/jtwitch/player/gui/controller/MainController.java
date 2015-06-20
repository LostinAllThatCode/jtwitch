package org.gdesign.jtwitch.player.gui.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.gui.model.ChannelListModel;
import org.gdesign.jtwitch.player.gui.model.ChannelModel;
import org.gdesign.jtwitch.player.gui.model.EmbeddedPlayerModel;
import org.gdesign.jtwitch.player.gui.model.MainModel;
import org.gdesign.jtwitch.player.gui.view.ChannelView;
import org.gdesign.jtwitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.jtwitch.player.gui.view.MainView;
import org.gdesign.jtwitch.player.livestreamer.Livestreamer;
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
		
		listener = new ChannelMouseListener();
		
		this.view.addMouseListener(listener);
		this.view.addMouseWheelListener(listener);
		
		this.model.addModelListener(this);
	}

	public void updateGUI(long interval) throws ParseException{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						if (model.getChannelListModel().getChannelCount() == 0) {
								for (TChannel channel : twitch.getFollows("its1z0").getChannels()){
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
									view.getChannelListView().sortChannels(model.getChannelListModel().getSortedChannels());
								}
						} else {
							for (ChannelModel m : model.getChannelListModel().getChannels()){
								TStream stream = twitch.getStream(m.getName());
								if (m.getGame().compareTo(stream.getString("game")) != 0) m.setGame(stream.getString("game"));
								if (m.getViewers() != Integer.valueOf(stream.getInt("viewers"))) m.setViewers(Integer.valueOf(stream.getInt("viewers")));
								if (m.isOnline() != stream.isOnline()) m.setOnline(stream.isOnline());
								m.fireUpdate();
							}
							view.getChannelListView().sortChannels(model.getChannelListModel().getSortedChannels());
							
						}
					} catch (ParseException e) {
						LogManager.getLogger().error(e.getMessage());
					}
					view.repaint();
				}
			}
		}).start();

	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
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
					LogManager.getLogger().trace("Added new channel to view. "+cm);
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
					LogManager.getLogger().trace("Updated channel ."+cm);
				}
			}
		} else if (evt.getSource().getClass().equals(EmbeddedPlayerModel.class)){
			if (evt.getNewValue() != null){
				EmbeddedPlayerModel pm = model.getEmbeddedPlayerModel();
				EmbeddedPlayerView pv = view.getPlayerView();
				if (pv != null && pm != null){
					if (evt.getPropertyName().equals("streamStarted")) {
						pv.setDescription((String) evt.getNewValue());
						LogManager.getLogger().trace("Stream started on embebbed player. "+evt.getNewValue());
					}
					
				}
			}
		}
	}
		
	class ChannelMouseListener extends MouseAdapter {
		
		Color colorLive = new Color(80,40,180);
		Color colorOff	= Color.DARK_GRAY;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannelListModel().getChannel(e.getComponent().getName());
				Livestreamer streamer = model.getEmbeddedPlayerModel().getInstance();
				if (channel.isOnline()){
					if (streamer == null){
						e.getComponent().setEnabled(false);
						String mrl = model.getEmbeddedPlayerModel().startInstance("twitch.tv/"+channel.getName(),LivestreamerFactory.getDefaultQuality());
						String mediaOptions = "--network-cache=5000 --volume=0";
						if (mrl != null) {
							view.getPlayerView().playMedia(mrl, mediaOptions);
							e.getComponent().setEnabled(true);
						}
					} else {
						if (!streamer.getStream().equals("twitch.tv/"+channel.getName())) {
							LivestreamerFactory.removeInstance(streamer);
							e.getComponent().setEnabled(false);
							String mrl = model.getEmbeddedPlayerModel().startInstance("twitch.tv/"+channel.getName(),LivestreamerFactory.getDefaultQuality());
							String mediaOptions = "--network-cache=5000";
							if (mrl != null) {
								view.getPlayerView().playMedia(mrl, mediaOptions);
								e.getComponent().setEnabled(true);
							}
						}
					}
				}
			} else if (e.getComponent().isEnabled() && 
					(e.getComponent().getName().equals("controlFullscreen") || e.getComponent().getClass().equals(EmbeddedPlayerView.MouseClickOverlay.class)) ){
						if (e.getClickCount() >= 2) {
							view.getPlayerView().toggleFullscreen();
							if (view.getPlayerView().isFullscreen()) {
								view.getChannelListView().setVisible(false);
							} else {
								view.getChannelListView().setVisible(true);
							}
						}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()) {
					e.getComponent().setBackground(colorLive.brighter());
					e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					e.getComponent().setBackground(colorOff.brighter());
				}
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()) e.getComponent().setBackground(colorLive);
				else e.getComponent().setBackground(colorOff);
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) view.getPlayerView().setVolume(view.getPlayerView().getVolume() + 10); 
			else view.getPlayerView().setVolume(view.getPlayerView().getVolume() - 10);
		}
	}

}
