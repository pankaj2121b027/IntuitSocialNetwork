package personal.intuit.socialnetwork.application.model;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import personal.intuit.socialnetwork.application.Application;
import personal.intuit.socialnetwork.application.JsonConvertible;

import java.util.Set;
import java.util.TreeMap;



/*

On an average, every employee will send approximately 10 messages a day to their followers.
hence store every user's last 3 days worth of posts in cache . i.e. store 30 posts in cache

if we want to show latest 100 , we need to check only cache.
if still can not make 100, need to query DB and update cache

*/

public class User implements JsonConvertible{
	private String id;
	private String userName;
	
	private Set<String> peopleImFollowing = new HashSet<>();
	
	private TreeMap<Long, List<String>> myRecentPosts;
	private TreeMap<Long, List<String>> recentPostsImFollowing;
	
	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}
	
	public static String getNextId(){
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "userid_"+ (new Date()).getTime();
	}
	
	public User(){
		this.id = getNextId();
	}
	
	public User(String id,String userName){
		this.id = id;
		this.userName = userName;
	}
	
	public void addOwnPost(Post post){
		
		if(this.myRecentPosts == null){
			this.myRecentPosts = new TreeMap<>();
		}
		
		if(this.myRecentPosts.get(post.getCreationTime()) == null){
			this.myRecentPosts.put(post.getCreationTime(), new LinkedList<String>());
		}
		
		this.myRecentPosts.get(post.getCreationTime()).add( post.getId());
		
	}
	
	
	public List<Post> getRecentPosts(int count){
		
		System.out.println("\nshowing recent posts for " + this.id);
		
		if(this.recentPostsImFollowing == null){
			this.recentPostsImFollowing = new TreeMap<>();
			
			if(this.peopleImFollowing != null){
				Post post = null;
				
				//load posts from cache if empty
				for(String followToId : this.peopleImFollowing){
					if(Application.getUserCache().getUser(followToId).myRecentPosts != null){
						for(List<String> postIds : Application.getUserCache().getUser(followToId).myRecentPosts.values()){
							
							
							
							for(String postId : postIds){
								post = Application.getPostsCache().getPost(postId);
								
								if(!this.recentPostsImFollowing.containsKey(post.getCreationTime())){
									this.recentPostsImFollowing.put(post.getCreationTime(), new LinkedList<String>());
								}
								
								this.recentPostsImFollowing.get(post.getCreationTime()).add(post.getId());
							}
						}
					}
					
				}
			}
		}
		
		
		TreeMap<Long, List<Post>> latestPosts = new TreeMap<Long, List<Post>>();
		
		if(this.myRecentPosts != null){
			for(List<String> myPostIds : this.myRecentPosts.values()){
				for(String myPostId : myPostIds){
					Post post = Application.getPostsCache().getPost(myPostId);
					
					count--;
					
					if(count < 0){
						break;
					}
					
					if(!latestPosts.containsKey(post.getCreationTime())){
						latestPosts.put(post.getCreationTime(),new LinkedList<Post>());
					}
					latestPosts.get(post.getCreationTime()).add(post);
				}
			}
		}
		
		if(this.recentPostsImFollowing != null){
			Set<Entry<Long, List<String>>> entries = this.recentPostsImFollowing.entrySet();
			for(Entry<Long, List<String>> entry : entries){
				for(String postId : entry.getValue()){
					
					count--;
					
					if(count < 0){
						break;
					}
					
					Post post = Application.getPostsCache().getPost(postId);
					
					if(!latestPosts.containsKey(post.getCreationTime())){
						latestPosts.put(post.getCreationTime(),new LinkedList<Post>());
					}
					latestPosts.get(post.getCreationTime()).add(post);
					
				}
				
			}
		}
		
		
		List<Post> result = new LinkedList<Post>();
		for(List<Post> posts : latestPosts.values()){
			result.addAll(posts);
		}
		
		return result;
	}
	
	
	public void follow(String userId){
		this.peopleImFollowing.add(userId);
	}
	
	public JSONObject toJsonObject() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", this.id);
		jsonObject.put("user_name", this.userName);
		
		return jsonObject;
	}
}