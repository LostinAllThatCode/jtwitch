package org.gdesign.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JLabel;

public class FontResource {
	
	private static HashMap<String, Font> fonts;
	
	static {
		fonts = new HashMap<>();
		createFont("gnuolane.ttf");
	}
	
	public static Font getFont(String fontName){
		return fonts.get(fontName);
	}
	
	private static void createFont(String resource){
		 try {
             //create the font to use. Specify the size!
			 Font customFont = Font.createFont(Font.TRUETYPE_FONT,ClassLoader.getSystemResource(resource).openStream());
             GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
             //register the font
             ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,ClassLoader.getSystemResource(resource).openStream()));
             
             fonts.put(resource, customFont);
         } catch (IOException e) {
             e.printStackTrace();
         }
         catch(FontFormatException e)
         {
             e.printStackTrace();
         }
	}
	
	public static void fixFontSize(JLabel label){
		Font labelFont = label.getFont();
		String labelText = label.getText();

		int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		label.setFont(label.getFont().deriveFont(fontSizeToUse));
	}
}
