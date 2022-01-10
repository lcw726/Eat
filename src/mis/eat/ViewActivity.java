package mis.eat;

import java.security.KeyStore.LoadStoreParameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);

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

		loadStore();
	}

	private Button random_button;
	private Button newstore_button;

	private void findViews() {
		random_button = (Button) findViewById(R.id.random_button);
		newstore_button = (Button) findViewById(R.id.new_button);
	}

	private void setListeners() {
		random_button.setOnClickListener(click_random);
	}

	private Button.OnClickListener click_random = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String result = DBConnector.executeQuery("SELECT * FROM store ORDER BY RAND() LIMIT 1");
			JSONArray jsonArray;
			Bundle store_data = null;
			try {
				jsonArray = new JSONArray(result);
				JSONObject jsonData = jsonArray.getJSONObject(0);
				store_data = new Bundle();
                store_data.putInt("store_id", jsonData.getInt("store_id"));
                store_data.putString("store_name", jsonData.getString("store_name"));
                store_data.putString("store_tel", jsonData.getString("store_tel"));
                store_data.putString("store_addr", jsonData.getString("store_addr"));
                store_data.putString("store_menu", jsonData.getString("store_menu"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.setClass(ViewActivity.this, StoreInfoActivity.class);
			Bundle bundle = new Bundle();
			// 這邊要傳值，隨機選一間店
			bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
			bundle.putBundle("store_data", store_data);
			//Log.v("user-id", Integer.toString(getIntent().getExtras().getInt("user_id")));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	private void loadStore() {
		String result = DBConnector.executeQuery("SELECT * FROM store");
		TableLayout list_layout = (TableLayout) findViewById(R.id.store_list);
		list_layout.setStretchAllColumns(true);
		
		// Log.v("result", result);
		
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(ViewActivity.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                
                Bundle store_data = new Bundle();
                store_data.putInt("store_id", jsonData.getInt("store_id"));
                store_data.putString("store_name", jsonData.getString("store_name"));
                store_data.putString("store_tel", jsonData.getString("store_tel"));
                store_data.putString("store_addr", jsonData.getString("store_addr"));
                store_data.putString("store_menu", jsonData.getString("store_menu"));
                
                TextView store_name = new TextView(this);
                store_name.setText(store_data.getString("store_name"));
                store_name.setLayoutParams(view_layout);
                
                TextView store_tel = new TextView(this);
                store_tel.setText(store_data.getString("store_tel"));
                store_tel.setLayoutParams(view_layout);
                
                Button button_info = new Button(this);
                button_info.setText("查看資訊");
                button_info.setLayoutParams(view_layout);
                setDynamicListener(button_info, store_data);
                
                tr.addView(store_name);
                tr.addView(store_tel);
                tr.addView(button_info);
                list_layout.addView(tr);
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
	}
	
	private void setDynamicListener(Button btn, final Bundle store_data) {
		btn.setOnClickListener(new Button.OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ViewActivity.this, StoreInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBundle("store_data", store_data);
				bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
}
