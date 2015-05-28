package com.example.tianlaiui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInject;
import com.lu.demo.util.DialogUtil;
import com.lu.demo.util.UsbDeviceConstant;
import com.lu.demo.util.UsbDevicesUtil;

@ContentView(value=R.layout.yinxiao)
public class YinXiaoActivity extends BaseActivity{
	/** ��ЧͼƬ */
	private int[] imgsYinXiao = {  R.drawable.zdy_normal,
			R.drawable.zdy_normal, R.drawable.zdy_normal,
			R.drawable.zdy_normal, R.drawable.zdy_normal,
			R.drawable.zdy_normal, R.drawable.zdy_normal,
			R.drawable.zdy_normal,R.drawable.zdy_normal, R.drawable.zdy_normal,  };

	
	@ViewInject(value = R.id.sb_banzou)
	private SeekBar sb_banzou;
	@ViewInject(value = R.id.sb_huatong)
	private SeekBar sb_huatong;
	
	@ViewInject(value = R.id.sb_mainVoice)
	private SeekBar sb_mainVoice;
	 
	
	
	@ViewInject(value = R.id.tv_voice)
	private TextView tv_voice;

	@ViewInject(R.id.tv_banzou)
	private TextView tv_banzou;
	
	@ViewInject(R.id.tv_mainVoice)
	private TextView tv_mainVoice;
	
	private UsbDevicesUtil usbDevicesUtil;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UsbDeviceConstant.DEVICE_CONNECTION_SUCCESS:
//				DialogUtil.showToast(getApplicationContext(), "�豸���ӳɹ�");
				currentSet.setText("�豸���ӳɹ�");
				break;
			case UsbDeviceConstant.DEVICE_CONNECTION_FAIL:
//				DialogUtil.showToast(getApplicationContext(), "�豸����ʧ��");
				currentSet.setText("�豸����ʧ��");
				break;
				
			case UsbDeviceConstant.ACTION_USB_DEVICE_ATTACHED:
//				DialogUtil.showToast(getApplicationContext(), "����豸�Ѳ���");
				currentSet.setText("����豸�Ѳ���,����������...");
				usbDevicesUtil.getPermissionConnection();
				break;
			case UsbDeviceConstant.ACTION_USB_DEVICE_DETACHED :
//				DialogUtil.showToast(getApplicationContext(), "����豸��ɾ��");
				currentSet.setText("����豸��ɾ��");
				usbDevicesUtil.release();
				break;
			default:
				break;
			}

		}

	};
	
	@Override
	protected void initList(){
		list = listAddItem(getResources()
				.getStringArray(R.array.yinxiao), imgsYinXiao);
		this.currentSetRes=R.id.tv_currentYinXiao;
		this.gridViewRes=R.id.gridview;
		
		usbDevicesUtil =  UsbDevicesUtil.getInstance(this, handler);
		usbDevicesUtil.getPermissionConnection();
	};
	@Override
	protected void initListener() {
		sb_huatong.setOnSeekBarChangeListener(this);
		sb_banzou.setOnSeekBarChangeListener(this);
		sb_mainVoice.setOnSeekBarChangeListener(this);
		
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		
		case R.id.sb_huatong:
			tv_voice.setText(String.valueOf(seekBar.getProgress()));
			sendVoice(0, seekBar.getProgress());
			break;
		case R.id.sb_banzou:
			tv_banzou.setText(String.valueOf(progress));
			sendVoice(1, seekBar.getProgress());
			break;
		case R.id.sb_mainVoice:
			tv_mainVoice.setText(String.valueOf(progress));
			sendVoice(2, seekBar.getProgress());
			break;
		default:
			break;
		}
		
	}
	private void sendVoice(int i, int progress) {
//		if(!isUsbConnection()){
//			return ; 
//			
//		}	
		try{
			usbDevicesUtil.sendParamterDataNew(i, progress);
		}catch(Exception e){
			DialogUtil.showToast(this, "�����쳣");
		}
		
		 
	}
	@Override
	protected void notifyDataSetChanged(int position){
		
		if(position==list.size()-1){
			startActivity(new Intent(this,PluginActivity.class));
			return;
		}
//		
//		if(!isUsbConnection()){
//			return ; 
//		}
		currentSet.setText("��ǰ��Ч ��" + list.get(position).name);
//		try{
		if(position>6){
			position=6;
		}
		usbDevicesUtil.sendEffectDataNew(position);
		
//		DataUtil.currentEffect=position;
//			usbDevicesUtil.setCurrentEffect(position);
//		}catch(Exception e){
//			DialogUtil.showToast(this, "�����쳣");
//		}
		
	 
	}
	@Override 
	protected void updateUI() {
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(usbDevicesUtil!=null){
			usbDevicesUtil.unregisterReceiver();
		}
		
	}
}
