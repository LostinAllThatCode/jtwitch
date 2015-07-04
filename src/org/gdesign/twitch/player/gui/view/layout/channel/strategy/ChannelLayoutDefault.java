package org.gdesign.twitch.player.gui.view.layout.channel.strategy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayout;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayoutStrategy;
import org.gdesign.utils.ResourceManager;

import com.google.gson.Gson;

public abstract class ChannelLayoutDefault implements ChannelLayoutStrategy {
	
	protected ChannelLayout layout;
	
	public ChannelLayoutDefault(String jsonFile) {
		Reader reader = ResourceManager.getLayout(jsonFile);
		if (reader == null) {
			LogManager.getLogger().error("Layout configuration file not found or data corrupt.\nLayoutfile: "+jsonFile);
			System.exit(1);
		}
		this.layout = new Gson().fromJson(reader, ChannelLayout.class);
	}

	@Override
	public Color getBackgroundColorHover() {
		return Color.decode(layout.bg_hover);
	}

	@Override
	public Color getBackgroundColorOffline() {
		return Color.decode(layout.bg_offline);
	}

	@Override
	public Color getBackgroundColorOnline() {
		return Color.decode(layout.bg_online);
	}
	
	@Override
	public Font getGameFont() {
		if (layout.game_font.endsWith(".ttf")) return ResourceManager.getCustomFont(layout.game_font).deriveFont(layout.game_font_size);
		else return new Font(layout.game_font,Font.BOLD, (int) layout.game_font_size); 
	}

	@Override
	public Color getGameFontColor() {
		return Color.decode(layout.game_font_color);
	}
	
	@Override
	public Color getGameFontColorHover() {
		return Color.decode(layout.game_font_color_hover);
	}

	@Override
	public float getGameSize() {
		return layout.game_font_size;
	}

	@Override
	public Font getNameFont() {
		if (layout.name_font.endsWith(".ttf")) return ResourceManager.getCustomFont(layout.name_font).deriveFont(layout.name_font_size_offline);
		else return new Font(layout.name_font, Font.BOLD, (int) layout.name_font_size_offline); 
	}

	@Override
	public Color getNameFontColor() {
		return Color.decode(layout.name_font_color);
	}
	@Override
	public Color getNameFontColorHover() {
		return Color.decode(layout.name_font_color_hover);
	}

	@Override
	public float getNameSizeOffline() {
		return layout.name_font_size_offline;
	}

	@Override
	public float getNameSizeOnline() {
		return layout.name_font_size;
	}

	@Override
	public Dimension getOfflineDimension() {
		return new Dimension(layout.offline_width,layout.offline_height);
	}

	@Override
	public Dimension getOnlineDimension() {
		return new Dimension(layout.online_width,layout.online_height);
	}

	@Override
	public Font getViewerFont() {
		if (layout.viewer_font.endsWith(".ttf")) return ResourceManager.getCustomFont(layout.viewer_font).deriveFont(layout.viewer_font_size);
		else return new Font(layout.viewer_font, Font.BOLD, (int) layout.viewer_font_size);
	}

	@Override
	public Color getViewerFontColor() {
		return Color.decode(layout.viewer_font_color);
	}

	@Override
	public Color getViewerFontColorHover() {
		return Color.decode(layout.viewer_font_color_hover);
	}

	@Override
	public float getViewerSize() {
		return layout.viewer_font_size;
	}
	
}
