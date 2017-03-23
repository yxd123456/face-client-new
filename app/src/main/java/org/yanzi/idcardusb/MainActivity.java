package org.yanzi.idcardusb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.yanzi.grg.idcard.IDCardMsg;
import org.yanzi.grg.idcard.IDCardRecognition;
import org.yanzi.playcamera.R;


public class MainActivity extends Activity {
	private final String TAG = "TestIDCardActivity";
	private TextView textView;
	private ImageView ivPortriat;
	
	private IDCardRecognition mIDCardRecognition;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testidcard);
		findViewById(R.id.testidcard_connect).setOnClickListener(mOncClickListener);
		findViewById(R.id.testidcard_getidinfo).setOnClickListener(mOncClickListener);
		textView = (TextView)findViewById(R.id.testidcard_text);
		ivPortriat = (ImageView)findViewById(R.id.testidcard_img);
	}
	
	@Override
	protected void onDestroy() {
		mIDCardRecognition.close();
		super.onDestroy();
	}
	
	private OnClickListener mOncClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.testidcard_connect:
				findViewById(R.id.testidcard_connect).setEnabled(false);
				mIDCardRecognition = new IDCardRecognition(MainActivity.this, mIDCardRecListener);
				mIDCardRecognition.start();
				break;
				
			case R.id.testidcard_getidinfo:
				mIDCardRecognition.close();
				findViewById(R.id.testidcard_connect).setEnabled(true);
				break;
 
			default:
				break;
			}
			
		}
	};
	
	private IDCardRecognition.IDCardRecListener mIDCardRecListener = new IDCardRecognition.IDCardRecListener() {

		@Override
		public void onResp(IDCardMsg info) {
			if(info == null) {
				textView.setText("");
				ivPortriat.setImageBitmap(null);

				return;
			}
			String text = info.getName() + "\n"
					+ info.getIdCardNum() + "\n"
					+ info.getAddress() + "\n"
					+ info.getSex() + "\n";
			textView.setText(text);
			Bitmap bitmap = BitmapFactory.decodeByteArray(info.getPortrait(), 0, info.getPortrait().length);
			ivPortriat.setImageBitmap(bitmap);
		}
	};

	
	
	

}
