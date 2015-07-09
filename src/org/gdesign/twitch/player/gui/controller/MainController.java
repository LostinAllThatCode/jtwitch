package org.gdesign.twitch.player.gui.controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.provider.AuthorizedStreamProvider;
import org.gdesign.twitch.player.gui.controller.provider.interfaces.IStreamProvider;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.gui.view.streams.layout.StreamListViewLayout;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory.MessageType;
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
	private IStreamProvider channelProvider;
	
	public MainController(MainView view, MainModel model) {
		this.config = ResourceManager.getPlayerConfiguration();

		this.model = model;

		this.actionListener = new PlayerActionListener(this);
		this.mouseListener = new PlayerMouseListener(this);
		
		this.hotkeyListener = new PlayerHotkeyListener(this);
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.ALT_DOWN_MASK));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
		this.hotkeyListener.register(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));

		this.view = view;
		this.view.getEmbeddedPlayer().setVolume(config.player.volume);
		this.view.getStreamList().setLayoutStrategy(ResourceManager.getLayout("streamlist.default.layout.json", StreamListViewLayout.class));
		this.view.addMouseListener(mouseListener);
		this.view.addMouseWheelListener(mouseListener);
		this.view.addActionListener(actionListener);
		
		this.interval = 60000;
	
		if (this.config.token.length() != 0) {
			try {
				this.setChannelProvider(new AuthorizedStreamProvider(this));
			} catch (IOException | TwitchAPINoTokenSpecifiedException | TwitchAPINoPermissionException
					| TwitchAPIAuthTokenInvalidException e) {
				LogManager.getLogger().warn(e + ". No channel provider is set.");
				MessagePopupFactory.getFactory().createMessagePopup(MessageType.ERROR, e + ". No channel provider is set.",true);
			}
		} else {
			MessagePopupFactory.getFactory().createMessagePopup(MessageType.INFO,"Please click on the Twitch icon in the top left corner "
					+ "to authorize this app.\nAuthorizing this app will allow it to display your followed streams." ,true);
		}

		LivestreamerFactory.addListener(this);
	}
	
	public IStreamProvider getChannelProvider(){
		return this.channelProvider;
	}
	
	public void setChannelProvider(IStreamProvider provider) {
		channelProvider = provider;
		forceUpdate();
	}
	
	public void forceUpdate(){
		if (updateThread != null) {
			updateThread.interrupt();
		}
		update(interval);
	}
	
	private void update(final long interval) {
		this.interval 		= interval;
		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (channelProvider != null) {
							channelProvider.updateStreamList();
							Thread.sleep(interval);
						} else {
							LogManager.getLogger().warn("No channel provider is set.");
							Thread.sleep(1000);
						}
						
					} catch (InterruptedException e) {
						LogManager.getLogger().warn("Update loop terminated.");
						view.validate();
						view.repaint();
						break;
					} catch (NullPointerException e) {
						MessagePopupFactory.getFactory().createMessagePopup(MessageType.WARN, e+": No data received from Twitch.",true);
						LogManager.getLogger().warn("Provider couldn't receive data from twitch.");
					}
				}
			}
		});
		updateThread.start();
	}

	@Override
	public void streamStarted(LivestreamerInstance livestreamer) {
		model.getEmbeddedPlayerModel().setInstance(livestreamer);
		view.getStreamList().setStreamPlaying(livestreamer.getChannel(), true);
		view.getEmbeddedPlayer().playMedia(livestreamer.getMRL(), "");
		view.getEmbeddedPlayer().setControlValue("STATUS",
				"[" + livestreamer.getMRL() + "] [" + livestreamer.getQuality()+"] Fetching data from " + livestreamer.getStream()+ "...");
	}

	@Override
	public void streamEnded(LivestreamerInstance livestreamer) {
		model.getEmbeddedPlayerModel().setInstance(null);
		view.getStreamList().setStreamPlaying(livestreamer.getChannel(), false);
		view.getEmbeddedPlayer().stopMedia();
		view.getEmbeddedPlayer().setControlValue("STATUS","[" + livestreamer.getMRL() + "] Stream closed.");
	}

}
