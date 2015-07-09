package org.gdesign.twitch.player.gui.view.streams.layout;

import java.awt.Color;
import java.awt.Dimension;

import org.gdesign.utils.GsonDataSet;

public class StreamListViewLayout extends GsonDataSet {
	public Color bg_color;
	public Color scrollbar_v_color,scrollbar_h_color;
	public Dimension scrollbar_v_dim,scrollbar_h_dim;
	public int scrollbar_unit_inc;
	public StreamViewLayout stream;
}
