package mis.eat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class OpenListActivity extends Activity {
	
	private Bundle store_data;
	
	private TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_list);
		
		Bundle bundle = getIntent().getExtras();
		store_data = bundle.getBundle("store_data");
		
		loadGroup();
	}

	private void loadGroup() {
		String result = DBConnector.executeQuery("SELECT * FROM `group` WHERE store_id=" + store_data.getInt("store_id"));
		TableLayout list_layout = (TableLayout) findViewById(R.id.open_list);
		list_layout.setStretchAllColumns(true);
		
		TableRow tr = new TableRow(OpenListActivity.this);
        tr.setLayoutParams(row_layout);
        tr.setGravity(Gravity.CENTER_HORIZONTAL);
        tr.addView(createTextView("帳號"));
        tr.addView(createTextView("電話"));
        tr.addView(createTextView("店家"));
        tr.addView(createTextView(""));
        
        list_layout.addView(tr);
        
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                
                Bundle group_data = new Bundle();
                group_data.putInt("group_id", jsonData.getInt("group_id"));
                
                int temp = jsonData.getInt("user_id");
                String tpResult = DBConnector.executeQuery("SELECT * FROM user WHERE user_id=" + temp);
                JSONArray tempArray = new JSONArray(tpResult); 
                JSONObject tempData = tempArray.getJSONObject(0);
                group_data.putString("user_name", tempData.getString("user_name"));
                group_data.putString("user_tel", tempData.getString("user_tel"));
                
                temp = store_data.getInt("store_id");
                tpResult = DBConnector.executeQuery("SELECT * FROM store WHERE store_id=" + temp);
                tempArray = new JSONArray(tpResult); 
                tempData = tempArray.getJSONObject(0);
                group_data.putString("store_name", tempData.getString("store_name"));                
                
                list_layout.addView(createTableRow(group_data, group_data.getInt("group_id")));
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
	}
	
	private TextView createTextView(String text) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setLayoutParams(view_layout);
		
		return textView;
	}
	
	private TableRow createTableRow(Bundle dataSet, final int group_id) {
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(row_layout);
		
		String[] keySet = dataSet.keySet().toArray(new String[0]);
		for(int i = 0; i < keySet.length; i++) {
			tableRow.addView(createTextView(dataSet.get(keySet[i]).toString()));
		}
		
		Button go_order = new Button(this);
		go_order.setText("GO!!!");
		go_order.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(OpenListActivity.this, NewOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("group_id", group_id);
				bundle.putBundle("store_data", store_data);
				bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
				//Log.v("user-id", Integer.toString(getIntent().getExtras().getInt("user_id")));
				intent.putExtras(bundle);
				startActivity(intent);
			}			
		});
		
		tableRow.addView(go_order);
		
		return tableRow;
	}
}
