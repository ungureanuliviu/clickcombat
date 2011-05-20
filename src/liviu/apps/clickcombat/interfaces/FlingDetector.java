package liviu.apps.clickcombat.interfaces;

import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class FlingDetector  extends SimpleOnGestureListener{
	
	// constants
	private 	   final String TAG							= "FlingDetector";
    private static final int 	SWIPE_MIN_DISTANCE 			= 120;
    private static final int 	SWIPE_MAX_OFF_PATH 			= 250;
    private static final int 	SWIPE_THRESHOLD_VELOCITY 	= 200;
    
    // listeners
    private SwipeListener swipeListener;
    
    public FlingDetector() { }
		 
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {	                
                if(swipeListener != null)
                	swipeListener.onSwipeRight();
                return true;
            }  
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {               
	            	if(swipeListener != null)
	            		swipeListener.onSwipeLeft();
	            	return true;
            }
        }
        catch (Exception e) { }
        return false;
    }
    
    public void setSwipeListener(SwipeListener listener){
    	swipeListener = listener;
    }
}
