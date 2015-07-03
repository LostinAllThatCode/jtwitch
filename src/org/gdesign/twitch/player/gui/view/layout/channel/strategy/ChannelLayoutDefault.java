package org.gdesign.twitch.player.gui.view.layout.channel.strategy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayout;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayoutStrategy;
import org.gdesign.utils.ResourceManager;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public abstract class ChannelLayoutDefault implements ChannelLayoutStrategy {
	
	protected ChannelLayout layout;
	
	public ChannelLayoutDefault(String jsonFile) {
		try {
			FileReader reader = new FileReader(ClassLoader.getSystemResource(jsonFile).getFile());
			this.layout = new Gson().fromJson(reader, ChannelLayout.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			LogManager.getLogger().error("Layout configuration file not found or data corrupt.\nLayoutfile: "+layout);
			System.exit(1);
		}
	}

	public Color getBackgroundColorHover() {
		return Color.decode(layout.bg_hover);
	}

	public Color getBackgroundColorOffline() {
		return Color.decode(layout.bg_offline);
	}

	public Color getBackgroundColorOnline() {
		return Color.decode(layout.bg_online);
	}
	
	public Font getGameFont() {
		if (layout.game_font.endsWith(".ttf")) return ResourceManager.getFont(layout.game_font);
		else return new Font(layout.game_font,Font.BOLD, (int) layout.game_font_size); 
	}

	public Color getGameFontColor() {
		return Color.decode(layout.game_font_color);
	}
	
	public Color getGameFontColorHover() {
		return Color.decode(layout.game_font_color_hover);
	}

	public float getGameSize() {
		return layout.game_font_size;
	}

	public Font getNameFont() {
		if (layout.name_font.endsWith(".ttf")) return ResourceManager.getFont(layout.name_font);
		else return new Font(layout.name_font, Font.BOLD, (int) layout.name_font_size_offline); 
	}

	public Color getNameFontColor() {
		return Color.decode(layout.name_font_color);
	}
	public Color getNameFontColorHover() {
		return Color.decode(layout.name_font_color_hover);
	}

	public float getNameSizeOffline() {
		return layout.name_font_size_offline;
	}

	public float getNameSizeOnline() {
		return layout.name_font_size;
	}

	public Dimension getOfflineDimension() {
		return new Dimension(layout.offline_width,layout.offline_height);
	}

	public Dimension getOnlineDimension() {
		return new Dimension(layout.online_width,layout.online_height);
	}

	public Font getViewerFont() {
		if (layout.viewer_font.endsWith(".ttf")) return ResourceManager.getFont(layout.viewer_font);
		else return new Font(layout.viewer_font, Font.BOLD, (int) layout.viewer_font_size); 
	}

	public Color getViewerFontColor() {
		return Color.decode(layout.viewer_font_color);
	}

	public Color getViewerFontColorHover() {
		return Color.decode(layout.viewer_font_color_hover);
	}

	public float getViewerSize() {
		return layout.viewer_font_size;
	}
	
}
