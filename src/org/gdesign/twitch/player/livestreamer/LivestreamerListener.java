package org.gdesign.twitch.player.livestreamer;

public interface LivestreamerListener {
	public void streamStarted(LivestreamerInstance livestreamer);

	public void streamEnded(LivestreamerInstance livestreamer);
}
