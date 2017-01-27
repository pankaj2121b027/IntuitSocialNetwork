package personal.intuit.socialnetwork.application;

/**
 * Configurations
 * @author pc2856
 *
 */
public class Constants {
	
	public static final String PostsTable = "posts";
	public static final String PostsTable_id = "id";
	public static final String PostsTable_ownerId = "ownerid";
	public static final String PostsTable_message = "message";
	public static final String PostsTable_creationTime= "creationtime";
	
	public static final String UsersTable = "users";
	public static final String UsersTable_id = "id";
	public static final String UsersTable_userName = "userName";
	
	public static final String FollowersTable = "followers";
	public static final String FollowersTable_followerId = "followerId";
	public static final String FollowersTable_followtoId = "followtoId";

	public static final int MAX_POST_TO_LOAD_EACH_USER = 30; //max # of posts to load from DB to each user's own post field
	public static final int MAX_POST_DB = 100000; //max # of mock posts to insert to DB
	public static final int MAX_USERS   = 10000; //max # of users to insert in DB
	public static final int MAX_FOLLOWERS_PER_USER = 10;
	public static final int MAX_POSTS_PER_USER = 20;
	
}
