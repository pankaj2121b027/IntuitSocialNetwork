package personal.intuit.socialnetwork.application.cache;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import personal.intuit.socialnetwork.application.Application;
import personal.intuit.socialnetwork.application.Constants;
import personal.intuit.socialnetwork.application.model.Post;
import personal.intuit.socialnetwork.application.storage.DBInterface;

/**
 * Cache for Posts
 * @author pc2856
 *
 */
public class PostsCache {
	private static PostsCache singletonObj;
	
	private DBInterface db;
	private ConcurrentHashMap<String,Post> cache = null;
	
	public static PostsCache getInstance(DBInterface dbInterface) throws SQLException{
		if(singletonObj == null){
			singletonObj = new PostsCache(dbInterface);
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
	
	public void add(Post post){
		this.cache.put(post.getId(), post);
	}
	
	public Post getPost(String postId){
		return this.cache.get(postId);
	}
	
	private PostsCache(DBInterface db) throws SQLException{
		initCache(db);
		loadCache();
	}
	
	private void initCache(DBInterface db) throws SQLException{
		cache = new ConcurrentHashMap<>(); 
		this.db = db;
	}
	
	private void loadCache() throws SQLException{
		System.out.println("Loading Posts from DB");
		List<Post> posts = db.getRecentPosts(Constants.MAX_POST_TO_LOAD_EACH_USER);
		for(Post post : posts){
			cache.put(post.getId(), post);
			
			Application.getUserCache().getUser(post.getOwnerId()).addOwnPost(post);
		}
		
		System.out.println("Loaded " + cache.size() +" Posts to cache");
	}
	
	public void close(){
		cache.clear();
	}
	
}
