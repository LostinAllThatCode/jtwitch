package org.gdesign.twitch.layout;


import java.awt.Color;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;

public class LayoutTests {

	@org.junit.Test
	public void test() {
		LogManager.getLogger().debug(new Gson().toJson(new Color(255,255,255,127)));
	}

}
