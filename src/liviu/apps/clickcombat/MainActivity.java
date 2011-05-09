package liviu.apps.clickcombat;

import java.util.ArrayList;

import liviu.apps.clickcombat.adapters.DomainsAdapter;
import liviu.apps.clickcombat.data.Domain;
import liviu.apps.clickcombat.interfaces.DataListener;
import liviu.apps.clickcombat.managers.DataManager;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements DataListener {
	
	// constants
	private final String 		TAG		= "MainActivity";
	
	// data
	private DataManager	 		dMan;
	private DomainsAdapter 		adapter;
	
	// ui
	private ProgressBar	 		pBar; 
	private ListView	 		listView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();        
        window.setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);      
        
        dMan 		= new DataManager();
        pBar 		= (ProgressBar)findViewById(R.id.loading_progress);
        listView 	= (ListView)findViewById(R.id.main_domains_list);
        adapter		= new DomainsAdapter(this);
        
        dMan.setDataListener(this);
        dMan.getAllDomains();
        pBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

	public void onAllDomainsLoaded(ArrayList<Domain> domainsList) {
		
		adapter.clear();
		Log.e(TAG, "val: " + domainsList);
		if(domainsList != null){			
			Log.e(TAG, "domainsList length: " + domainsList.size());
			for(int i = 0; i < domainsList.size(); i++)
				adapter.addItem(domainsList.get(i));
			
			listView.setAdapter(adapter);
	        pBar.setVisibility(View.GONE);
	        listView.setVisibility(View.VISIBLE);
		}
		else
			Toast.makeText(MainActivity.this, "Error: please check you network connection.", Toast.LENGTH_LONG).show();
		
		adapter.notifyDataSetChanged();
	}
}


