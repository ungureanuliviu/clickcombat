package liviu.apps.clickcombat;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();        
        window.setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.main);                
        
    }



}


