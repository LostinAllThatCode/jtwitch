package org.gdesign.twitch.player.gui.view.layout.utils;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class LayoutUtils {

	public static void fixFontSize(JLabel label, String text) {
		label.setText(text);

		Font labelFont = label.getFont();
		String labelText = label.getText();

		int stringWidth = label.getFontMetrics(labelFont)
				.stringWidth(labelText);
		int componentWidth = label.getWidth();

		double widthRatio = (double) componentWidth / (double) stringWidth;

		int newFontSize = (int) (labelFont.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		float fontSizeToUse = Math.min(newFontSize, componentHeight);

		label.setFont(label.getFont().deriveFont((int)fontSizeToUse));
		label.setFont(label.getFont().deriveFont(fontSizeToUse));
	}
	
	public static Color changeAlpha(Color c, float alpha){
		float r,g,b;
		r = c.getRed()/255; g = c.getGreen()/255; b = c.getBlue()/255;
		return new Color(r,g,b,alpha);
	}
}
