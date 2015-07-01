package org.gdesign.twitch.player.gui.controller;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.follows.Follows;
import org.gdesign.twitch.api.resource.streams.StreamInfo;
import org.gdesign.twitch.api.resource.users.UserFollowedChannels;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.twitch.player.livestreamer.LivestreamerListener;
import org.gdesign.utils.Configuration;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class MainController implements LivestreamerListener{
	
	protected MainView view;
	protected MainModel model;
	protected PlayerMouseListener mouseListener;
	protected PlayerHotkeyListener hotkeyListener;
	protected Properties config;
	
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
		
		this.view.getChannelListView().setEnabled(false);
	}
	
	private synchronized void updateChannelModel(ChannelModel m) throws ParseException {
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
			updateChannelView(m);
		}
	}
	
	private synchronized void updateChannelView(final ChannelModel m){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ChannelView channelView	= view.getChannelListView().getChannel(m.getName());
				if (channelView != null) {
					channelView.setChannelGame(m.getGame());
					channelView.setChannelViewers(String.valueOf(m.getViewers()));
					channelView.setChannelAction(m.getAction());
					channelView.setOnline(m.isOnline());
					channelView.repaint();
				} else {
					channelView = new ChannelView(m.getName());
					channelView.setChannelName(m.getDisplayname());
					channelView.setChannelGame(m.getGame());
					channelView.setChannelViewers(String.valueOf(m.getViewers()));
					channelView.setChannelAction(m.getAction());
					channelView.setOnline(m.isOnline());
					channelView.addMouseListener(mouseListener);
					view.getChannelListView().addChannel(channelView);
				}
			}
		}).start();
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
							for (ChannelModel m : model.getChannelListModel().getChannels()) updateChannelModel(m);
							view.getChannelListView().sortChannels(model.getChannelListModel().getChannels());
						}
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
		
	@Override
	public void streamStarted(LivestreamerInstance livestreamer) {
		model.getChannelListModel().getChannel(livestreamer.getChannel()).setAction("PLAYING");
		updateChannelView(model.getChannelListModel().getChannel(livestreamer.getChannel()));
		view.getEmbeddedPlayerView().playMedia(livestreamer.getMRL(), "");
		view.getEmbeddedPlayerView().setControlValue("STATUS", "["+livestreamer.getMRL()+"] ["+livestreamer.getQuality()+"] Fetching data from "+livestreamer.getStream()+"...");
	}

	@Override
	public void streamEnded(LivestreamerInstance livestreamer) {
		model.getChannelListModel().getChannel(livestreamer.getChannel()).setAction("");
		updateChannelView(model.getChannelListModel().getChannel(livestreamer.getChannel()));
		view.getEmbeddedPlayerView().stopMedia();
		view.getEmbeddedPlayerView().setControlValue("STATUS", "["+livestreamer.getMRL()+"] Stream closed.");
	}

}
