package org.gdesign.twitch.api.prototypes;


import org.json.simple.JSONObject;

public class TChannel extends TNode {

	public TChannel(JSONObject obj) {
		super(obj);
	}

	public String get(String key){
		return values.get(key).toString();
	}
	
	@Override
	protected void decode() {
		JSONObject data = (JSONObject) self.get("channel");
		for (Object key : data.keySet()){
			setValue(key.toString(), data);
		}
	}
}
