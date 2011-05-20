package liviu.apps.clickcombat.data;

import org.json.JSONException;
import org.json.JSONObject;

import liviu.apps.clickcombat.api.API;
import liviu.apps.clickcombat.interfaces.LoginListener;
import android.os.Handler;
import android.os.Message;

public class User {
	
	// constants
	private final String 	TAG					= "User";
	private final int		MSG_LOGIN_FAILED	= 0;
	private final int		MSG_LOGIN_SUCCEES	= 1;
	
	
	// data
	private String 	name;
	private int	   	id;
	private String 	password;
	private String 	email;
	private String 	fname;
	private String 	sname;
	private boolean isLoggedIn;
	private API	   	api;
	private Handler	handler;	
	private static User instance;
	
	// listeners
	private LoginListener loginListener;
	
	
	private User(){
		name 		= "";
		password	= "";
		id			= -1;
		email		= "";
		fname		= "";
		sname		= "";
		isLoggedIn  = false;
		api			= API.getInstance();
		handler		= new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MSG_LOGIN_FAILED:
					if(loginListener != null)
						loginListener.onUserLogin(false, (String)msg.obj);					
					break;
				case MSG_LOGIN_SUCCEES:
					if(loginListener != null)
						loginListener.onUserLogin(true, (String)msg.obj);
					break;

				default:
					break;
				}
			};
		};
	}
	
	public static User getInstance(){
		if(instance == null){
			instance = new User();
			return instance;
		}
		else
			return instance;
	}
	
	public void login(String userName, String userPassword){
		final String userName_ 		= userName;
		final String userPassword_ 	= userPassword;
		
		Thread tLogin = new Thread(new Runnable() {			
			public void run() {
				String 	response = api.login(userName_, userPassword_);
				Message	msg		 = new Message();
				
				if(response == null){
					msg.obj 	= new String("Please check your internet connection.");
					msg.what 	= MSG_LOGIN_FAILED;
					handler.sendMessage(msg);
				}
				else{
					try {
						JSONObject json = new JSONObject(response);
						if(json.getInt("is_success") == 1){
							JSONObject jsonUser = json.getJSONObject("data").getJSONObject("user");
							id 			= jsonUser.getInt("id");
							name 		= userName_;
							password 	= userPassword_;
							email		= jsonUser.getString("email");
							fname		= jsonUser.getString("fname");
							sname		= jsonUser.getString("sname");
							isLoggedIn 	= true;
							
							msg.obj 	= new String("Ok");
							msg.what 	= MSG_LOGIN_SUCCEES;
							handler.sendMessage(msg);	
						}
						else{
							msg.obj 	= new String("Please check your username/password.");
							msg.what 	= MSG_LOGIN_FAILED;
							handler.sendMessage(msg);							
						}
					} 
					catch (JSONException e) {
						e.printStackTrace();
						msg.obj 	= new String("Please check your username/password.");
						msg.what 	= MSG_LOGIN_FAILED;
						handler.sendMessage(msg);
					}
				}
			}
		});
		tLogin.start();
	}
	
	public boolean isLoggedIn(){
		return isLoggedIn;
	}
	
	// setters
	public void setLoginListener(LoginListener listener){
		loginListener = listener;
	}
}
