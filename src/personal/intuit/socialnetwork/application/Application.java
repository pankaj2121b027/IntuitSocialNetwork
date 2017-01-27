package personal.intuit.socialnetwork.application;


import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import personal.intuit.socialnetwork.application.cache.PostsCache;
import personal.intuit.socialnetwork.application.cache.UserCache;
import personal.intuit.socialnetwork.application.model.Post;
import personal.intuit.socialnetwork.application.storage.DBInterface;
import personal.intuit.socialnetwork.application.storage.SQLiteInterface;;

/**
 * This class handled all Social Network operations
 * @author pc2856
 *
 */
public class Application {

	private static DBInterface dbInterface;
	private static UserCache userCache;
	private static PostsCache postCache;
	
	public static UserCache getUserCache(){
		return userCache;
	}
	
	public static PostsCache getPostsCache(){
		return postCache;
	}
	
	public static void init() throws SQLException{
		dbInterface = new SQLiteInterface();
		userCache = UserCache.getInstance(dbInterface);
		postCache = PostsCache.getInstance(dbInterface);
	}
	
	/**
	 * get recent 100 posts for user
	 * @param userId
	 * @return
	 */
	public static List<Post> getPosts(String userId){
		
		if(userCache.isEmpty() || postCache.isEmpty()){
			throw new RuntimeException("User cache is empty / application is not initalized");
		}
		
		if(userCache.getUser(userId) == null){
			return new LinkedList<>();
		}
		
		return userCache.getUser(userId).getRecentPosts(100);
	}
	
	public static void close(){
		dbInterface.close();
		userCache.close();
		postCache.close();
	}
}
