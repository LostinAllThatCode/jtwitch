package org.gdesign.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.gdesign.twitch.player.gui.JTwitch;

public class JarExport {

	/**
	 * Export a resource embedded into a Jar file to the local file path.
	 * 
	 * @param resourceName
	 *            ie.: "/SmartLibrary.dll"
	 * @return The path to the exported resource
	 * @throws Exception
	 */
	static public String ExportResource(String resourceName) throws Exception {// todo
																				// move
																				// to
																				// Utils
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String jarFolder;
		try {
			stream = JTwitch.class.getResourceAsStream(resourceName);// note
																		// that
																		// each
																		// / is
																		// a
																		// directory
																		// down
																		// in
																		// the
																		// "jar tree"
																		// been
																		// the
																		// jar
																		// the
																		// root
																		// of
																		// the
																		// tree"
			if (stream == null) {
				throw new Exception("Cannot get resource \"" + resourceName
						+ "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			jarFolder = new File(JTwitch.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath())
					.getParentFile().getPath().replace('\\', '/');
			resStreamOut = new FileOutputStream(jarFolder + resourceName);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			stream.close();
			resStreamOut.close();
		}

		return jarFolder + resourceName;
	}
}
