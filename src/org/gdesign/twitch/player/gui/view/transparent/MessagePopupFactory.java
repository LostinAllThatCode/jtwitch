package org.gdesign.twitch.player.gui.view.transparent;

import java.awt.Color;

import javax.swing.JFrame;

public class MessagePopupFactory {
	
	public static enum MessageType { INFO, ERROR, WARN }
	private static MessagePopupFactory factory;
	private static JFrame frame;
	
	private MessagePopupFactory() {}
	
	public static void initialize(JFrame frame){
		MessagePopupFactory.frame = frame;
	}
	
	public static MessagePopupFactory getFactory(){
		if (factory == null && frame != null) {
			MessagePopupFactory.factory = new MessagePopupFactory();
		}
		return factory;
	}
	
	public void createMessagePopup(MessageType type, String message, boolean timer) {
		switch (type) {
			case ERROR:
				new MessagePopup(frame, message, Color.RED, timer);
				break;
			case INFO:
				new MessagePopup(frame, message, Color.WHITE, timer);
				break;
			case WARN:
				new MessagePopup(frame, message, Color.ORANGE, timer);
				break;
			default:
				new MessagePopup(frame, message, Color.WHITE, timer);
				break;
		}
	}

}
