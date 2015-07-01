package org.gdesign.twitch.player.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;

public class EmbeddedPlayerControlView extends JPanel {
	private static final long serialVersionUID = -6544459734640671955L;

	public static enum Control {
		PLAY_STOP, FULLSCREEN, VOLUME, STATUS, QUALITY
	}

	private Control control;
	private boolean hover;
	private boolean active;
	private Object value;
	private boolean valueChanged = false;
	private Color dynColor = Color.WHITE;
	private Thread thread = null;

	public EmbeddedPlayerControlView(Control c, boolean active) {
		setDoubleBuffered(true);
		setBackground(Color.DARK_GRAY);
		setControlType(c);
		this.hover = false;
		this.active = active;
	}

	public EmbeddedPlayerControlView(Control c) {
		this(c, false);
	}

	public void setHover(boolean hover) {
		this.hover = hover;
		setCursor(Cursor
				.getPredefinedCursor((hover && active ? Cursor.HAND_CURSOR
						: Cursor.DEFAULT_CURSOR)));
		repaint();
	}

	public void setActive(boolean running) {
		this.active = running;
		repaint();
	}

	public Control getControlType() {
		return control;
	}

	public void setControlType(Control c) {
		this.control = c;
		this.setName(control.toString());
		switch (c) {
		case FULLSCREEN:
		case PLAY_STOP:
			setMinimumSize(new Dimension(20, 20));
			setPreferredSize(new Dimension(20, 20));
			setMaximumSize(new Dimension(20, 20));
			break;
		case VOLUME:
			setMinimumSize(new Dimension(200, 20));
			setPreferredSize(new Dimension(200, 20));
			setMaximumSize(new Dimension(200, 20));
			break;
		case STATUS:
			setMinimumSize(new Dimension(400, 20));
			setPreferredSize(new Dimension(600, 20));
			setMaximumSize(new Dimension(600, 20));
			break;
		case QUALITY:
			setMinimumSize(new Dimension(20, 20));
			setPreferredSize(new Dimension(20, 20));
			setMaximumSize(new Dimension(20, 20));
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
		g2d.setFont(new Font("Arial", Font.PLAIN, 10));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		switch (control) {
		case PLAY_STOP:
			Polygon play = new Polygon();
			play.addPoint(0, 0);
			play.addPoint(0, getHeight() - 6);
			play.addPoint(getWidth() - 6, (getHeight() - 6) / 2);
			play.translate(4, 3);
			g2d.fillRect(1, 1, this.getWidth(), this.getHeight() - 2);
			if (hover && active) {
				g2d.setColor(Color.WHITE.darker());
				g2d.fillRect(4, 4, 12, 12);
			} else {
				if (active)
					g2d.setColor(Color.WHITE.darker());
				else
					g2d.setColor(Color.DARK_GRAY);
				g2d.fillPolygon(play);
			}
			break;
		case FULLSCREEN:
			Polygon arrow1 = new Polygon();
			arrow1.addPoint(0, 0);
			arrow1.addPoint(0, 4);
			arrow1.addPoint(4, 0);
			Polygon arrow2 = new Polygon();
			arrow2.addPoint(0, 0);
			arrow2.addPoint(4, 0);
			arrow2.addPoint(4, 4);
			Polygon arrow3 = new Polygon();
			arrow3.addPoint(0, 0);
			arrow3.addPoint(4, 0);
			arrow3.addPoint(0, -4);
			Polygon arrow4 = new Polygon();
			arrow4.addPoint(0, 0);
			arrow4.addPoint(4, 0);
			arrow4.addPoint(4, -4);
			arrow1.translate(4, 4);
			arrow2.translate(13, 4);
			arrow3.translate(4, 16);
			arrow4.translate(13, 16);
			g2d.drawRect(1, 1, this.getWidth() - 2, this.getHeight() - 3);
			if (hover && active)
				g2d.setColor(Color.WHITE.darker());
			g2d.fillPolygon(arrow1);
			g.fillPolygon(arrow2);
			g2d.fillPolygon(arrow3);
			g.fillPolygon(arrow4);
			break;
		case VOLUME:
			Polygon speaker = new Polygon();
			speaker.addPoint(3, 8);
			speaker.addPoint(3, 12);
			speaker.addPoint(6, 12);
			speaker.addPoint(12, 15);
			speaker.addPoint(12, 3);
			speaker.addPoint(6, 8);
			g2d.drawRect(0, 1, this.getWidth() - 1, this.getHeight() - 3);
			g2d.fillRect(1, 2, Integer.valueOf(this.value.toString()) - 3,
					this.getHeight() - 4);
			if (valueChanged) {
				g.setColor(dynColor);
				g.drawString(value.toString(), 16, this.getHeight() - 6);
				speaker.translate(0, 1);
				g.fillPolygon(speaker);
			}
			if (hover && active) {
				int mX = MouseInfo.getPointerInfo().getLocation().x;
				g2d.setColor(Color.WHITE.darker());
				g2d.drawLine(mX - this.getLocationOnScreen().x, 2,
						mX - this.getLocationOnScreen().x, 18);
				g2d.setColor(Color.DARK_GRAY.darker());
			}
			break;
		case STATUS:
			g2d.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
			g2d.setColor(Color.LIGHT_GRAY.darker());
			if (value != null)
				g2d.drawString(value.toString(), 4, this.getHeight() - 6);
			break;
		case QUALITY:
			if (hover && active)
				g2d.setColor(Color.WHITE.darker());
			g2d.fillOval(this.getWidth() - 18, 2, 16, 16);
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillOval(this.getWidth() - 13, 7, 6, 6);
			break;
		default:
			break;
		}
		g2d.setColor(Color.DARK_GRAY.darker());
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		this.valueChanged = true;
		this.dynColor = new Color(1f, 1f, 1f, 1f);
		if (thread == null) {
			thread = new Thread(new Runnable() {
				long time = 2000;
				long step = 10;

				@Override
				public void run() {
					try {
						int oldAlpha = 255;
						while (dynColor.getAlpha() >= 1) {
							if (dynColor.getAlpha() > oldAlpha)
								time = 2000;
							dynColor = new Color(1f, 1f, 1f,
									(float) time / 2000);
							oldAlpha = dynColor.getAlpha();
							time -= step;
							repaint();
							Thread.sleep(step);
						}
					} catch (InterruptedException e) {
						LogManager.getLogger().trace(e);
					}
					valueChanged = false;
					thread = null;
				}
			});
			thread.start();
		}
		repaint();
	}

}
