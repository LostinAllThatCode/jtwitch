package org.gdesign.twitch.player.gui.controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.follows.Follows;
import org.gdesign.twitch.api.resource.users.UserFollowedChannels;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutSmall;
import org.gdesign.twitch.player.gui.view.layout.channellist.strategy.ChannelListLayoutNormal;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.twitch.player.livestreamer.LivestreamerListener;
import org.gdesign.utils.Configuration;

public class MainController implements LivestreamerListener {

	protected MainView view;
	protected MainModel model;
	protected PlayerMouseListener mouseListener;
	protected PlayerHotkeyListener hotkeyListener;
	protected PlayerActionListener actionListener;
	protected Properties config;

	private long updateTimer	= 0;
	private long interval 		= 0;
	private String username		= "";
	private Thread updateThread;
	
	public MainController(MainView view, MainModel model) {
		this.config = new Configuration("jtwitch.properties");
		this.username = config.getProperty("username");

		this.model = model;

		this.actionListener = new PlayerActionListener(this);
		this.mouseListener = new PlayerMouseListener(this);
		this.hotkeyListener = new PlayerHotkeyListener(this);
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.ALT_DOWN_MASK));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));

		this.view = view;
		this.view.getEmbeddedPlayerView().setVolume(Integer.valueOf(config.getProperty("volume")));
		this.view.getEmbeddedPlayerView().setQuality(LivestreamerFactory.getDefaultQuality());
		this.view.getChannelListView().setChannelLayoutStrategy(new ChannelLayoutSmall());
		this.view.getChannelListView().setChannelListLayoutStrategy(new ChannelListLayoutNormal());
		this.view.addMouseListener(mouseListener);
		this.view.addMouseWheelListener(mouseListener);
		this.view.addActionListener(actionListener);
		
		channelviewUpdateThread();
	}

	public void setUsername(String username){
		this.username = username;
	}
	
	public void updateImmediatly(){
		if (updateThread != null) {
			updateThread.interrupt();
			update(interval);
		}
	}
	
	public void update(final long interval) {
		this.interval 		= interval;
		this.updateTimer 	= interval * 4;
		
		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (updateTimer / interval >= 4) {
							List<ChannelModel> copy = new ArrayList<ChannelModel>(model.getChannelListModel().getChannels());
							UserFollowedChannels channels = new TwitchAPI().getResource("https://api.twitch.tv/kraken/users/"+username+"/follows/channels",UserFollowedChannels.class);
							for (Follows f : channels.follows) {
								ChannelModel channelModel = model.getChannelListModel().getChannel(f.channel.name);
								if (channelModel == null) {
									model.getChannelListModel().updateChannelModel(model.getChannelListModel().createChannel(f.channel.name,f.channel.display_name));
								} else {
									model.getChannelListModel().updateChannelModel(channelModel);
									copy.remove(channelModel);
								}
							}
							for (ChannelModel rem : copy) model.getChannelListModel().removeChannel(rem);
							updateTimer = 0;
						} else {
							for (ChannelModel m : model.getChannelListModel().getChannels()) if (m.isOnline()) model.getChannelListModel().updateChannelModel(m);
						}
						
						model.getChannelListModel().waitForUpdate();
						view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
						Thread.sleep(interval);
						updateTimer += interval;
					} catch (TwitchAPIUnauthorizedAccessException e) {
						LogManager.getLogger().warn(e);
						e.printStackTrace();				
					} catch (InterruptedException e) {
						LogManager.getLogger().warn("Update loop terminated.");
						break;
					} catch (IOException e) {
						LogManager.getLogger().warn(e);
						e.printStackTrace();
					}
					
				}
			}
		});
		updateThread.start();
	}

	private synchronized void channelviewUpdateThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					List<ChannelModel> copy = new ArrayList<ChannelModel>(model.getChannelListModel().getChannels());
					if (copy.size() == 0) LogManager.getLogger().debug("Obtaining data from twitch servers...");
					for (ChannelModel m : copy) {
						ChannelView channelView = view.getChannelListView().getChannel(m.getName());
						if (channelView != null) {
							channelView.setChannelGame(m.getGame())
										.setChannelViewers(String.valueOf(m.getViewers()))
										.setChannelAction(m.getAction())
										.setOnline(m.isOnline());
							channelView.doCustomLayout();
						} else {
							channelView = view.getChannelListView().createChannel(m.getName())
										.setChannelName(m.getDisplayname())
										.setChannelGame(m.getGame())
										.setChannelViewers(String.valueOf(m.getViewers()))
										.setChannelAction(m.getAction())
										.setOnline(m.isOnline());
							channelView.addMouseListener(mouseListener);
							channelView.addMouseMotionListener(mouseListener);
							channelView.doCustomLayout();
						}
						
						if (view.getChannelListView().showOfflineStreams()){
							channelView.setVisible(true);
							if (m.isOnline()) {
								channelView.setEnabled(true); 
								channelView.setIcon(m.getIconUrl());
							} else channelView.setEnabled(false);
						} else {
							if (m.isOnline()) {
								channelView.setEnabled(true);
								channelView.setVisible(true);
								channelView.setIcon(m.getIconUrl());
							} else {
								channelView.setEnabled(false);
								channelView.setVisible(false);
							}
						}
						
					}
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			} 
		}).start();
	}

	@Override
	public void streamStarted(LivestreamerInstance livestreamer) {
		model.getEmbeddedPlayerModel().setInstance(livestreamer);
		model.getChannelListModel().getChannel(livestreamer.getChannel()).setAction("PLAYING");
		view.getEmbeddedPlayerView().playMedia(livestreamer.getMRL(), "");
		view.getEmbeddedPlayerView().setControlValue("STATUS",
				"[" + livestreamer.getMRL() + "] [" + livestreamer.getQuality()+"] Fetching data from " + livestreamer.getStream()+ "...");
	}

	@Override
	public void streamEnded(LivestreamerInstance livestreamer) {
		model.getEmbeddedPlayerModel().setInstance(null);
		model.getChannelListModel().getChannel(livestreamer.getChannel()).setAction("");
		view.getEmbeddedPlayerView().stopMedia();
		view.getEmbeddedPlayerView().setControlValue("STATUS","[" + livestreamer.getMRL() + "] Stream closed.");
	}

}
