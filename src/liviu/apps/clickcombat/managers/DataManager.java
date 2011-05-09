package liviu.apps.clickcombat.managers;

import java.util.ArrayList;

import liviu.apps.clickcombat.data.Domain;
import liviu.apps.clickcombat.interfaces.DataListener;

import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;

public class DataManager {

	// constants
	private final String TAG = "DataManager";
	
	// messages
	private final int 	MSG_ALL_DOMAINS_LOADED = 1;
		
	// data
	private API			api;
	private Handler 	handler;
	
	// listeners
	private DataListener dataListener;
		
	public DataManager() {
		api = API.getInstance();
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MSG_ALL_DOMAINS_LOADED:
					if(dataListener != null){
						ArrayList<Domain> domains = (ArrayList<Domain>)msg.obj;
						dataListener.onAllDomainsLoaded(domains);
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
	
	public DataManager setDataListener(DataListener listener){
		dataListener = listener;
		return this;
	}
		
}
