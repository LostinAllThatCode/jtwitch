package org.gdesign.twitch.player.gui.view.layout.channellist.strategy;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.layout.channellist.ChannelListLayout;
import org.gdesign.twitch.player.gui.view.layout.channellist.ChannelListLayoutStrategy;
import org.gdesign.utils.ResourceManager;

import com.google.gson.Gson;

public abstract class ChannelListLayoutDefault implements ChannelListLayoutStrategy {
	protected ChannelListLayout layout;
	
	public ChannelListLayoutDefault(String jsonFile) {
		Reader reader = ResourceManager.getLayout(jsonFile);
		if (reader == null) {
			LogManager.getLogger().error("Layout configuration file not found or data corrupt.\nLayoutfile: "+jsonFile);
			System.exit(1);
		}
		this.layout = new Gson().fromJson(reader, ChannelListLayout.class);
	}

	@Override
	public String getVerion(){
		return layout.version;
	}
	
	@Override
	public boolean showOfflineStreams() {
		return layout.show_offline_streams;
	}
	
	@Override
	public Dimension getDimension(){
		return new Dimension((layout.width == -1 ? Integer.MAX_VALUE : layout.width),
				(layout.height == -1 ? Integer.MAX_VALUE : layout.height));
	}
	
	@Override
	public Color getBackground(){
		return Color.decode(layout.background);
	}
	
	
	@Override
	public int getVerticalScrollBarWidth(){
		return layout.vert_scrollbar_width;
	}
	
	@Override
	public Color getVerticalScrollBarColor(){
		return Color.decode(layout.vert_scrollbar_color);	
	}
	
	@Override
	public int getHorizontalScrollBarHeight(){
		return layout.hori_scrollbar_height;
	}
	
	@Override
	public Color getHorizontalScrollBarColor(){
		return Color.decode(layout.hori_scrollbar_color);
	}
	
	@Override
	public int getScrollbarUnitIncrement(){
		return layout.scrollbar_unit_increment;
	}
}
