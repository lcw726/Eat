package mis.eat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewOpenActivity extends Activity {
	
	private Bundle store_data;
	private int user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newopen);
		
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
		
		Bundle bundle = getIntent().getExtras();
		store_data = bundle.getBundle("store_data");
		user_id = bundle.getInt("user_id");
		
		store_name_textView.setText(store_data.getString("store_name"));
	}

	private TextView store_name_textView;
	private TimePicker stop_timePicker;
	private TimePicker arrive_timePicker;
	private EditText place_editText;
	private EditText notice_editText;
	private Button confirm_button;
	
	private void findViews() {
		store_name_textView = (TextView)findViewById(R.id.store_name);
		stop_timePicker = (TimePicker)findViewById(R.id.stop_timePicker);
		arrive_timePicker = (TimePicker)findViewById(R.id.arrive_timePicker);
		place_editText = (EditText)findViewById(R.id.get_place_editText);
		notice_editText = (EditText)findViewById(R.id.notice_editText);
		confirm_button = (Button)findViewById(R.id.open_confirm_button);
	}
	
	private void setListeners() {
		confirm_button.setOnClickListener(click_confirm);
	}
	
	private Button.OnClickListener click_confirm = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int stop_hr = stop_timePicker.getCurrentHour();
			int stop_min = stop_timePicker.getCurrentMinute();
			int arrive_hr = arrive_timePicker.getCurrentHour();
			int arrive_min = arrive_timePicker.getCurrentMinute();
			String get_place = place_editText.getText().toString();
			String notice = notice_editText.getText().toString();
			
			String update_string = "INSERT INTO `group` VALUES(null, " + user_id + ", " + store_data.getInt("store_id") + ", " + stop_hr  + ", " + stop_min + ", " + arrive_hr + ", " + arrive_min + ", ^" + get_place  + "^, ^" + notice  + "^, 1)";
			//Log.v("sql", update_string);
			try {
				DBConnector.executeUpdate(update_string);
			} catch(Exception e) {
				Log.e("exception", e.toString());
			}
			
			Intent intent = new Intent();
			intent.setClass(NewOpenActivity.this, StoreInfoActivity.class);
			Bundle bundle = new Bundle();
			// 這邊要傳值，隨機選一間店
			bundle.putInt("user_id", user_id);
			bundle.putBundle("store_data", store_data);
			//Log.v("user-id", Integer.toString(getIntent().getExtras().getInt("user_id")));
			intent.putExtras(bundle);
			startActivity(intent);
		}		
	};
}
