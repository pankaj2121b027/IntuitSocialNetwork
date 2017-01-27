package personal.intuit.socialnetwork.application.model;


import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import personal.intuit.socialnetwork.application.JsonConvertible;

public class Post implements Comparable<Post>, JsonConvertible{

	private String id;
	private String ownerId;
	private String message;
	private long creationTime;
	
	public String getId() {
		return id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getMessage() {
		return message;
	}

	public long getCreationTime() {
		return creationTime;
	}
	
	public static String getNextId(){
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "postid_" + (new Date()).getTime();
	}
	
	public Post(String id, String ownerId , String message , long creationTime){
		this.ownerId = ownerId;
		this.message = message;
		this.id = id;
		this.creationTime = creationTime;
	}
	
	public JSONObject toJsonObject() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("post_id", this.id);
		jsonObject.put("post_message", this.message);
		jsonObject.put("post_creationTime", this.creationTime);
		jsonObject.put("post_ownerId", this.ownerId);
		
		return jsonObject;
	}

	@Override
	public int compareTo(Post o) {
		return new Long(this.creationTime - o.creationTime).intValue();
	}
 }
