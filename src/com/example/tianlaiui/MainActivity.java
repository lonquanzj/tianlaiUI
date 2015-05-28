package com.example.tianlaiui;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TabHost;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInjectUtil;
import com.lu.demo.util.DialogUtil;
import com.lu.demo.util.UsbDeviceConstant;
import com.lu.demo.util.UsbDevicesUtil;

@SuppressLint({ "NewApi", "ResourceAsColor" })
@SuppressWarnings("deprecation")
@ContentView(value = R.layout.activity_main)
public class MainActivity extends TabActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	private TabHost mTabHost;
	UsbDevicesUtil usbDevicesUtil;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UsbDeviceConstant.DEVICE_CONNECTION_SUCCESS:
				DialogUtil.showToast(getApplicationContext(), "�豸���ӳɹ�");
				break;
			case UsbDeviceConstant.DEVICE_CONNECTION_FAIL:
				DialogUtil.showToast(getApplicationContext(), "�豸����ʧ��");
				break;
				
			case UsbDeviceConstant.ACTION_USB_DEVICE_ATTACHED:
				DialogUtil.showToast(getApplicationContext(), "����豸�Ѳ���");
				usbDevicesUtil.getPermissionConnection();
				break;
			case UsbDeviceConstant.ACTION_USB_DEVICE_DETACHED :
				DialogUtil.showToast(getApplicationContext(), "����豸��ɾ��");
				usbDevicesUtil.release();
				break;
			default:
				break;
			}

		}

	};

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		usbDevicesUtil =  UsbDevicesUtil.getInstance(this, handler);
		intiTabHost();
	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		usbDevicesUtil.unregisterReceiver();
	}

	private void intiTabHost() {
		ViewInjectUtil.inject(this);
		mTabHost = getTabHost();
		
		mTabHost.addTab(mTabHost.newTabSpec("��Ч").setIndicator("��Ч")
				.setContent(new Intent(this,YinXiaoActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("������").setIndicator("������")
				.setContent(new Intent(this,JunHengQiActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("������").setIndicator("������")
				.setContent(new Intent(this,HunXiangQiActivity.class)));

		mTabHost.addTab(mTabHost.newTabSpec("����").setIndicator("����")
				.setContent(new Intent(this,ShanBiActivity.class)));
		 mTabHost.addTab(mTabHost.newTabSpec("��������").setIndicator("��������")
		 .setContent(new Intent(this,LuYinActivity.class)));
		mTabHost.setCurrentTab(0);

	}

}
