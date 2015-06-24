package org.gdesign.twitch.api.json.type;

import org.json.simple.JSONObject;

public class TStream extends TNode{
	
	public TStream(JSONObject obj) {
		super(obj);
	}
	
	@Override
	protected void decode() {
		if (self.get("stream") != null) {
			JSONObject data = (JSONObject) self.get("stream");
			for (Object key : data.keySet()){
				setValue(key.toString(), data);
			}
		}
	}
	
	public boolean isOnline(){
		return values.get("viewers") != null;
	}
}
