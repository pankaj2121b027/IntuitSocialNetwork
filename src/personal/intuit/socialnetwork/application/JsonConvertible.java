package personal.intuit.socialnetwork.application;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonConvertible {
	
	public JSONObject toJsonObject() throws JSONException;
}
