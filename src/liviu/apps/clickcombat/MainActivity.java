package liviu.apps.clickcombat;

import java.util.ArrayList;

import liviu.apps.clickcombat.data.Domain;
import liviu.apps.clickcombat.interfaces.DataListener;
import liviu.apps.clickcombat.managers.DataManager;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class MainActivity extends Activity implements DataListener {
	
	// constants
	private final String TAG		= "MainActivity";
	
	// data
	private DataManager	 dMan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();        
        window.setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);      
        
        dMan = new DataManager();
        
        dMan.setDataListener(this);
        dMan.getAllDomains();
    }

	public void onAllDomainsLoaded(ArrayList<Domain> domainsList) {
		Log.e(TAG, "val: " + domainsList);
		if(domainsList != null)
			Log.e(TAG, "domainsList length: " + domainsList.size());
	}
}


