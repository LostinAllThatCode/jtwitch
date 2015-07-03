package org.gdesign.twitch.player.gui.view.layout.channellist.strategy;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.layout.channellist.ChannelListLayout;
import org.gdesign.twitch.player.gui.view.layout.channellist.ChannelListLayoutStrategy;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public abstract class ChannelListLayoutDefault implements ChannelListLayoutStrategy {
	protected ChannelListLayout layout;
	
	public ChannelListLayoutDefault(String jsonFile) {
		try {
			FileReader reader = new FileReader(ClassLoader.getSystemResource(jsonFile).getFile());
			this.layout = new Gson().fromJson(reader, ChannelListLayout.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			LogManager.getLogger().error("Layout configuration file not found or data corrupt.\nLayoutfile: "+layout);
			System.exit(1);
		}
	}

	public String getVerion(){
		return layout.version;
	}
	
	public boolean showOfflineStreams() {
		return layout.show_offline_streams;
	}
	
	public Dimension getDimension(){
		return new Dimension((layout.width == -1 ? Integer.MAX_VALUE : layout.width),
				(layout.height == -1 ? Integer.MAX_VALUE : layout.height));
	}
	
	public Color getBackground(){
		return Color.decode(layout.background);
	}
	
	
	public int getVerticalScrollBarWidth(){
		return layout.vert_scrollbar_width;
	}
	
	public Color getVerticalScrollBarColor(){
		return Color.decode(layout.vert_scrollbar_color);	
	}
	
	public int getHorizontalScrollBarHeight(){
		return layout.hori_scrollbar_height;
	}
	
	public Color getHorizontalScrollBarColor(){
		return Color.decode(layout.hori_scrollbar_color);
	}
	
	public int getScrollbarUnitIncrement(){
		return layout.scrollbar_unit_increment;
	}
}
