package org.gdesign.twitch.player.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;


public class EmbeddedPlayerControlView extends JPanel {
	public static enum Control { PLAY_STOP }
	private static final long serialVersionUID = -6544459734640671955L;
	
	private Control control;
	private boolean hover;
	private boolean isRunning;
	
	Color hlink = new Color(80,40,180);
	
	public EmbeddedPlayerControlView(Control c) {
		setBackground(Color.DARK_GRAY);
		setControlType(c);
		this.hover = false;
		this.isRunning = false;
	}
	
	public void setHover(boolean hover){
		this.hover = hover;
		setCursor(Cursor.getPredefinedCursor((hover && isRunning ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR))); 
		repaint();
	}
	
	public void setRunning(boolean running){
		this.isRunning = running;
	}
	
	public Control getControlType(){
		return control;
	}
	
	public void setControlType(Control c){
		this.control = c;
		this.setName(control.toString());
		switch (c) {
		case PLAY_STOP:
			setMinimumSize(new Dimension(20,20));
			setPreferredSize(new Dimension(20,20));
			setMaximumSize(new Dimension(20,20));
			break;
		default:
			break;
		}
		revalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Arial",Font.PLAIN,10));
		if (!hover) {
			if (!isRunning) g.setColor(Color.DARK_GRAY.darker()); else  g.setColor(Color.WHITE);
		} else {
			if (!isRunning) g.setColor(Color.DARK_GRAY.darker()); else  g.setColor(hlink.brighter());
		}
		switch (control) {
			case PLAY_STOP:
				Polygon play = new Polygon();
				play.addPoint(2, 2); play.addPoint(2, 18); play.addPoint(18, 10);
				if (hover && isRunning) g.fillRect(2,2,16,16); else g.fillPolygon(play);						
				break;
			default:
				break;
		}
	}
		
	
}
