package org.gdesign.twitch.player.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class EmbeddedPlayerControlView extends JPanel {
	public static enum Control { PLAY_STOP, FULLSCREEN, VOLUME, STATUS}
	private static final long serialVersionUID = -6544459734640671955L;
	
	private Control control;
	private boolean hover;
	private boolean active;
	private Object value;
	
	Color hlink = new Color(80,40,180);
	
	public EmbeddedPlayerControlView(Control c) {
		setDoubleBuffered(true);
		setBackground(Color.DARK_GRAY);
		setControlType(c);
		this.hover = false;
		this.active = false;
	}
	
	public void setHover(boolean hover){
		this.hover = hover;
		setCursor(Cursor.getPredefinedCursor((hover && active ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR))); 
		repaint();
	}
	
	public void setActive(boolean running){
		this.active = running;
	}
	
	public Control getControlType(){
		return control;
	}
	
	public void setControlType(Control c){
		this.control = c;
		this.setName(control.toString());
		switch (c) {
		case FULLSCREEN:
		case PLAY_STOP:
			setMinimumSize(new Dimension(20,20));
			setPreferredSize(new Dimension(20,20));
			setMaximumSize(new Dimension(20,20));
			break;
		case VOLUME:
			setMinimumSize(new Dimension(200,20));
			setPreferredSize(new Dimension(200,20));
			setMaximumSize(new Dimension(200,20));
			break;	
		case STATUS:
			setMinimumSize(new Dimension(400,20));
			setPreferredSize(new Dimension(600,20));
			setMaximumSize(new Dimension(600,20));
			break;	
		default:
			break;
		}
		invalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(new Font("Arial",Font.PLAIN,10));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (!hover) {
			if (!active) g2d.setColor(Color.DARK_GRAY.darker()); else  g2d.setColor(Color.GRAY);
		} else {
			if (!active) g2d.setColor(Color.DARK_GRAY.darker()); else  g2d.setColor(hlink.brighter());
		}
		switch (control) {
			case PLAY_STOP:
				Polygon play = new Polygon(); 
				play.addPoint(0, 0); play.addPoint(0, getHeight()-4); play.addPoint(getWidth()-4, (getHeight()-4)/2);
				play.translate(2, 2);
				if (hover && active) g2d.fillRect(2,2,16,16); else g2d.fillPolygon(play);
				break;
			case FULLSCREEN:
				Polygon arrow1 = new Polygon(); arrow1.addPoint(0, 0); arrow1.addPoint(0, 4); arrow1.addPoint(4, 0);
				Polygon arrow2 = new Polygon(); arrow2.addPoint(0, 0); arrow2.addPoint(4, 0); arrow2.addPoint(4, 4);
				Polygon arrow3 = new Polygon(); arrow3.addPoint(0, 0); arrow3.addPoint(5, 0); arrow3.addPoint(0, -5);
				Polygon arrow4 = new Polygon(); arrow4.addPoint(0, 0); arrow4.addPoint(5, 0); arrow4.addPoint(5, -5);
				arrow1.translate(4, 4); arrow2.translate(13,4); arrow3.translate(4, 17); arrow4.translate(12, 17);

				g2d.drawRect(1, 1, this.getWidth()-2, this.getHeight()-2);
				g2d.fillPolygon(arrow1); g.fillPolygon(arrow2);
				g2d.fillPolygon(arrow3); g.fillPolygon(arrow4);
				break;
			case VOLUME:
				g2d.fillRect(2, 2, Integer.valueOf(this.value.toString())-4, this.getHeight()-2);
				g2d.drawRect(1, 1, this.getWidth()-2, this.getHeight()-2);
				break;
			case STATUS:
				g2d.fillRect(1, 1, this.getWidth()-2, this.getHeight()-2);
				g2d.setColor(Color.LIGHT_GRAY);
				if (value != null) g2d.drawString(value.toString(), 4, this.getHeight()-4);
				break;
			default:
				break;
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		repaint();
	}
		
	
}
