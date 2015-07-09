package org.gdesign.twitch.player.gui.view.streams.layout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.EmptyBorder;

import org.gdesign.utils.GsonDataSet;

public class StreamViewLayout extends GsonDataSet {
	public Dimension dim;
	public Cursor cursor;
	public Color bg_color,bg_color_hover;
	public Font name_font;
	public Color name_fg_color,name_fg_color_hover;
	public EmptyBorder name_border;
	public Font game_font;
	public Color game_fg_color,game_fg_color_hover;
	public EmptyBorder game_border;
	public Font viewers_font;
	public Color viewers_fg_color,viewers_fg_color_hover;
	public EmptyBorder viewers_border;
	
	/*
		"dim" 						: {"width":250,"height":48} ,
		"cursor"					: {"type":12, "name":"Handcursor"},
		"bg_color" 					: {"value": "351273020","falpha":0.0},
		"bg_color_hover"			: {"value": "351273020","falpha":0.0},
		"name_font"					: {"name":"GnuolaneRg-Regular","style":1,"size":15,"pointSize":15.0,"fontSerializedDataVersion":1},
		"name_fg_color"				: {"value": "-1","falpha":0.0},
		"name_fg_color_hover"		: {"value": "-11523916","falpha":0.0},
		"name_border"				: {"left":8,"right":0,"top":0,"bottom":-4},
		"game_font"					: {"name":"Arial","style":1,"size":15,"pointSize":15.0,"fontSerializedDataVersion":1},
		"game_fg_color"				: {"value": "-1","falpha":0.0},
		"game_fg_color_hover"		: {"value": "11523916","falpha":0.0},
		"game_border"				: {"left":8,"right":0,"top":0,"bottom":-4},
		"viewers_font"				: {"name":"Arial","style":1,"size":15,"pointSize":15.0,"fontSerializedDataVersion":1},
		"viewers_fg_color"			: {"value": "-1","falpha":0.0},
		"viewers_fg_color_hover"	: {"value": "11523916","falpha":0.0},
		"viewers_border"			: {"left":8,"right":0,"top":0,"bottom":-4}
	 */
}
