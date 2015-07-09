package org.gdesign.twitch.player.gui.view.streams;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class StreamListScrollBarView extends BasicScrollBarUI {

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		g.setColor(c.getBackground().darker());
		g.fillRect(0, 0, trackBounds.width, trackBounds.height);
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		g.setColor(c.getBackground());
		g.translate(thumbBounds.x, thumbBounds.y);
		g.fillRect(0, 0, thumbBounds.width, thumbBounds.height);
		g.translate(-thumbBounds.x, -thumbBounds.y);
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		JButton button = new JButton("zero button");
		Dimension zeroDim = new Dimension(0, 0);
		button.setPreferredSize(zeroDim);
		button.setMinimumSize(zeroDim);
		button.setMaximumSize(zeroDim);
		return button;
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		JButton button = new JButton("zero button");
		Dimension zeroDim = new Dimension(0, 0);
		button.setPreferredSize(zeroDim);
		button.setMinimumSize(zeroDim);
		button.setMaximumSize(zeroDim);
		return button;
	}
}
