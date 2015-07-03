package org.gdesign.twitch.player.gui.view.layout.channellist;

import java.awt.Color;
import java.awt.Dimension;

public interface ChannelListLayoutStrategy {
	
	public String getVerion();
	public boolean showOfflineStreams();
	public Dimension getDimension();
	public Color getBackground();
	public int getVerticalScrollBarWidth();
	public Color getVerticalScrollBarColor();
	public int getHorizontalScrollBarHeight();
	public Color getHorizontalScrollBarColor();
	public int getScrollbarUnitIncrement();
	
}
