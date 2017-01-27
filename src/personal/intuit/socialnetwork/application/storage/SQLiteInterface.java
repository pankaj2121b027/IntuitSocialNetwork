package personal.intuit.socialnetwork.application.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sqlite.JDBC;

import personal.intuit.socialnetwork.application.Constants;
import personal.intuit.socialnetwork.application.model.Post;
import personal.intuit.socialnetwork.application.model.User;
import personal.intuit.socialnetwork.application.utils.Util;

public class SQLiteInterface implements DBInterface {

	private Connection connection;
	
	public SQLiteInterface(){
		try{
			
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			
			if(connection == null){
				throw new RuntimeException("Could not initialize sqlite connection");
			}
			
			
			init();
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public List<Post> getRecentPosts(int count) throws SQLException {

		List<Post> posts = new LinkedList<>();
		
		try(Statement statement = connection.createStatement()){
			statement.setQueryTimeout(60);

			String postQuery = "select * from " + Constants.PostsTable + " order by " + Constants.PostsTable_ownerId + "," + Constants.PostsTable_creationTime + " desc ";
			System.out.println(postQuery);
			ResultSet rs = statement.executeQuery(postQuery);
			
			String prevId = "";
			int countPost = 1;
			while(rs.next())
			{
				
				if(prevId.compareTo(rs.getString(Constants.PostsTable_ownerId)) == 0){
					countPost++;
				}
				else{
					countPost = 1;
				}
				
				prevId = rs.getString(Constants.PostsTable_ownerId);
				
				if(countPost <= count){
					Post post = new Post(rs.getString(Constants.PostsTable_id), rs.getString(Constants.PostsTable_ownerId), rs.getString(Constants.PostsTable_message), rs.getLong(Constants.PostsTable_creationTime));
					
					posts.add(post);
				}
			}
		}
		

		return posts;
	}

	
	public void init() throws SQLException {
		List<String> userIds = createUsers();
		createPosts(userIds);
		createFollowers(userIds);
	}
	
	@Override
	public void createFollowers(List<String> userIds) throws SQLException{
		
		System.out.println("Creating Followers");
		int followersCratedCount=0;
		String createTableQuery = "create table " + Constants.FollowersTable +" (" + Constants.FollowersTable_followerId + " string, " + Constants.FollowersTable_followtoId + " string )";
		
		try(Statement statement = connection.createStatement()){
			statement.executeUpdate(createTableQuery);
			
			
			for(String followerId : userIds){
				
				int followToCount = Util.getRandom(1, Constants.MAX_FOLLOWERS_PER_USER);
				
				for(int counter =0;counter < followToCount;counter++){
					
					int index = Util.getRandom(0, userIds.size()-1);
					
					String followToId = userIds.get(index);
					if(followToId.compareTo(followerId) == 0){
						index++;
						index =  (userIds.size() - 1) % index;
						followToId = userIds.get(index);
					}
					
					
					String followQuery = "insert into " + Constants.FollowersTable +" values ('" + followerId +"','" + followToId +"')";
					statement.executeUpdate(followQuery);
					followersCratedCount++;
				}
				
			}
			
		}
		System.out.println("Inserted " + followersCratedCount +" followers to DB");
	}

	@Override
	public List<String> createUsers() throws SQLException{
		List<String> userIds = new LinkedList<>();
		
		int createUserCount=0;
		
		if(connection == null)		
			System.out.println(connection.toString());
		else
			System.out.println(connection.toString());
		
		System.out.println("Creating Users");
		//create tables
		String createTableQuery = "create table " + Constants.UsersTable +" (" + Constants.UsersTable_id + " string, " + Constants.UsersTable_userName + " string )";
		try(Statement statement = connection.createStatement()){
			statement.executeUpdate(createTableQuery);
			
			int count = Constants.MAX_USERS;
			while(count > 0){
				String userId = User.getNextId();
				
				String insertUser = "insert into " + Constants.UsersTable + " values ('" + userId + "','"+ "name" + "')";
				statement.executeUpdate(insertUser);
				count--;
				createUserCount++;
				userIds.add(userId);
			}
		}
		System.out.println("Inserted " + createUserCount +" Users to DB");
		return userIds;
	}
	
	@Override
	public void createPosts(List<String> userIds) throws SQLException{
		System.out.println("Creating Posts");
		
		//create tables
		String createTableQuery = "create table " + Constants.PostsTable +" (" + Constants.PostsTable_id + " string, " + Constants.PostsTable_ownerId + " string , " + Constants.PostsTable_message + " string, " + Constants.PostsTable_creationTime + " long)";

		int createdPostsCount = 0;
		
		try(Statement statement = connection.createStatement()){
			statement.executeUpdate(createTableQuery);
			int maxPosts = Constants.MAX_POST_DB;
			while(maxPosts > 0){
				
				int postsCount = Util.getRandom(1, Constants.MAX_POSTS_PER_USER);
				
				
				int index = Util.getRandom(0, userIds.size()-1);
				
				String userId =  userIds.get(index);
				
				for(int i=0;i<postsCount && maxPosts > 0;i++){
					
					String postid = Post.getNextId();
					
					String insertPosts = "insert into " + Constants.PostsTable + " values ('" + postid + "','" + userId + "','asda'," + Util.getRandom(1, 10) + ")";
					statement.executeUpdate(insertPosts);
					maxPosts--;
					createdPostsCount++;
					
				}
			}
			
		}
		System.out.println("Inserted " + createdPostsCount + " Posts to DB");
		
	}
	
	
	@Override
	public List<User> getUsers() throws SQLException {
		List<User> users = new LinkedList<>();
		
		try(Statement statement = connection.createStatement()){
			statement.setQueryTimeout(60);

			String postQuery = "select * from " + Constants.UsersTable ; // + "," + Constants.FollowersTable + " where " + Constants.UsersTable_id +" = " + Constants.FollowersTable_followerId;
			System.out.println(postQuery);
			ResultSet rs = statement.executeQuery(postQuery);
			
			while(rs.next()){
				User user = new User(rs.getString(Constants.UsersTable_id), rs.getString(Constants.UsersTable_userName));
				users.add(user);
			}
		}
		
		System.out.println("Read " + users.size() +" Users from DB");
		
		return users;
	}
	
	@Override
	public Map<String, List<String>> getFollowersMap() throws SQLException {
		
		Map<String, List<String>> followerMap = new HashMap<String, List<String>>();
		try(Statement statement = connection.createStatement()){
			String followerQuery = "select * from " + Constants.FollowersTable ;
			System.out.println(followerQuery);
			ResultSet rs = statement.executeQuery(followerQuery);
			
			while(rs.next()){
				if(followerMap.get(rs.getString(Constants.FollowersTable_followerId)) == null){
					followerMap.put(rs.getString(Constants.FollowersTable_followerId), new LinkedList<String>());
				}
				
				followerMap.get(rs.getString(Constants.FollowersTable_followerId)).add(rs.getString(Constants.FollowersTable_followtoId));
			}
		}
		
		System.out.println("Read Followers mapping for " + followerMap.size() +" users");
		
		return followerMap;
	}

	@Override
	protected void finalize() throws Throwable {
		try{
			if(this.connection != null)
				this.connection.close();
		}catch(Throwable t){
			throw t;
		}finally{
			super.finalize();
		}

	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

