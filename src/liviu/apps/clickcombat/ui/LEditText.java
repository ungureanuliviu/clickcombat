package liviu.apps.clickcombat.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class LEditText extends EditText{

	// constants
	private final String TAG = "LEditText";
	
	public LEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public LEditText(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(event.getX() > LEditText.this.getWidth() - 30){
				LEditText.this.setText("");
			}
		}
		return super.onTouchEvent(event);
	}

}
