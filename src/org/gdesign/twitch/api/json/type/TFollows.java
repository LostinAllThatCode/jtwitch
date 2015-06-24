package org.gdesign.twitch.api.json.type;

import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class TFollows extends TNode{
	
	public TFollows(JSONObject obj) {
		super(obj);
	}
	
	public Collection<TChannel> getChannels(){
		Collection<TChannel> channels = new ArrayList<TChannel>(); 
		for (Object channel : values.values()){
			channels.add((TChannel) channel);
		}
		return channels;
	}
	
	public TChannel getChannel(Object channel){
		return (TChannel) values.get(channel.toString().toLowerCase());
	}
	
	
	@Override
	protected void decode() {
		JSONArray follows = (JSONArray) self.get("follows");
		for (int i=0; i<follows.size();i++){
			TChannel c = new TChannel((JSONObject) follows.get(i));
			values.put(c.values.get("name").toString().toLowerCase(), c);	
		}
	}


}
