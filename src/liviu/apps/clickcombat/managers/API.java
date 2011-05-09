package liviu.apps.clickcombat.managers;

import java.io.IOException;
import java.util.ArrayList;

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
	private final 	String 		TAG 		= "API";
	private final 	String		API_URL 	= "http://www.clickcombat.com/api/index.php"; 
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
	
	private String postData(String jsonString) {

	    try {

	        //JSONObject json = new JSONObject("{\"method\":\"check_user_name\",\"params\":{\"username\":\"test\"}}");	       
	        post = new HttpPost(API_URL);
            JSONObject json = new JSONObject(jsonString);           	            	            
            StringEntity en = new StringEntity(json.toString());	
	        post.setEntity(en);
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Content-type", "application/json");
            HttpResponse responsePOST = client.execute(post, httpContext);  
            HttpEntity   resEntity 	  = responsePOST.getEntity();
            
	        return EntityUtils.toString(resEntity);

	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    	return null;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
	    } catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkUsername(String userName){
		boolean status = false;
        JSONObject json;
		try {
			json = new JSONObject("{\"method\":\"check_user_name\",\"params\":{\"username\":\"test\"}}");
			String response = postData(json.toString());
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
	

	public ArrayList<Domain> getAllDomains(){
		ArrayList<Domain> domainList = new ArrayList<Domain>();
		
        JSONObject json;
		try {
			json = new JSONObject();
			json.put("method", "get_domains");
			String response = postData(json.toString());
			Log.e(TAG, "response: " + response);

			JSONArray responseJSON = new JSONArray(response);	
			for(int i = 0; i < responseJSON.length(); i++){
				JSONObject jDomain = responseJSON.getJSONObject(i);
				Domain domain = new Domain();
				domain.setId(jDomain.getInt("dm_id"));
				domain.setName(jDomain.getString("dm_name"));
				domain.setUserId(jDomain.getInt("dm_user_id"));
				domain.setDescription(jDomain.getString("dm_description"));
				domain.setFile(jDomain.getString("dm_file_check"));
				domain.setAddedDate(jDomain.getLong("dm_added_date"));
				domain.setClicks(jDomain.getInt("dm_clicks"));
				domain.setIsVerified(jDomain.getInt("dm_is_verified") == 1 ? true : false);
				domain.setPx(jDomain.getInt("px"));
				domainList.add(domain);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();			
		}
		
		Log.e(TAG, "domainList length: " + domainList.size());
		return domainList;			
	}
	
	
}
