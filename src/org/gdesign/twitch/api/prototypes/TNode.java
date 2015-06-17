package org.gdesign.twitch.api.prototypes;

import java.util.HashMap;

import org.json.simple.JSONObject;

public abstract class TNode {
	
	protected HashMap<String, Object> values;
	protected JSONObject self;
	
	public TNode(JSONObject obj) {
		this.self = obj;
		this.values = new HashMap<String, Object>();
		decode();
	}

	protected abstract void decode();
	
	
	protected void setValue(String s, JSONObject o){
		values.put(s, o.get(s));
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
}
