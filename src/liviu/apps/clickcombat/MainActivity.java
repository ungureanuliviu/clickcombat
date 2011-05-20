package liviu.apps.clickcombat;

import java.util.ArrayList;

import liviu.apps.clickcombat.adapters.DomainsAdapter;
import liviu.apps.clickcombat.data.AnimationHelper;
import liviu.apps.clickcombat.data.Domain;
import liviu.apps.clickcombat.interfaces.DataListener;
import liviu.apps.clickcombat.interfaces.FlingDetector;
import liviu.apps.clickcombat.interfaces.SwipeListener;
import liviu.apps.clickcombat.managers.ActivityIdProvider;
import liviu.apps.clickcombat.managers.DataManager;
import liviu.apps.clickcombat.utils.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements DataListener,
													  OnClickListener,
													  OnScrollListener,
													  OnItemClickListener,
													  SwipeListener{
	
	// constants
	private final String 		TAG			= "MainActivity";
	public  final int			REQUEST_ID  = ActivityIdProvider.getInstance().getNewId(); 
    
	// data
	private DataManager	 		dMan;
	private DomainsAdapter 		adapter;
	private DomainsAdapter		adapterLatest;
	private int					pagesCount;
	private int 				lastPageLoaded;
	private LayoutInflater		lf;
    private GestureDetector 	gestureDetector;
    private OnTouchListener		gestureListener;
    private FlingDetector		flingDetector;
    
	// ui        
	private ProgressBar	 		pBar; 
	private ListView	 		listView;
	
	private ProgressBar	 		pBarLatest; 
	private ListView	 		listViewLatest;
	
	private ImageButton			butHome;
	private ImageButton			butInfo;	
	private RelativeLayout		listFooter;
	private ViewFlipper			viewFlipper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();        
        window.setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);      
        
        dMan 			= new DataManager();
        pBar 			= (ProgressBar)findViewById(R.id.loading_progress);
        listView 		= (ListView)findViewById(R.id.main_domains_list);
        adapter			= new DomainsAdapter(this);
        butHome			= (ImageButton)findViewById(R.id.lt_but_home);
        butInfo			= (ImageButton)findViewById(R.id.lt_but_info); 
        pagesCount		= 0;
        lastPageLoaded 	= -1;
        lf				= (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listFooter		= (RelativeLayout)lf.inflate(R.layout.listview_footer, null);
        viewFlipper		= (ViewFlipper)findViewById(R.id.lt_view_flipper);     
        pBarLatest		= (ProgressBar)findViewById(R.id.loading_progress_latest_domains);
        listViewLatest	= (ListView)findViewById(R.id.main_domains_list_latest_domains);
        adapterLatest	= new DomainsAdapter(this);
        
        flingDetector	= new FlingDetector();
        gestureDetector = new GestureDetector(flingDetector);
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };        
        
        flingDetector.setSwipeListener(this);
        listView.addFooterView(listFooter);
        
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);   
        viewFlipper.setOnTouchListener(gestureListener);
        
        dMan.setDataListener(this);
        dMan.getPagesCount(Constants.PAGE_SIZE);        
        pBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        listViewLatest.setOnItemClickListener(this);
        butHome.setOnClickListener(this);
        butInfo.setOnClickListener(this);
        listView.setOnTouchListener(gestureListener);
        listViewLatest.setOnTouchListener(gestureListener);
    
    }

	public void onAllDomainsLoaded(ArrayList<Domain> domainsList) { }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lt_but_info:
			viewFlipper.showNext();
			Toast.makeText(MainActivity.this, "Infos", Toast.LENGTH_LONG).show();
			break;
		case R.id.lt_but_home:
			viewFlipper.showPrevious();
			break;
		default:
			break;
		}
	}

	public void onPagesCountLoaded(int count) {
		Log.e(TAG, "page count is: " + count);			
		pagesCount = count;
		
		if(pagesCount > 0)
			dMan.getPage(0);		
		else{
			Toast.makeText(MainActivity.this, "No domains.", Toast.LENGTH_SHORT).show();
			pBar.setVisibility(View.GONE);
		}		
	}

	public void onPageLoaded(int pageIndex, ArrayList<Domain> domains) {
		Log.e(TAG, "onPageLoaded: pageIndex " + pageIndex + " domains " + domains);
		
		listFooter.setVisibility(View.GONE);		
		lastPageLoaded = pageIndex;
		
		if(domains != null){			
			Log.e(TAG, "domainsList length: " + domains.size());
			for(int i = 0; i < domains.size(); i++){
				adapter.addItem(domains.get(i));
			}
			
			if(listView.getVisibility() != View.VISIBLE){
				listView.setAdapter(adapter);				
		        pBar.setVisibility(View.GONE);
		        listView.setVisibility(View.VISIBLE);
			}
			else
				adapter.notifyDataSetChanged();
		}
		else
			Toast.makeText(MainActivity.this, "Error: please check you network connection.", Toast.LENGTH_LONG).show();		
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {		
		
		if((firstVisibleItem + 1 + visibleItemCount) > totalItemCount){			
			
			if(lastPageLoaded + 1 < pagesCount){
				lastPageLoaded++;
				listFooter.setVisibility(View.VISIBLE);
				dMan.getPage(lastPageLoaded);
			}
			else{
				listFooter.setVisibility(View.GONE);
			}
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
		Domain domain =  new Domain();
		if(adapterView.getId() == R.id.main_domains_list)
			domain = adapter.getItem(position);
		else if(adapterView.getId() == R.id.main_domains_list_latest_domains)
			domain = adapterLatest.getItem(position);
		
		dMan.click(domain);
		Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www." + domain.getName()));
		startActivity(browserIntent);		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {	
		super.onConfigurationChanged(newConfig);
	}

	public void onSwipeLeft() {
		viewFlipper.setInAnimation(AnimationHelper.inFromLeftAnimation());
        viewFlipper.setOutAnimation(AnimationHelper.outToRightAnimation());		
		viewFlipper.showNext();
		Log.e(TAG, "left: " + viewFlipper.getCurrentView().getId() + " raw: " + R.id.lt_view_latest_domains);
		if(viewFlipper.getCurrentView().getId() == R.id.lt_view_latest_domains){
			if(adapterLatest.getCount() == 0 && dMan.latestDomainsAreDownloading() == false){
				dMan.getLatestDomains();
			}
		}
	}

	public void onSwipeRight() {
		viewFlipper.setInAnimation(AnimationHelper.inFromRightAnimation());
        viewFlipper.setOutAnimation(AnimationHelper.outToLeftAnimation());		
		viewFlipper.showPrevious();		
		Log.e(TAG, "right: " + viewFlipper.getCurrentView().getId() + " raw: " + R.id.lt_view_latest_domains);
		if(viewFlipper.getCurrentView().getId() == R.id.lt_view_latest_domains){
			if(adapterLatest.getCount() == 0 && dMan.latestDomainsAreDownloading() == false){
				dMan.getLatestDomains();
			}
		}		
	}

	public void onLatestDomainsLoaded(ArrayList<Domain> latestDomains) {
		
		if(latestDomains != null){			
			Log.e(TAG, "latest domains list size: " + latestDomains.size());
			for(int i = 0; i < latestDomains.size(); i++){
				adapterLatest.addItem(latestDomains.get(i));
			}
			
			if(listViewLatest.getVisibility() != View.VISIBLE){
				listViewLatest.setAdapter(adapterLatest);				
		        pBarLatest.setVisibility(View.GONE);
		        listViewLatest.setVisibility(View.VISIBLE);
			}
			else
				adapterLatest.notifyDataSetChanged();
		}
		else{
			Toast.makeText(MainActivity.this, "Error: please check you network connection.", Toast.LENGTH_LONG).show();
			pBarLatest.setVisibility(View.GONE);
		}
	}
}


