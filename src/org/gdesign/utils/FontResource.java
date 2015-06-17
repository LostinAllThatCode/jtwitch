package org.gdesign.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.HashMap;

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
}
