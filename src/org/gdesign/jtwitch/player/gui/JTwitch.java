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
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class JTwitch extends Observable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","true");
		System.setProperty("swing.aatext", "true");
		
		final Properties config = new Configuration("jtwitch.properties");
		
        if (checkVLCj()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	new MainController(new MainView(), new MainModel(config.getProperty("username")));
                }
            });
        } else {
        	LogManager.getLogger().error("Can't find vlc natives.");
        }
	}
	
	
	public static boolean checkVLCj(){
		if (new NativeDiscovery().discover()){
			LogManager.getLogger().debug("libvlcj vlc native path: "+RuntimeUtil.getLibVlcLibraryName());
			return true;
		}
		return false;
	}
}
