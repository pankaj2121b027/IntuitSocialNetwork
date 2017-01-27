package personal.intuit.socialnetwork.ws;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import personal.intuit.socialnetwork.application.Application;
import personal.intuit.socialnetwork.application.JsonConvertible;
import personal.intuit.socialnetwork.application.model.Post;
import personal.intuit.socialnetwork.application.model.User;
import personal.intuit.socialnetwork.application.utils.Util;

@Path("/social")
public class UserService {
	
	/**get recent posts/timeline for user
	 * 
	 * @param userid
	 * @return
	 */
   @GET
   @Path("/feed/{userid}")
   @Produces(MediaType.TEXT_PLAIN)
   public String getTimeline(@PathParam("userid")String userid){
	   
      try {
		  
    	  List<Post> postsList = Application.getPosts(userid);  
    	  List<JsonConvertible> posts = new LinkedList<>(); 
    	  for(Post post : postsList){
    		  posts.add(post);
    	  }
    			  
    	 return Util.getJsonString(posts);
    	  
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
   }
   
   /**
    * Get users
    * @return
    */
   @GET
   @Path("/users/")
   @Produces(MediaType.TEXT_PLAIN)
   public String getUsers(){
      try {
    	  
    	  int count = 10;
    	  List<User> usersList = Application.getUserCache().getRandomUsers(count);
    	  List<JsonConvertible> users = new LinkedList<>();
    	  for(User user : usersList){	  
    		  users.add(user);
    		  count--;
    	  }
    	  
    	  return Util.getJsonString(users);
    	  
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
   }
}