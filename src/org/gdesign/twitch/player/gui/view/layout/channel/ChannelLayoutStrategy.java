package org.gdesign.twitch.player.gui.view.layout.channel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public interface ChannelLayoutStrategy {

	public Dimension getOfflineDimension();
	public Dimension getOnlineDimension();
	public Font getNameFont();
	public Color getNameFontColor();
	public Color getNameFontColorHover();
	public float getNameSizeOnline();
	public float getNameSizeOffline();
	public Font getViewerFont();
	public Color getViewerFontColor();
	public Color getViewerFontColorHover();
	public float getViewerSize();
	public Font getGameFont();
	public Color getGameFontColor();
	public Color getGameFontColorHover();
	public float getGameSize();
	public Color getBackgroundColorOffline();
	public Color getBackgroundColorOnline();
	public Color getBackgroundColorHover();
}
