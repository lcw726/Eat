package mis.eat;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreInfoActivity extends Activity {

	private Bundle store_data;
	public final static String image_url = "http://140.120.55.94/AndroidProject/images/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_info);

		findViews();
		setListeners();

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork() // or .detectAll() for all detectable problems
		.penaltyLog()
		.build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects()
		.penaltyLog()
		.penaltyDeath()
		.build());

		Bundle bundle = getIntent().getExtras();
		store_data = bundle.getBundle("store_data");
		
		setStoreInfo();
	}

	private TextView store_name_textView;
	private TextView store_tel_textView;
	private TextView store_addr_textView;
	private ImageView menu_imageView;
	private Button open_button;
	private Button with_button;

	private void findViews() {
		store_name_textView = (TextView) findViewById(R.id.store_name_textView);
		store_tel_textView = (TextView) findViewById(R.id.store_tel_textView);
		store_addr_textView = (TextView) findViewById(R.id.store_add_textView);
		menu_imageView = (ImageView) findViewById(R.id.menu_imageView);
		open_button = (Button) findViewById(R.id.open_button);
		with_button = (Button) findViewById(R.id.with_button);
	}

	private void setListeners() {
		open_button.setOnClickListener(click_open);
		with_button.setOnClickListener(click_with);
		
	}

	private Button.OnClickListener click_open = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(StoreInfoActivity.this, NewOpenActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBundle("store_data", store_data);
			bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
			Log.v("user-id", Integer.toString(getIntent().getExtras().getInt("user_id")));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
	
	private Button.OnClickListener click_with = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(StoreInfoActivity.this, OpenListActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBundle("store_data", store_data);
			bundle.putInt("user_id", getIntent().getExtras().getInt("user_id"));
			intent.putExtras(bundle);
			startActivity(intent);
		}		
	};

	private void setStoreInfo() {
		store_name_textView.setText(store_data.getString("store_name"));
		store_tel_textView.setText(store_data.getString("store_tel"));
		store_addr_textView.setText(store_data.getString("store_addr"));
		menu_imageView.setImageDrawable(getDrawableFromURL(store_data.getString("store_menu")));
	}

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
}
