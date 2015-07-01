package org.gdesign.twitch.api.resource;

import java.lang.reflect.Field;

/**
 * TwitchAPI base resource
 * @author agaida
 *
 */
public abstract class TwitchAPIResource{
	
	@Override
	public String toString() {
		String s = this.getClass().getSimpleName()+"[";
		for (Field f : getClass().getFields()) s+=f.getName()+",";
		return (s+"]").replace(",]","]");
	}
}
