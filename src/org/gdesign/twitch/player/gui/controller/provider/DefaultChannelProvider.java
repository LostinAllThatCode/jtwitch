package org.gdesign.twitch.player.gui.controller.provider;

import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.utils.ResourceManager;

public abstract class DefaultChannelProvider implements IChannelProvider {
	protected PlayerConfiguration playerConfig;
	protected MainController controller;
	
	public DefaultChannelProvider(MainController controller) {
		this.playerConfig 	= ResourceManager.getPlayerConfiguration();
		this.controller 	= controller;
	}
}
