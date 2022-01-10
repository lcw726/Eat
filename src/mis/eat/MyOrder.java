package mis.eat;

import org.json.JSONArray;
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
import android.widget.Toast;


public class MyOrder extends Activity {	
	private int user_id;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);
        
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
		user_id = bundle.getInt("user_id");
	    
        openQuery();   
        withQuery();
    }
    
	private void openQuery(){
		String result = DBConnector.executeQuery("SELECT SUM(price),store_name,A2.group_id FROM user_group A1,`group` A2,store A3 WHERE A2.user_id="+user_id+"  AND A3.store_id=A2.store_id GROUP BY A2.group_id");
		//String sum = DBConnector.executeQuery("SELECT SUM(price) FROM user_group A1,`group` A2 WHERE A2.user_id=" + user_id + " GROUP BY A2.group_id");
		TableLayout list_layout = (TableLayout) findViewById(R.id.open_list);
		list_layout.setStretchAllColumns(true);
		
		 //Log.v("result", result);
		
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(MyOrder.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                
                /*Bundle budget_data = new Bundle();
                budget_data.putInt("open_account", jsonData.getInt("store_id"));
                budget_data.putString("store_name", jsonData.getString("store_name"));
                budget_data.putString("store_tel", jsonData.getString("store_tel"));
                budget_data.putString("store_addr", jsonData.getString("store_addr"));
                budget_data.putString("store_menu", jsonData.getString("store_menu"));*/
                
                TextView store_name = new TextView(this);
                store_name.setText(jsonData.getString("store_name"));
                store_name.setLayoutParams(view_layout);
                
                Button button_inform = new Button(this);
                button_inform.setText("通知");
                button_inform.setLayoutParams(view_layout);
                setInformListener(button_inform,jsonData.getInt("group_id"));
                
                Button button_open_price = new Button(this);
                button_open_price.setText(jsonData.getString("SUM(price)"));
                button_open_price.setLayoutParams(view_layout);
                setBudgetListener(button_open_price);          
               
                tr.addView(store_name);
                tr.addView(button_inform);
                tr.addView(button_open_price);
                list_layout.addView(tr);
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
		
		/*
		JSONObject jsonObject;
    	try{
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.store_name FROM store A1,`group` A2 WHERE A2.user_id=" + user_id + " AND A2.store_id=A1.store_id")).getJSONObject(0);
    		open_store_name1.setText(jsonObject.getString("store_name"));
    		
    		jsonObject =new JSONArray(DBConnector.executeQuery("SELECT SUM(A1.price) FROM user_group A1,`group` A2 WHERE A2.user_id=" + user_id + " AND A1.group_id=A2.group_id")).getJSONObject(0);
    		button_open_price1.setText(jsonObject.getString("SUM(A1.price)"));  
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.store_name FROM store A1,`group` A2 WHERE A2.user_id=" + user_id + " AND A2.store_id=A1.store_id")).getJSONObject(0);
    		my_store_name1.setText(jsonObject.getString("store_name"));    		
    		
    		jsonObject =new JSONArray(DBConnector.executeQuery("SELECT A4.user_account FROM store A1, user_group A2, `group` A3,user A4 WHERE A2.user_id =" + user_id + " AND A2.group_id = A3.group_id AND A3.user_id = A4.user_id")).getJSONObject(0);
    		account1.setText(jsonObject.getString("user_account"));
    		
    		jsonObject =new JSONArray(DBConnector.executeQuery("SELECT price FROM user_group WHERE user_id =" + user_id)).getJSONObject(0);
    		button_my_price1.setText(jsonObject.getString("price"));
  		
    	}
    	catch (JSONException e) {
    	 	// TODO Auto-generated catch block
    	 	e.printStackTrace();
    	 	}   	*/
    }
	
	private void withQuery(){
		String result = DBConnector.executeQuery("SELECT * FROM user_group A1,`group` A2,user A3,store A4 WHERE A1.user_id ="+user_id+" AND A2.store_id=A4.store_id AND A2.group_id = A1.group_id AND A3.user_id=A2.user_id ORDER BY A1.user_group_id ASC");
		//String sum = DBConnector.executeQuery("SELECT SUM(price) FROM user_group A1,`group` A2 WHERE A2.user_id=" + user_id + " GROUP BY A2.group_id");
		TableLayout list_layout = (TableLayout) findViewById(R.id.with_list);
		list_layout.setStretchAllColumns(true);
		
		 //Log.v("result", result);
		
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(MyOrder.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                
                /*Bundle budget_data = new Bundle();
                budget_data.putInt("open_account", jsonData.getInt("store_id"));
                budget_data.putString("store_name", jsonData.getString("store_name"));
                budget_data.putString("store_tel", jsonData.getString("store_tel"));
                budget_data.putString("store_addr", jsonData.getString("store_addr"));
                budget_data.putString("store_menu", jsonData.getString("store_menu"));*/
                
                TextView store_name = new TextView(this);
                store_name.setText(jsonData.getString("store_name"));
                store_name.setLayoutParams(view_layout);
                
                TextView account = new TextView(this);
                account.setText(jsonData.getString("user_account"));
                account.setLayoutParams(view_layout);
                
                Button button_price = new Button(this);
                button_price.setText(jsonData.getString("price"));
                button_price.setLayoutParams(view_layout);
                setBudgetListener(button_price);            
 
                tr.addView(store_name);
                tr.addView(account);
                tr.addView(button_price);
                list_layout.addView(tr);
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
	}
    
	private void setInformListener(Button btn,final int id) {
		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v){
				DBConnector.executeUpdate("UPDATE `group` SET inform=1 WHERE group_id=" + id);	
				Toast.makeText(MyOrder.this,"已送出通知", Toast.LENGTH_SHORT).show();
			}
		});			    
    }
	
	private void setBudgetListener(Button btn) {
		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v){
	    		Intent intent = new Intent();
	    		intent.setClass(MyOrder.this, Budget.class);  
				Bundle bundle = new Bundle();
				bundle.putInt("user_id", user_id);
				intent.putExtras(bundle);
	    		startActivity(intent);		
			}
		});			    
    }      
}