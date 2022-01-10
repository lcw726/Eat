package mis.eat;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class NewOrderActivity extends Activity {

	private int group_id;
	private int user_id;
	private Bundle store_data;
	public final static String image_url = "http://140.120.55.94/AndroidProject/images/";
	
	private TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	private TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neworder);
		
		Bundle bundle = getIntent().getExtras();
		group_id = bundle.getInt("group_id");
		user_id = bundle.getInt("user_id");
		store_data = bundle.getBundle("store_data");
		
		findViews();
		setListeners();
		
		menu_imageView.setImageDrawable(getDrawableFromURL(store_data.getString("store_menu")));
		
		order_list.setStretchAllColumns(true);
		
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(row_layout);
		tr.addView(createTextView(getString(R.string.item)));
		tr.addView(createTextView(getString(R.string.price)));
		tr.addView(createTextView(getString(R.string.quantity)));
		tr.addView(createTextView(getString(R.string.remark)));
		order_list.addView(tr);
	}

	private TableLayout order_list;
	private ImageView menu_imageView;
	private Button confirm_button;
	private ImageButton list_add_button;
	
	private void findViews() {
		order_list = (TableLayout)findViewById(R.id.new_order_list);
		menu_imageView = (ImageView)findViewById(R.id.menu_image);
		confirm_button = (Button)findViewById(R.id.confrim_button);
		list_add_button = (ImageButton)findViewById(R.id.list_add_button);
	}
	
	private void setListeners() {
		list_add_button.setOnClickListener(add_list);
		confirm_button.setOnClickListener(make_order);
	}
	
	private ImageButton.OnClickListener add_list = new ImageButton.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			order_list.addView(createInputRow());
		}		
	};
	
	private Button.OnClickListener make_order = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int order_count = order_list.getChildCount();
			for(int i = 1; i < order_count; i++) {
				TableRow order = (TableRow)order_list.getChildAt(i);
				String[] params = new String[4];
				for(int j = 0; j < 4; j++) {
					params[j] = ((EditText)order.getVirtualChildAt(j)).getText().toString();
				}
				String update_string = "INSERT INTO user_group VALUES(null, " + group_id + ", " + user_id + ", ^" + params[0] + "^, " + params[1] + ", " + params[2] + ", 0)";
				DBConnector.executeUpdate(update_string);
				//Log.v("update_string", update_string);
			}
			
			Intent intent = new Intent();
			intent.setClass(NewOrderActivity.this, MyOrder.class);
			Bundle bundle = new Bundle();
			bundle.putInt("group_id", group_id);
			bundle.putInt("user_id", user_id);
			intent.putExtras(bundle);
			startActivity(intent);
			//String edit = ((EditText)((TableRow)order_list.getChildAt(1)).getVirtualChildAt(2)).getText().toString();
			//Toast.makeText(NewOrderActivity.this, edit, Toast.LENGTH_SHORT).show();
		}		
	};
	
	private Drawable getDrawableFromURL(String src) {
		InputStream is = null;
		try {
			URL url = new URL(image_url + src);
			Object content = url.getContent();
			is = (InputStream) content;
			Drawable mybitmap = Drawable.createFromStream(is, "store_menu");

			return mybitmap;
		} catch (Exception e) {
			Log.e("log_tag", e.toString());
			return null;
		}
	}
	
	private TextView createTextView(String text) {
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setLayoutParams(view_layout);
		
		return textView;
	}
	
	private TableRow createInputRow() {
		TableRow tableRow = new TableRow(this);
		tableRow.addView(new EditText(this));
		tableRow.addView(new EditText(this));
		tableRow.addView(new EditText(this));
		tableRow.addView(new EditText(this));
		return tableRow;
	}
}
