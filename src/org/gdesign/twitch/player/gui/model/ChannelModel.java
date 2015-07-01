package org.gdesign.twitch.player.gui.model;

public class ChannelModel implements Comparable<ChannelModel> {

	private boolean online;
	private String displayName, name, game, action;
	private int viewers;

	public ChannelModel(String channelName) {
		this.name = channelName;
		this.game = "";
		this.viewers = 0;
		this.action = "";
	}

	public boolean isOnline() {
		return this.online;
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayname() {
		return displayName;
	}

	public String getGame() {
		return this.game;
	}

	public int getViewers() {
		return this.viewers;
	}

	public String getAction() {
		return this.action;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public void setDisplayname(String displayName) {
		this.displayName = displayName;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public void setViewers(int viewers) {
		this.viewers = viewers;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int compareTo(ChannelModel o) {
		if (this.viewers > o.getViewers())
			return -1;
		else if (this.viewers < o.getViewers())
			return 1;
		else if (viewers == 0 && o.getViewers() == 0) {
			if (isOnline() && !o.isOnline())
				return -1;
			if (!isOnline() && o.isOnline())
				return 1;
			return name.compareTo(o.getName());
		} else
			return 0;
	}

	@Override
	public String toString() {
		return "[" + name + "," + game + "," + viewers + "," + action + ","
				+ online + "]";
	}

}
