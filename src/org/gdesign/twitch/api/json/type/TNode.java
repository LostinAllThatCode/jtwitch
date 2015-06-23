package org.gdesign.twitch.api.json.type;

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
	
	public JSONObject getJSONObject(){
		return self;
	}
	
	public Integer getInt(String key){
		Object obj = values.get(key);
		if (obj == null) return 0; else return Integer.valueOf(obj.toString());
	}

	public String getString(String key){
		Object obj = values.get(key);
		if (obj == null) return ""; else return obj.toString();
	}
	
	@Override
	public String toString() {
		return values.toString();
	}
	
}
