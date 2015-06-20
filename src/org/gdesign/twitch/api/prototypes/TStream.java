package org.gdesign.twitch.api.prototypes;

import org.json.simple.JSONObject;

public class TStream extends TNode{
	
	public TStream(JSONObject obj) {
		super(obj);
	}
	
	@Override
	protected void decode() {
		JSONObject data = (JSONObject) self.get("stream");
		if (data != null){
			for (Object key : data.keySet()){
				setValue(key.toString(), data);
			}
		} 
	}
	
	public boolean isOnline(){
		return values.get("viewers") != null;
	}
}
