package org.gdesign.twitch.player.gui;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.json.simple.parser.ParseException;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class JTwitch{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","true");
		System.setProperty("swing.aatext", "true");
		
        if (checkDependencies()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                	try {
						new JTwitch();
					} catch (ParseException e) {
						LogManager.getLogger().error(e);
						e.printStackTrace();
					}
                }
            });
        } else {
        	LogManager.getLogger().error("Can't find vlc/livestreamer natives. Please configure livestreamer.properties.");
        	JOptionPane.showMessageDialog(null, "Can't find vlc/livestreamer natives. Please configure livestreamer.properties.\n"
        			+ "1) You need VideoLAN Player installed on your system. For Java(64Bit) you need VLC(64Bit) and vice versa.\n"
        			+ "2) You need Livestreamer. See details in livestreamer.properties to set it up correctly.",
        			"Libvlc/Livestreamer natives discovery", JOptionPane.OK_CANCEL_OPTION);
        }
	}
	
	public JTwitch() throws ParseException {
		JFrame frame = new JFrame("JTwitch Player (0.2.0)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 1280, 640);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		MainView mainView 	= new MainView(frame);
		MainModel mainModel	= new MainModel();
		MainController controller = new MainController(mainView, mainModel);
	
		LogManager.getLogger().debug(new TwitchAPI().authorized());
		
		controller.update(15000);
	}
	
	public static boolean checkDependencies(){
		if (new NativeDiscovery().discover() && LivestreamerFactory.discover()){
			return true;
		}
		return false;
	}
}
