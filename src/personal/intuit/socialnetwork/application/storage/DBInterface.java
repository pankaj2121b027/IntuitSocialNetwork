package personal.intuit.socialnetwork.application.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import personal.intuit.socialnetwork.application.model.Post;
import personal.intuit.socialnetwork.application.model.User;


public interface DBInterface {

	
	public List<Post> getRecentPosts(int count) throws SQLException;
	public List<User> getUsers() throws SQLException;
	public Map<String,List<String>> getFollowersMap() throws SQLException; // followerid -> follow to id
	public void init() throws SQLException;
	public List<String> createUsers() throws SQLException;
	public void createPosts(List<String> userIds) throws SQLException;
	public void createFollowers(List<String> userIds) throws SQLException;
	public void close();
	
}