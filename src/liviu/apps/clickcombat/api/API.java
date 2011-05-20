package liviu.apps.clickcombat.api;

import java.io.IOException;
import java.util.ArrayList;

import liviu.apps.clickcombat.data.Base64;
import liviu.apps.clickcombat.data.Domain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class API {
	// constants
	private final 	String 		TAG 			= "API";
	private final 	String		API_URL_SECURE 	= "http://www.clickcombat.com/api/secure/";
	private final 	String		API_URL 		= "http://www.clickcombat.com/api/";
	private static 	API 		instance;	
	
	// data
	private 	   	HttpClient 				client; 	
	private         ClientConnectionManager	cm;
    private 	    HttpPost 				post;	  
	private 	    HttpContext 			httpContext;
	private		    HttpParams 				params;
	
	private API() {
		
		params 		= new BasicHttpParams();
        httpContext = new BasicHttpContext();
        ConnManagerParams.setMaxTotalConnections(params, 300);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);                          
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80) );
       
        cm 			= new ThreadSafeClientConnManager(params, schemeRegistry);
        client 		= new DefaultHttpClient(cm, params);        		 		
		
	}
	
	
	public static API getInstance(){
		if(instance == null){
			instance = new API();
			return instance;
		}
		else
			return instance;
	}
	
	private static String getCredentials(String userName, String password){		
		return Base64.encodeBytes((userName + ":" + password).getBytes());
	}
	
	private String makeRequest(String url, JSONObject json, String userName, String userPassword) {
		
	    try {	       
	        post 			= new HttpPost(url);              	            	           
            StringEntity en = new StringEntity(json.toString());	
            
	        post.setEntity(en);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");
	        
	        if(userName != null && userPassword != null)
	        	post.addHeader("Authorization","Basic "+ getCredentials(userName, userPassword));	        
	        
            HttpResponse responsePOST = client.execute(post, httpContext);  
            HttpEntity   resEntity 	  = responsePOST.getEntity();
            
	        return EntityUtils.toString(resEntity);

	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    	return null;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
	    }
	}
	
	public boolean checkUsername(String userName){
		boolean status = false;
        JSONObject json;
		try {
			json = new JSONObject("{\"method\":\"check_user_name\",\"params\":{\"username\":\"test\"}}");
			String response = makeRequest(API_URL, json, null, null);
			JSONObject responsJSON = new JSONObject(response);
			if(responsJSON.getInt("is_available") == 1)
				return true;
			else
				return false;
		}
		catch (JSONException e) {
			e.printStackTrace();
			status = false;
		}
		return status;		
	}
	

	public synchronized ArrayList<Domain> getAllDomains(){
		ArrayList<Domain> domainList = new ArrayList<Domain>();
		
        JSONObject json;
		try {
			json = new JSONObject();
			json.put("method", "get_domains");
			String response = makeRequest(API_URL, json, null, null);
			Log.e(TAG, "response: " + response);

			if(response == null)
				return domainList;
			
			JSONArray responseJSON = new JSONArray(response);	
			for(int i = 0; i < responseJSON.length(); i++){
				JSONObject jDomain = responseJSON.getJSONObject(i);
				Domain domain = new Domain();
				domain.setId(jDomain.getInt("id"));
				domain.setName(jDomain.getString("name"));
				domain.setUserId(jDomain.getInt("user_id"));
				domain.setDescription(jDomain.getString("description"));
				domain.setFile(jDomain.getString("file_check"));
				domain.setAddedDate(jDomain.getLong("added_date"));
				domain.setClicks(jDomain.getInt("clicks"));
				domain.setIsVerified(jDomain.getInt("is_verified") == 1 ? true : false);
				domain.setPx(Float.parseFloat(jDomain.getString("px")));
				domainList.add(domain);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();			
		}
		
		Log.e(TAG, "domainList length: " + domainList.size());
		return domainList;			
	}


	public synchronized int getPagesCount(int pageSize_) {
		JSONObject json 	= new JSONObject();
		JSONObject params 	= new JSONObject();
		
		try {
			params.put("items_count", pageSize_);
			json.put("method", "get_total_pages");
			json.put("params", params);
			String result = makeRequest(API_URL, json, null, null);			
			Log.e(TAG, "getPagesCount: " + result);
			
			if(result == null)
				return -1;
			
			JSONObject jResponse = new JSONObject(result);
			if(jResponse != null){
				if(jResponse.getInt("is_success") == 1){
					return jResponse.getInt("pages_count");
				}
				else 
					return 0;						
			}					
		}
		catch (JSONException e) {		
			e.printStackTrace();			
		}		
		
		return 0;
	}


	public synchronized ArrayList<Domain> getPageDomains(int pageIndex, int pageSize) {
		ArrayList<Domain> domainList = new ArrayList<Domain>();
		
        JSONObject json;
        JSONObject params;
        
		try {
			json 	= new JSONObject();
			params 	= new JSONObject();
			
			params.put("page_index", pageIndex);
			params.put("page_size", pageSize);			
			json.put("method", "get_page");
			json.put("params", params);
			
			String response = makeRequest(API_URL, json, null, null);
			Log.e(TAG, "response: " + response);

			if(response == null)
				return domainList;
			
			JSONObject responseJSON = new JSONObject(response);	
			
			if(responseJSON.getInt("is_success") == 1){
				JSONArray jArray = responseJSON.getJSONArray("domains");
				
				for(int i = 0; i < jArray.length(); i++){
					JSONObject jDomain = jArray.getJSONObject(i);
					Domain domain = new Domain();
					domain.setId(jDomain.getInt("id"));
					domain.setName(jDomain.getString("name"));
					domain.setDescription(jDomain.getString("description"));
					domain.setAddedDate(jDomain.getLong("added_date"));
					domain.setClicks(jDomain.getInt("clicks"));
					domain.setPx(Float.parseFloat(jDomain.getString("px")));
					domainList.add(domain);
				}
			}
			else
				return domainList;
		}
		catch (JSONException e) {
			e.printStackTrace();			
		}
		
		Log.e(TAG, "domainList length: " + domainList.size());
		return domainList;			
	}


	public synchronized void click(int domainId) {
		
		JSONObject json 	= new JSONObject();
		JSONObject params 	= new JSONObject();
		
		try {
			params.put("domain_id", domainId);
			json.put("method", "click");
			json.put("params", params);
			Log.e(TAG, "click for: " + domainId + " response: " + makeRequest(API_URL, json, null, null));
		}
		catch (JSONException e) {		
			e.printStackTrace();
		}
	}
	
	public synchronized String login(String user_name, String user_password){
		JSONObject json 	= new JSONObject();
		JSONObject params 	= new JSONObject();
		
		try {
			json.put("method", "log_in_user");
			params.put("user_name", user_name);
			params.put("user_pass", user_password);
			json.put("params", params);
			String result = makeRequest(API_URL_SECURE, json, user_name, user_password);
			Log.e(TAG, "login result: " + result);
			return result;			
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	public synchronized String getLatestDomains() {
		JSONObject json 	= new JSONObject();
		
		try {
			json.put("method", "get_latest_domains");
			String response = makeRequest(API_URL, json, null, null);
			Log.e(TAG, "getLatestDomains response: " + response);
			return response;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
}
