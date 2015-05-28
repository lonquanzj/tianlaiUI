package com.example.tianlaiui;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInject;
import com.lu.demo.annotation.ViewInjectUtil;
import com.lu.demo.util.DialogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@ContentView(value=R.layout.activity_usbreadwritemain)
public class LuYinActivity extends Activity{
	
	@ViewInject(value = R.id.tv_message)
	private TextView tv_message;

	@ViewInject(value = R.id.btn_receivedMusicData)
	private Button btn_receivedMusicData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ViewInjectUtil.inject(this);
		
		
	}
	
	// Â¼Òô
		private boolean isRecord = false;

		public void receivedMusicData(View view) {

//			if (!usbDevicesUtil.isConnection()) {
				DialogUtil.showToast(this, "Éè±¸Îª¿Õ");
//				return;
//			}
//
//			if (!isRecord) {
//				usbDevicesUtil.startRecord();
//				btn_receivedMusicData.setText("Í£Ö¹Â¼Òô");
//			} else {
//				usbDevicesUtil.stopRecord();
//				btn_receivedMusicData.setText("¿ªÊ¼Â¼Òô");
//			}
			isRecord = !isRecord;
		}
}
