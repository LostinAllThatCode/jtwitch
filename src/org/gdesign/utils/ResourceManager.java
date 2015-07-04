package org.gdesign.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.livestreamer.config.LivestreamerConfiguration;

import uk.co.caprica.vlcj.version.Version;

import com.google.gson.Gson;

public class ResourceManager {

	private static boolean init;
	private static HashMap<String, Font> fonts;
	
	public static boolean initialize(){
		if (!init) {
			try {
				fonts = new HashMap<String, Font>();	
				
				createFont("gnuolane.ttf");
				
				prepareLocalResourceLocation();
				
				init = true;
			} catch (Exception e) {
				LogManager.getLogger().error(e);
				e.printStackTrace();
			}
		}
		return init;
	}
	
	public static void prepareLocalResourceLocation() throws Exception{
		if (isRunningFromJar()) {
			File resources = new File( "./resources" );
			if (!resources.exists()) resources.mkdir();
	
			File configs = new File( "./resources/configs/" );
			if (!configs.exists()) configs.mkdir();
			
			File channel_logos = new File( "./resources/channel_logos/" );
			if (!channel_logos.exists()) channel_logos.mkdir();	
			
			File layouts = new File( "./resources/layouts/" );
			if (!layouts.exists()) layouts.mkdir();	
			
			File logs = new File( "./resources/logs/" );
			if (!logs.exists()) logs.mkdir();	
			
			for (String resource : getResourceListing()){
				if (resource.contains("layout") && resource.endsWith(".json")) {
					File file = new File("./resources/layouts/"+resource);
					if (!file.exists()){
						JarExport.ExportResource("/"+resource, "/resources/layouts");
					} else {
						Reader reader = new InputStreamReader(new FileInputStream(file));
						Version current = new Version(ResourceManager.class.getPackage().getImplementationVersion());
						GsonDataSet resClass = new Gson().fromJson(reader, GsonDataSet.class);
						if (new Version(resClass.version).compareTo(current) == -1) {
							JarExport.ExportResource("/"+resource, "/resources/layouts");
						}
					}
				} else if (resource.contains("config") && resource.endsWith(".json")){
					File file = new File("./resources/configs/"+resource);
					if (!file.exists()){
						JarExport.ExportResource("/"+resource, "/resources/configs");
					} else {
						Reader reader = new InputStreamReader(new FileInputStream(file));
						Version current = new Version(ResourceManager.class.getPackage().getImplementationVersion());
						GsonDataSet resClass = new Gson().fromJson(reader, GsonDataSet.class);
						if (new Version(resClass.version).compareTo(current) == -1) {
							JarExport.ExportResource("/"+resource, "/resources/configs");
						}
					}						
				}
			}
		}
	}
	
	public static PlayerConfiguration getPlayerConfiguration(){
		if (isRunningFromJar()) {
			try {
				File cfg = new File("./resources/configs/jtwitch.config.json");
				if (cfg.exists()) {
					Reader reader = new InputStreamReader(new FileInputStream(cfg));
					return new Gson().fromJson(reader, PlayerConfiguration.class);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream("jtwitch.config.json"));
			return new Gson().fromJson(reader, PlayerConfiguration.class);
		}
		return null;
	}
	
	public static void setPlayerConfiguration(PlayerConfiguration cfg){
		try {
			LogManager.getLogger().debug(cfg);
			File cfgFile = new File("./resources/configs/jtwitch.config.json");
	        if (cfgFile.exists()) {
	        	LogManager.getLogger().debug(cfg);
	            FileOutputStream fOut = new FileOutputStream(cfgFile);
	            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
	         	myOutWriter.append(new Gson().toJson(cfg));
	            myOutWriter.close();
	            fOut.close();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogManager.getLogger().debug(cfg);
	}
	
	public static LivestreamerConfiguration getLivestreamerConfiguration(){
		if (isRunningFromJar()) {
			try {
			File cfg = new File("./resources/configs/livestreamer.config.json");
			if (cfg.exists()) {
				Reader reader = new InputStreamReader(new FileInputStream(cfg));
				return new Gson().fromJson(reader, LivestreamerConfiguration.class);
			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream("livestreamer.config.json"));
			return new Gson().fromJson(reader, LivestreamerConfiguration.class);
		}
		return null;
	}
	
	public static Reader getLayout(String name){
		if (isRunningFromJar()) {
			try {
			File layout = new File("./resources/layouts/"+name);
			if (layout.exists()) return new InputStreamReader(new FileInputStream(layout));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			InputStream in = ClassLoader.getSystemResourceAsStream(name);
			return new InputStreamReader(in);
		}
		return null;
	}

	public static Font getCustomFont(String fontName) {
		return fonts.get(fontName);
	}

	public static BufferedImage getLogo(URL url) throws IOException{
		if (!isRunningFromJar()) return ImageIO.read(url);
		String channelLogos = "./resources/channel_logos/";
		String trimmedURL = url.toString().substring(46, url.toString().length());
		File logo = new File(channelLogos+trimmedURL);
		if (logo.exists()) {
			return ImageIO.read(logo);
		} else {
			downloadImage(url.toString(), channelLogos);
			return ImageIO.read(logo);
			
		}
	}
	
	private static void createFont(String resource) {
		try {
			// create the font to use. Specify the size!
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader
					.getSystemResource(resource).openStream());
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader
					.getSystemResource(resource).openStream()));

			fonts.put(resource, customFont);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	
	private static synchronized void downloadImage(String sourceUrl, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
		URL imageUrl = new URL(sourceUrl);
		InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
        OutputStream imageWriter = new BufferedOutputStream(new FileOutputStream(targetDirectory + File.separator + FilenameUtils.getName(sourceUrl)));
        int readByte;

        while ((readByte = imageReader.read()) != -1)
        {
            imageWriter.write(readByte);
        }
        imageReader.close();
        imageWriter.close();

    }
	
	private static boolean isRunningFromJar() {
		String className = ResourceManager.class.getName().replace('.', '/');
		String classJar = ResourceManager.class.getResource(
				"/" + className + ".class").toString();
		return classJar.startsWith("jar:");
	}

	private static String[] getResourceListing() throws IOException {	
		CodeSource src = ResourceManager.class.getProtectionDomain().getCodeSource();
		List<String> list = new ArrayList<String>();

		if( src != null ) {
		    URL jar = src.getLocation();
		    ZipInputStream zip = new ZipInputStream( jar.openStream());
		    ZipEntry ze = null;

		    while( ( ze = zip.getNextEntry() ) != null ) {
		        String entryName = ze.getName();
		        list.add( entryName  );
		    }

		 }
		return list.toArray( new String[ list.size() ] );
	}
}
