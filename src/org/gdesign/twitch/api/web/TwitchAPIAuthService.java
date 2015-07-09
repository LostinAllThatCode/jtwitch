package org.gdesign.twitch.api.web;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.handler.StaticFileHandler;

public class TwitchAPIAuthService extends Thread {
	private int timeout = 120000;
	private Permission[] permissons;
	private String token;	
	private String request = "https://api.twitch.tv/kraken/oauth2/authorize"+
		    "?response_type=token" + "&client_id="+TwitchAPI.CLIENT_ID+
		    "&redirect_uri="+TwitchAPI.REDIRECT_URI+"&scope=";

	public TwitchAPIAuthService(Permission... permissions) {
		this.permissons = permissions;
		this.request += TwitchAPI.parsePermissonToURLString(permissions);
		this.token = "";
	}
	
	@Override
	public void run() {
		WebServer service = WebServers.createWebServer(8080);
		service.add(new StaticFileHandler("/static-files"));
		service.add("/websocket-echo", new BaseWebSocketHandler() {
	    	@Override
	    	public void onOpen(WebSocketConnection connection) throws Exception {
	    		super.onOpen(connection);
	    	}
	    	@Override
	    	public void onMessage(WebSocketConnection connection, String msg)
	    			throws Throwable {
	    		super.onMessage(connection, msg);
	    		try {
	    			LogManager.getLogger().debug(msg);
	    			TwitchAPI.setAuthToken(msg);
		    		if (TwitchAPI.hasPermisson(permissons)) {
		    			token = msg;
		    			connection.send("OK. You can close this browser now.");
		    		}
	    		} catch (TwitchAPIAuthTokenInvalidException | TwitchAPINoTokenSpecifiedException | TwitchAPINoPermissionException e){
	    			connection.send("Failed. Can't authorize with given token. "+ e);
	    			throw e;
	    		}
	    	}
		});
		service.start();
		
		Desktop desktop = Desktop.getDesktop();
		if (desktop != null) {
			try {
				desktop.browse(new URI(request));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public String getToken(){
		return this.token;
	}
	
	public boolean isAuthorized() throws InterruptedException, IOException{
		this.start();
		long time=0;
		while (token.length() == 0) {
			if (time >= timeout) throw new IOException("Authorization failed. Timeout after "+timeout / 1000 +" seconds.");
			Thread.sleep(250);
			time+=250;
		}
		return true;
	} 
	
	

}
