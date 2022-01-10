package mis.eat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViews();
		setListeners();
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	    .detectDiskReads()
	    .detectDiskWrites()
	    .detectNetwork()   // or .detectAll() for all detectable problems
	    .penaltyLog()
	    .build());
	    
	    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	    .detectLeakedSqlLiteObjects()
	    .penaltyLog()
	    .penaltyDeath()
	    .build());
	}
	
	private EditText account_editText;
	private EditText pwd_editText;
	private Button login_button;
	
	private void findViews() {
		account_editText = (EditText)findViewById(R.id.account_editText);
		pwd_editText = (EditText)findViewById(R.id.pw_editText);
		login_button = (Button)findViewById(R.id.login_button);
	}
	
	private void setListeners() {
		login_button.setOnClickListener(click_login);
	}
	
	private Button.OnClickListener click_login = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String account = account_editText.getText().toString();
			String pwd = pwd_editText.getText().toString();
			
			// 判對帳號密碼是否正確
			//if(account.equals("") && pwd.equals("")) {
			
			String result = DBConnector.executeQuery("SELECT * FROM user WHERE user_account=^" + account + "^ AND user_pwd=^" + pwd + "^");
			Log.v("login", result);
			if(!result.equals("empty\n")) {
				int user_id = 0;
				try {
					JSONArray jsonArray = new JSONArray(result);
					JSONObject jsonData = jsonArray.getJSONObject(0);
					user_id = jsonData.getInt("user_id");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("log_tag", e.toString());
				}
				
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("user_id", user_id);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				errMsg("登入失敗！");
			}
		}		
	};
	
	private void errMsg(String msg) {
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
