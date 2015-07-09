package org.gdesign.twitch.player.gui.controller.provider;

import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.twitch.player.gui.controller.provider.interfaces.IStreamProvider;
import org.gdesign.utils.ResourceManager;

public abstract class DefaultStreamProvider implements IStreamProvider {
	protected PlayerConfiguration playerConfig;
	protected MainController controller;
	protected boolean interupted;
	
	public DefaultStreamProvider(MainController controller) {
		this.playerConfig 	= ResourceManager.getPlayerConfiguration();
		this.controller 	= controller;
	}
}
