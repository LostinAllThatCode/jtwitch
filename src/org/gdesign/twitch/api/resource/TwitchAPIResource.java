package org.gdesign.twitch.api.resource;

import java.lang.reflect.Field;

/**
 * TwitchAPI base resource
 * 
 * @author agaida
 * 
 */
public abstract class TwitchAPIResource {

	public String jsonData;
	
	public <T extends TwitchAPIResource> T setJsonData(String data, Class<T> clazz){
		this.jsonData = data;
		return clazz.cast(this);
	}
	
	@Override
	public String toString() {
		String s = this.getClass().getSimpleName() + "[";
		for (Field f : getClass().getFields())
			s += f.getName() + ",";
		return (s + "]").replace(",]", "]");
	}
}
