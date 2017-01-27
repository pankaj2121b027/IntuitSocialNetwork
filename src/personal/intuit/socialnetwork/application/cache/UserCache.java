package personal.intuit.socialnetwork.application.cache;


import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import personal.intuit.socialnetwork.application.model.User;
import personal.intuit.socialnetwork.application.storage.DBInterface;

/**
 * Cache for Users
 * @author pc2856
 *
 */
public class UserCache {
	
	private static UserCache singletonObj;
	
	private DBInterface db;
	private ConcurrentHashMap<String,User> cache = null;
	
	public static UserCache getInstance(DBInterface dbInterface) throws SQLException{
		if(singletonObj == null){
			singletonObj = new UserCache(dbInterface);
		}
		return singletonObj;
	}
	
	public boolean isEmpty(){
		if(cache == null || cache.size() == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<User> getRandomUsers(int count){
		List<User> users = new LinkedList<User>();
		
		Iterator<User> iterator = cache.values().iterator();
		while(count > 0 && iterator.hasNext()){
			users.add(iterator.next());
		}
		return users;
	}
	
	public void add(User user){
		this.cache.put(user.getId(), user);
	}
	
	public User getUser(String userId){
		return this.cache.get(userId);
	}
	
	private UserCache(DBInterface db) throws SQLException{
		initCache(db);
		loadCache();
	}
	
	private void initCache(DBInterface db) throws SQLException{
		cache = new ConcurrentHashMap<>();
		this.db = db;
	}
	
	private void loadCache() throws SQLException{
		System.out.println("Loading Users from DB");
		List<User> users = db.getUsers();
		for(User user : users){
			cache.put(user.getId(), user);
		}
		
		System.out.println("Loading followers from DB");
		
		Map<String, List<String>> followerMap = db.getFollowersMap(); ///followerid -> followtoid 
		for(Entry<String, List<String>> entry: followerMap.entrySet()){
			
			for(String followToId : entry.getValue()){
				this.cache.get(entry.getKey()).follow(followToId);
			}
			
		}
		
		System.out.println("Loaded " + cache.size() +" Users to cache");
	}
	
	public void close(){
		cache.clear();
	}
	
}
