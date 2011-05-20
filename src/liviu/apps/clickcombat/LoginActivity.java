package liviu.apps.clickcombat;

import liviu.apps.clickcombat.data.User;
import liviu.apps.clickcombat.interfaces.LoginListener;
import liviu.apps.clickcombat.managers.ActivityIdProvider;
import liviu.apps.clickcombat.ui.LEditText;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity implements 	OnClickListener,
													 	LoginListener{

	// constants
	private final String TAG			= "LoginActivity";
	private final int 	 REQUEST_CODE	= ActivityIdProvider.getInstance().getNewId();	
	
	// data
	private User user;
	
	//ui
	private Button		butLogin;
	private Button		butSignUp;
	private Button		butLater;
	private LEditText	ledtxUserName;
	private LEditText	ledtxUserPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
        Window window = getWindow();        
        window.setFormat(PixelFormat.RGBA_8888);
        requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.login_layout);      
        
        butLogin 			= (Button)findViewById(R.id.login_button_login);
        butSignUp			= (Button)findViewById(R.id.login_button_sign_up);
        butLater			= (Button)findViewById(R.id.login_button_later);
        ledtxUserName		= (LEditText)findViewById(R.id.login_edtx_username);
        ledtxUserPassword 	= (LEditText)findViewById(R.id.login_edtx_password);        
        user 				= User.getInstance();               
        
        if(user.isLoggedIn()){
        	Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        	startActivity(toMainActivity);
        	finish();
        	return;
        }
        
        user.setLoginListener(this);
        butLogin.setOnClickListener(this);
        butSignUp.setOnClickListener(this);
        butLater.setOnClickListener(this);
	}

	public void onUserLogin(boolean loginStatus, String message) {
		if(loginStatus){
			Toast.makeText(LoginActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
			butLogin.setText("Success");
        	Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        	startActivity(toMainActivity);
        	finish();
        	return;			
		}
		else{
			Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
			butLogin.setText("Login");
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_button_login:
			if(ledtxUserName.getText().toString().length() == 0){
				Toast.makeText(LoginActivity.this, "Please type your username.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(ledtxUserPassword.getText().toString().length() == 0){
				Toast.makeText(LoginActivity.this, "Please type your password.", Toast.LENGTH_SHORT).show();
				return;
			}			
			
			butLogin.setText("Loading...");
			user.login(ledtxUserName.getText().toString(), ledtxUserPassword.getText().toString());			
			break;
		case R.id.login_button_later:
        	Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        	startActivity(toMainActivity);
        	finish();
			break;
		default:
			break;
		}
	}	
	
	// listeners
}
