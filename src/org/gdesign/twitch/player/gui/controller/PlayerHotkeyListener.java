package org.gdesign.twitch.player.gui.controller;

import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.swing.KeyStroke;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class PlayerHotkeyListener implements HotKeyListener {

	private MainController controller;
	private Provider provider;

	public PlayerHotkeyListener(MainController controller) {
		this.controller = controller;
		this.provider = Provider.getCurrentProvider(false);
		Provider.logger.setLevel(Level.OFF);
	}

	public void register(KeyStroke s) {
		provider.register(s, this);
	}

	@Override
	public void onHotKey(HotKey hotKey) {
		switch (hotKey.keyStroke.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (controller.view.getEmbeddedPlayer().isFullscreen()) {
				controller.view.getEmbeddedPlayer().toggleFullscreen();
				controller.view.getStreamList().setVisible(true);
				controller.view.getStreamList().getStreamMenuBar().setVisible(true);
			}
			break;
		case KeyEvent.VK_ENTER:
			if (!controller.view.getEmbeddedPlayer().isFullscreen()) {
				controller.view.getEmbeddedPlayer().toggleFullscreen();
				controller.view.getStreamList().setVisible(false);
				controller.view.getStreamList().getStreamMenuBar().setVisible(false);
			} else {
				controller.view.getEmbeddedPlayer().toggleFullscreen();
				controller.view.getStreamList().setVisible(true);
				controller.view.getStreamList().getStreamMenuBar().setVisible(true);
			}
			break;
		case KeyEvent.VK_SUBTRACT:
		case KeyEvent.VK_MINUS:
			controller.view.getEmbeddedPlayer().setVolume(controller.view.getEmbeddedPlayer().getVolume() - 10);
			break;
		case KeyEvent.VK_ADD:
		case KeyEvent.VK_PLUS:
			controller.view.getEmbeddedPlayer().setVolume(controller.view.getEmbeddedPlayer().getVolume() + 10);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		provider.reset();
		provider.stop();
	}
}
