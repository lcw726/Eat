package mis.eat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Budget extends Activity {
	private int user_id;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget);
        
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
    	String result = DBConnector.executeQuery("SELECT * FROM user_group A1,`group` A2,user A3 WHERE A2.user_id="+user_id+" AND A3.user_id=A1.user_id AND A1.group_id=A2.group_id");
		TableLayout list_layout = (TableLayout) findViewById(R.id.open_list);
		list_layout.setStretchAllColumns(true);
		
		 //Log.v("result", result);
		
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(Budget.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView account = new TextView(this);
                account.setText(jsonData.getString("user_account"));
                account.setLayoutParams(view_layout);
                
                TextView item = new TextView(this);
                item.setText(jsonData.getString("order"));
                item.setLayoutParams(view_layout);
                
                TextView quantity = new TextView(this);
                quantity.setText(jsonData.getString("quantity"));
                quantity.setLayoutParams(view_layout);
                
                TextView price = new TextView(this);
                price.setText(jsonData.getString("price"));
                price.setLayoutParams(view_layout);
                
                CheckBox box_open_ok = new CheckBox(this);
                box_open_ok.setLayoutParams(view_layout);
                setFinishListener(box_open_ok,jsonData.getInt("group_id"));
        		if(jsonData.getInt("paid")==1)
        			box_open_ok.setChecked(true);               
               
                tr.addView(account);
                tr.addView(item);
                tr.addView(quantity);
                tr.addView(price);
                tr.addView(box_open_ok);
                list_layout.addView(tr);
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
    	
    	/*try{
    		JSONArray jsonArray = new JSONArray(result);
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.user_account FROM user A1,user_group A2,`group` A3 WHERE A3.user_id=1 AND A2.group_id = A3.group_id AND A2.user_id=A1.user_id")).getJSONObject(0);
    		open_account1.setText(jsonObject.getString("user_account"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.order FROM user_group A1, `group` A2 WHERE A2.user_id=1 AND A2.group_id=A1.group_id")).getJSONObject(0);
    		open_item1.setText(jsonObject.getString("order"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.quantity FROM user_group A1,`group` A2,user A3 WHERE A2.user_id =1 AND A2.group_id = A1.group_id")).getJSONObject(0);
    		open_quantity1.setText(jsonObject.getString("quantity"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.price FROM user_group A1,`group` A2 WHERE A2.user_id =1 AND A2.group_id = A1.group_id")).getJSONObject(0);
    		open_price1.setText(jsonObject.getString("price"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT paid FROM user_group WHERE user_id =1")).getJSONObject(0);
    		if(jsonObject.getString("paid").equals("1"))
    			box_open_ok.setChecked(true);  		
    		
    		
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT A1.user_account FROM user A1,user_group A2,`group` A3 WHERE A2.user_id=1 AND A2.group_id = A3.group_id AND A3.user_id=A1.user_id")).getJSONObject(0);
    		with_account1.setText(jsonObject.getString("user_account"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT `order` FROM user_group WHERE user_id=1")).getJSONObject(0);
    		with_item1.setText(jsonObject.getString("order"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT quantity FROM user_group WHERE user_id =1")).getJSONObject(0);
    		with_quantity1.setText(jsonObject.getString("quantity"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT price FROM user_group WHERE user_id =1")).getJSONObject(0);
    		with_price1.setText(jsonObject.getString("price"));
    		
    		jsonObject = new JSONArray(DBConnector.executeQuery("SELECT paid FROM user_group WHERE user_id =1")).getJSONObject(0);
    		if(jsonObject.getString("paid").equals("1"))
    			box_with_state.setText("已付清");    		
    	}
    	catch (JSONException e) {
    	 	// TODO Auto-generated catch block
    	 	e.printStackTrace();
    	 }*/
    }
    
    private void withQuery(){
    	String result = DBConnector.executeQuery("SELECT * FROM user_group A1,`group` A2,user A3 WHERE A1.user_id ="+user_id+" AND A1.group_id=A2.group_id AND A2.user_id=A3.user_id ORDER BY A1.user_group_id ASC");
		TableLayout list_layout = (TableLayout) findViewById(R.id.with_list);
		list_layout.setStretchAllColumns(true);
		
		 //Log.v("result", result);
		
		TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		try {
			JSONArray jsonArray = new JSONArray(result);
			for(int i = 0; i < jsonArray.length(); i++) {
			    JSONObject jsonData = jsonArray.getJSONObject(i);
                TableRow tr = new TableRow(Budget.this);
                tr.setLayoutParams(row_layout);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView account = new TextView(this);
                account.setText(jsonData.getString("user_account"));
                account.setLayoutParams(view_layout);
                
                TextView item = new TextView(this);
                item.setText(jsonData.getString("order"));
                item.setLayoutParams(view_layout);
                
                TextView quantity = new TextView(this);
                quantity.setText(jsonData.getString("quantity"));
                quantity.setLayoutParams(view_layout);
                
                TextView price = new TextView(this);
                price.setText(jsonData.getString("price"));
                price.setLayoutParams(view_layout);
                
                TextView with_state = new TextView(this);
                with_state.setLayoutParams(view_layout);                
        		if(jsonData.getInt("paid")==1)
        			with_state.setText("已付清");          
        		else 
        			with_state.setText("未付清");   
               
                tr.addView(account);
                tr.addView(item);
                tr.addView(quantity);
                tr.addView(price);
                tr.addView(with_state);
                list_layout.addView(tr);
            }
		} catch(Exception e) {
			Log.e("log_tag", e.toString());
		}
    }
	
	private void setFinishListener(final CheckBox ckb,final int id) {
		ckb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
	    		if(ckb.isChecked()==true){
	    			DBConnector.executeUpdate("UPDATE user_group SET paid=1 WHERE group_id="+id);
	    		}else{
	    			DBConnector.executeUpdate("UPDATE user_group SET paid=0 WHERE group_id="+id);
	    		}
			}
		});			    
    }
}