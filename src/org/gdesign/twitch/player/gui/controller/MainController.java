package org.gdesign.twitch.player.gui.controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.provider.AuthorizedStreamProvider;
import org.gdesign.twitch.player.gui.controller.provider.IChannelProvider;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutSmall;
import org.gdesign.twitch.player.gui.view.layout.channellist.strategy.ChannelListLayoutNormal;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.twitch.player.livestreamer.LivestreamerListener;
import org.gdesign.utils.ResourceManager;

public class MainController implements LivestreamerListener {

	public MainView view;
	public MainModel model;
	protected PlayerMouseListener mouseListener;
	protected PlayerHotkeyListener hotkeyListener;
	protected PlayerActionListener actionListener;
	protected PlayerConfiguration config;

	private long interval;
	private Thread updateThread;
	private IChannelProvider channelProvider;
	
	public MainController(MainView view, MainModel model) {
		this.config = ResourceManager.getPlayerConfiguration();

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
		this.view.getEmbeddedPlayerView().setVolume(config.player.volume);
		this.view.getEmbeddedPlayerView().setQuality(LivestreamerFactory.getDefaultQuality());
		this.view.getChannelListView().setChannelLayoutStrategy(new ChannelLayoutSmall());
		this.view.getChannelListView().setChannelListLayoutStrategy(new ChannelListLayoutNormal());
		this.view.addMouseListener(mouseListener);
		this.view.addMouseWheelListener(mouseListener);
		this.view.addActionListener(actionListener);

		try {
			this.channelProvider = new AuthorizedStreamProvider(this);
		} catch (Exception | TwitchAPINoPermissionException | TwitchAPINoTokenSpecifiedException | TwitchAPIAuthTokenInvalidException e) {
			LogManager.getLogger().warn(e + "\n!No channel provider is set.");
		}

		channelviewUpdateThread();
	}
	
	public IChannelProvider getChannelProvider(){
		return this.channelProvider;
	}
	
	public void setChannelProvider(IChannelProvider channelProvider) {
		this.channelProvider = channelProvider;
	}
	
	public void updateImmediatly(){
		if (updateThread != null) {
			updateThread.interrupt();
			update(interval);
		}
	}
	
	public void update(final long interval) {
		this.interval 		= interval;
		
		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (channelProvider != null) {
							channelProvider.updateChannels();
						} else {
							LogManager.getLogger().warn("No channel provider is set.");
						}
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						LogManager.getLogger().warn("Update loop terminated.");
						break;
					}
					
				}
			}
		});
		updateThread.start();
	}

	private void channelviewUpdateThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true){
					Collection<ChannelModel> channelmodelList = model.getChannelListModel().getChannels();
					if (channelmodelList != null){
						for (ChannelModel m : channelmodelList) {
							ChannelView channelView = view.getChannelListView().getChannel(m.getName());
							if (channelView == null) {
								channelView = view.getChannelListView().createChannel(m.getName());
								channelView.addMouseListener(mouseListener);
								channelView.addMouseMotionListener(mouseListener);
							}
							channelView.setChannelGame(m.getGame())
								.setChannelName(m.getDisplayname())
								.setChannelViewers(String.valueOf(m.getViewers()))
								.setChannelAction(m.getAction())
								.setOnline(m.isOnline())
								.doCustomLayout()
								.setIcon(m.getIconUrl());
							
							if (view.getChannelListView().showOfflineStreams()) {
								channelView.setVisible(true);
							} else {
								if (m.isOnline()) channelView.setVisible(true); else channelView.setVisible(false);
							}
							
							channelView.setEnabled(true);
							channelView.validate();
							channelView.repaint();
						}			
					}
		
					try {
						Thread.sleep(120);
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
