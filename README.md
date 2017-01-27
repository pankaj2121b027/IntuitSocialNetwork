# IntuitSocialNetwork
This is Social Network Service designed for Intuit Coding Challenge  
Provide 2 REST Endpoints to Get Users and to Get user timeline / posts  

Uses SQLite in memory database and JAX-RS  
Configuration is in /src/personal/intuit/socialnetwork/application/Constants.java  

### Compile

This project does not use maven  
To create WAR, use Eclipse  

```
1. Right click on Project
2. Select Export
3. Select WAR
4. Save at a directory

```

### Deployment
```
1. Install Tomcat Web Server 
2. Copy WAR to Tomcat's webapp directory
3. Start Tomcat
4. Get User IDs from http://localhost:8080/IntuitSocialNetwork/rest/social/users
5. Get Post details from http://localhost:8080/IntuitSocialNetwork/rest/social/feed/{user_id} 
```