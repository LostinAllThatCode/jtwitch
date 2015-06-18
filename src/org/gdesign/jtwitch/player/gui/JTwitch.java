package org.gdesign.jtwitch.player.gui;

import java.util.Observable;
import java.util.Properties;

import javax.swing.*;
import org.apache.logging.log4j.LogManager;

import org.gdesign.jtwitch.player.gui.controller.MainController;
import org.gdesign.jtwitch.player.gui.model.MainModel;
import org.gdesign.jtwitch.player.gui.view.MainView;
import org.gdesign.utils.Configuration;


import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class JTwitch extends Observable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","true");
		System.setProperty("swing.aatext", "true");
		
		final Properties config = new Configuration("jtwitch.properties");
		
        boolean found = new NativeDiscovery().discover();
        if (found) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	new MainController(new MainView(), new MainModel(config.getProperty("username")));
                }
            });
        } else {
        	//NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), config.getProperty("videolan"));
        	LogManager.getLogger().error("Can't find vlc natives.");
        }
	}
}
