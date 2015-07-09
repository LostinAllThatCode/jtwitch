package org.gdesign.twitch.api;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.api.resource.channels.Channel;
import org.gdesign.twitch.api.resource.channels.AuthorizedChannel;
import org.gdesign.twitch.api.resource.streams.StreamList;
import org.junit.Test;

import com.google.gson.JsonSyntaxException;

public class TwitchAPITests {

	@Test
	public void authWithToken() throws JsonSyntaxException, IOException,  TwitchAPINoTokenSpecifiedException, TwitchAPIAuthTokenInvalidException {
		TwitchAPI.setAuthToken("");
		assertTrue("oAuth2 token authentication failed.", TwitchAPI.authorized());
	}

	@Test
	public void getPrivateChannelWithoutToken() throws JsonSyntaxException, IOException{
		TwitchAPI.setAuthToken("");
		TwitchAPI.getResource("/channel", AuthorizedChannel.class);
	}

	@Test
	public void getGlobalChannel() throws JsonSyntaxException, IOException {
		TwitchAPI.setAuthToken("");
		Channel channel = TwitchAPI.getResource("/channels/its1z0", Channel.class);
		assertNotSame("its1z0", channel.name);
	}
	
	@Test
	public void getBigAmountOfData() throws JsonSyntaxException, IOException, InterruptedException{
		assertNotSame(TwitchAPI.getResource("/streams",StreamList.class,500).streams.size(),500);
	}
	
	@Test
	public void getNormalData() throws JsonSyntaxException, IOException, InterruptedException{
		assertNotSame(TwitchAPI.getResource("/streams",StreamList.class)._total,25);
	}

	@Test
	public void authorize(){
		try {
			LogManager.getLogger().debug(TwitchAPI.authorizeApp(Permission.user_read));
		} catch (IOException | URISyntaxException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
