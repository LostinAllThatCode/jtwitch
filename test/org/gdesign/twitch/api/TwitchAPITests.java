package org.gdesign.twitch.api;

import static org.junit.Assert.*;

import java.io.IOException;

import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.channels.Channel;
import org.gdesign.twitch.api.resource.channels.MyChannel;
import org.junit.Test;

import com.google.gson.JsonSyntaxException;

public class TwitchAPITests {

	@Test
	public void init(){
		assertNotNull(new TwitchAPI());
	}
	
	@Test
	public void authWithToken() throws JsonSyntaxException, IOException, TwitchAPIUnauthorizedAccessException{
		assertTrue("oAuth2 token authentication failed.",new TwitchAPI("TOKENHERE").authorized());
	}
	
	@Test
	public void getPrivateChannel() throws JsonSyntaxException, IOException, TwitchAPIUnauthorizedAccessException {
		MyChannel mychannel = new TwitchAPI("TOKENHERE").getResource(" https://api.twitch.tv/kraken/channel", MyChannel.class);
		assertNotSame("its1z0", mychannel.name);
	}	
	
	@Test
	public void getGlobalChannel() throws JsonSyntaxException, IOException, TwitchAPIUnauthorizedAccessException {
		Channel channel = new TwitchAPI().getResource(" https://api.twitch.tv/kraken/channels/its1z0", Channel.class);
		assertNotSame("its1z0", channel.name);
	}	

}
