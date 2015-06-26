package org.gdesign.twitch.player.gui.listener;

import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.swing.KeyStroke;

import org.gdesign.twitch.player.gui.controller.MainController;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class PlayerHotkeyListener implements HotKeyListener {

	private MainController controller;
	private Provider provider;
	
	public PlayerHotkeyListener(MainController controller) {
		this.controller = controller;
		this.provider	= Provider.getCurrentProvider(false);
		Provider.logger.setLevel(Level.OFF);
	}
	
	public void register(KeyStroke s){
		provider.register(s, this);
	}
	

	@Override
	public void onHotKey(HotKey hotKey) {
		switch (hotKey.keyStroke.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (controller.getView().getEmbeddedPlayerView().isFullscreen()) {
				controller.getView().getEmbeddedPlayerView().toggleFullscreen();
				controller.getView().getChannelListView().setVisible(true);
			}
			break;
		case KeyEvent.VK_ENTER:
			if (!controller.getView().getEmbeddedPlayerView().isFullscreen()) {
				controller.getView().getEmbeddedPlayerView().toggleFullscreen();
				controller.getView().getChannelListView().setVisible(false);
			} else {
				controller.getView().getEmbeddedPlayerView().toggleFullscreen();
				controller.getView().getChannelListView().setVisible(true);
			}
			break;
		case KeyEvent.VK_SUBTRACT:
			controller.getView().getEmbeddedPlayerView().setVolume(controller.getView().getEmbeddedPlayerView().getVolume()-10);
			break;
		case KeyEvent.VK_ADD:
			controller.getView().getEmbeddedPlayerView().setVolume(controller.getView().getEmbeddedPlayerView().getVolume()+10);
			break;
		default:
			break;
		}
		
	}
}
