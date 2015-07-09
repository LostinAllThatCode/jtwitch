package org.gdesign.twitch.player.gui.controller.provider;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.streams.StreamList;
import org.gdesign.twitch.player.gui.controller.MainController;

public class ViewerStreamProvider extends DefaultStreamProvider {

	private int amount;
	
	public ViewerStreamProvider(MainController controller, int amount) {
		super(controller);
		this.amount = amount;
	}
	
	public ViewerStreamProvider(MainController controller) {
		this(controller,25);
	}

	@Override
	public void updateStreamList() {
		LogManager.getLogger().trace("Updating channelmodels...");
		try {
			StreamList streamList = TwitchAPI.getResource("/streams", StreamList.class, amount);
			if (streamList._total != 0) controller.view.getStreamList().setStreamList(streamList);
		} catch (IOException e) {
			LogManager.getLogger().warn(e);
		} catch (InterruptedException e) {
			LogManager.getLogger().debug("Provider interrupted.");
		}
		LogManager.getLogger().trace("Done updating.");
	}
	

}
