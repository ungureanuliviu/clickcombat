package liviu.apps.clickcombat.managers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import liviu.apps.clickcombat.api.API;
import liviu.apps.clickcombat.data.Domain;
import liviu.apps.clickcombat.interfaces.DataListener;
import liviu.apps.clickcombat.utils.Constants;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DataManager {

	// constants
	private final String TAG = "DataManager";
	
	// messages
	private final int 	MSG_ALL_DOMAINS_LOADED 		= 1;
	private final int   MSG_PAGES_COUNT_RECEIVED 	= 2;
	private final int   MSG_PAGE_LOADED				= 3;
	private final int   MSG_LATEST_DOMAINS_LOADED	= 4;
		
	// data
	private API			api;
	private Handler 	handler;
	
	// listeners
	private DataListener dataListener;
	
	// flags
	private boolean 	latestDomainsAreDownloading;
		
	public DataManager() {
		api 					    = API.getInstance();
		latestDomainsAreDownloading = false;
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MSG_ALL_DOMAINS_LOADED:
					if(dataListener != null){
						ArrayList<Domain> domains = (ArrayList<Domain>)msg.obj;
						dataListener.onAllDomainsLoaded(domains);
					}
					break;
				case MSG_PAGES_COUNT_RECEIVED:
					if(dataListener != null){
						dataListener.onPagesCountLoaded((Integer)msg.obj);
					}
					break;
				case MSG_PAGE_LOADED:
					if(dataListener != null){
						dataListener.onPageLoaded(msg.arg1, (ArrayList<Domain>)msg.obj);
					}
					break;
				case MSG_LATEST_DOMAINS_LOADED:
					if(dataListener != null){
						dataListener.onLatestDomainsLoaded((ArrayList<Domain>)msg.obj);
					}
					break;
				default:
					break;
				}
			};
		};
	}
	
	public void getAllDomains(){
		Thread tDomains = new Thread(new Runnable() {			
			public void run() {
				ArrayList<Domain> domains = api.getAllDomains();
				Message msg = new Message();
				
				msg.what = MSG_ALL_DOMAINS_LOADED;
				msg.obj  = domains;
				
				handler.sendMessage(msg);
			}
		});
		
		tDomains.start();
	}
	
	public void getPagesCount(int pageSize){
		final int pageSize_ = pageSize;
		Thread tGetPagesCount = new Thread(new Runnable() {			
			public void run() {
				int pageCount = api.getPagesCount(pageSize_);
				
				Message msg = new Message();
				msg.obj 	= pageCount;
				msg.what 	= MSG_PAGES_COUNT_RECEIVED;
				
				handler.sendMessage(msg);
			}
		});
		tGetPagesCount.start();
	}
	
	public DataManager setDataListener(DataListener listener){
		dataListener = listener;
		return this;
	}

	public void getPage(int pageIndex) {
		
		final int pageIndex_ = pageIndex;
		Thread tLoadPage = new Thread(new Runnable() {			
			public void run() {
				ArrayList<Domain> domains = api.getPageDomains(pageIndex_, Constants.PAGE_SIZE);
				Message msg = new Message();
				
				msg.obj 	= domains;
				msg.arg1	= pageIndex_;
				msg.what 	= MSG_PAGE_LOADED;
				
				handler.sendMessage(msg);
			}
		});			
		
		tLoadPage.start();
	}

	public void click(Domain domain) {
		Log.e(TAG, "click " + domain);
		final int domainId = domain.getId();
		
		Thread tClick = new Thread(new Runnable() {			
			public void run() {
				Log.e(TAG, "click for " + domainId);
				api.click(domainId);
			}
		});
		tClick.start();
	}

	public void getLatestDomains() {
		Log.e(TAG, "get latest domains");
		latestDomainsAreDownloading = true;
		
		Thread tGetLatest = new Thread(new Runnable() {			
			public void run() {
				Message 			msg 			= new Message();
				ArrayList<Domain> 	latestDomains 	= new ArrayList<Domain>();
				String 				response 		= api.getLatestDomains();
				try {
					if(response != null){
						JSONObject json = new JSONObject(response);	
	
						if(json.getInt("is_success") == 1){
							JSONArray jArray;						
								jArray = json.getJSONArray("domains");
								
								for(int i = 0; i < jArray.length(); i++){
									JSONObject jDomain = jArray.getJSONObject(i);
									Domain domain = new Domain();
									domain.setId(jDomain.getInt("id"));
									domain.setName(jDomain.getString("name"));
									domain.setDescription(jDomain.getString("description"));
									domain.setAddedDate(jDomain.getLong("added_date"));
									domain.setClicks(jDomain.getInt("clicks"));
									domain.setPx(Float.parseFloat(jDomain.getString("px")));
									latestDomains.add(domain);
								}
							}					
						}
					}
				catch (JSONException e) {
					e.printStackTrace();
				}				
				
				msg.obj 	= latestDomains;
				msg.what 	= MSG_LATEST_DOMAINS_LOADED;
				handler.sendMessage(msg);				
			}
		});
		
		tGetLatest.start();
	}
	
	public boolean latestDomainsAreDownloading(){
		return latestDomainsAreDownloading;
	}
		
}
