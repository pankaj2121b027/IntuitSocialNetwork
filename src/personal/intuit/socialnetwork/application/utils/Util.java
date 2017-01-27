package personal.intuit.socialnetwork.application.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import personal.intuit.socialnetwork.application.JsonConvertible;

public class Util {

	public static int getRandom(int Min, int Max){
		return (Min + (int)(Math.random() * ((Max - Min) + 1)));
	}
	
	public static String getJsonString(List<JsonConvertible> objects){
		
		try {

			JSONArray usersArray = new JSONArray();
			
			for(JsonConvertible object : objects){
				usersArray.put(object.toJsonObject());
			}
			
			return usersArray.toString();
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}