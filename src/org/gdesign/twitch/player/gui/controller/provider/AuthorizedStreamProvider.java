package org.gdesign.twitch.player.gui.controller.provider;

import java.io.IOException;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.api.resource.streams.Stream;
import org.gdesign.twitch.api.resource.users.UserFollowedStreams;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.twitch.player.gui.model.ChannelModel;

public class AuthorizedStreamProvider extends DefaultChannelProvider {

	public AuthorizedStreamProvider(MainController controller) throws IOException, TwitchAPINoTokenSpecifiedException, TwitchAPINoPermissionException, TwitchAPIAuthTokenInvalidException {
		super(controller);
		TwitchAPI.setAuthToken(playerConfig.token);
		if (TwitchAPI.authorized()) {
			if (TwitchAPI.hasPermisson(Permission.user_read)) {
				LogManager.getLogger().trace(this.getClass().getSimpleName() + " initialized.");
			}
		} 
	}

	@Override
	public void updateChannels() {
		try {
			UserFollowedStreams onlineFollowedStreams = TwitchAPI.getResource("/streams/followed", UserFollowedStreams.class);
			if (onlineFollowedStreams._total != 0) {
				for (final Stream stream : onlineFollowedStreams.streams) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							ChannelModel channelModel = controller.model.getChannelListModel().getChannel(stream.channel.name);
							if (channelModel == null){
								channelModel = controller.model.getChannelListModel().createChannel(stream.channel.name);
								channelModel.setDisplayname(stream.channel.display_name);
								channelModel.setIconUrl(stream.channel.logo);
							}
							if (controller.model.getChannelListModel().updateChannelModel(channelModel)){
								Collection<ChannelModel> sortedChannels = controller.model.getChannelListModel().sortChannels();
								controller.view.getChannelListView().sortChannels(sortedChannels);
							}
						}
					}).start();
				}
			} else LogManager.getLogger().debug("No one you follow is streaming right now.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

}
