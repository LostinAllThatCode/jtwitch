package org.gdesign.twitch.api;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.api.resource.channels.Channel;
import org.gdesign.twitch.api.resource.channels.MyChannel;
import org.junit.Test;

import com.google.gson.JsonSyntaxException;

public class TwitchAPITests {

	@Test
	public void authWithToken() throws JsonSyntaxException, IOException,  TwitchAPINoTokenSpecifiedException, TwitchAPIAuthTokenInvalidException {
		TwitchAPI.setAuthToken("7vc7yh30rhk16fcajq4y2bc64j494r");
		assertTrue("oAuth2 token authentication failed.", TwitchAPI.authorized());
	}

	@Test
	public void getPrivateChannel() throws JsonSyntaxException, IOException{
		TwitchAPI.setAuthToken("7vc7yh30rhk16fcajq4y2bc64j494r");
		MyChannel mychannel = TwitchAPI.getResource("/channel", MyChannel.class);
		assertNotSame("its1z0", mychannel.name);
	}

	@Test
	public void getGlobalChannel() throws JsonSyntaxException, IOException {
		TwitchAPI.setAuthToken("7vc7yh30rhk16fcajq4y2bc64j494r");
		Channel channel = TwitchAPI.getResource("/channels/its1z0", Channel.class);
		assertNotSame("its1z0", channel.name);
	}

	@Test
	public void debugging() throws IOException, TwitchAPINoTokenSpecifiedException{
		try {
			TwitchAPI.authorizeApp(Permission.user_read);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
