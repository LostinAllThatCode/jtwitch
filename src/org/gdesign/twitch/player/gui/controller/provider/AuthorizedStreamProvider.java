package org.gdesign.twitch.player.gui.controller.provider;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.api.resource.streams.StreamList;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.twitch.player.gui.controller.provider.interfaces.IStreamProvider;

public class AuthorizedStreamProvider extends DefaultStreamProvider implements IStreamProvider {
	
	public AuthorizedStreamProvider(MainController controller) throws IOException, TwitchAPINoTokenSpecifiedException, TwitchAPINoPermissionException, TwitchAPIAuthTokenInvalidException {
		super(controller);
		TwitchAPI.setAuthToken(playerConfig.token);
		if (TwitchAPI.hasPermisson(Permission.user_read)) {
			LogManager.getLogger().debug("Authentication test successful.");
		}
	}

	@Override
	public void updateStreamList() {
		LogManager.getLogger().debug("Updating channelmodels...");
		try {
			StreamList streamList = TwitchAPI.getResource("/streams/followed", StreamList.class, 1000);
			if (streamList._total != 0) controller.view.getStreamList().setStreamList(streamList);
		} catch (IOException e) {
			LogManager.getLogger().warn(e);
		} catch (InterruptedException e) {
			LogManager.getLogger().debug("Provider interrupted.");
		}
		LogManager.getLogger().debug("Done updating.");
	}

}
