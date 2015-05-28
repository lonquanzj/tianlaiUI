package com.example.tianlaiui;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInject;
import com.lu.demo.util.DialogUtil;

@ContentView(value = R.layout.renshengbiandiaoactivity)
public class RenShengBianDiaoActivity extends BaseActivity {
	

	@ViewInject(value = R.id.sb_renshengbiandiao)
	private SeekBar sb_renshengbiandiao;
	@ViewInject(value = R.id.tv_renshengbiandiao)
	private TextView tv_renshengbiandiao;


//	private List<SeekBar> listJunHengQiSeekBars;

	protected void setJunHengQi(int position) {
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		
		//������û������Ļ�ı们��ֵ
		if(!fromUser){
			
			//��ǰ���л���ֵ�Ƿ��Ӧ����
			
			//��Ӧ����,����Ϊѡ��״̬
			
			//û�ж�Ӧ����,���ò�ѡ��
			return;
		}
		

		switch (seekBar.getId()) {
		case R.id.sb_renshengbiandiao:
			sendJunHengQi(20, seekBar.getProgress());
			tv_renshengbiandiao.setText(String.valueOf(seekBar.getProgress()));
			break;
		}

	}

	/**
	 * ���;���������
	 * 
	 * @param index
	 * @param progress
	 */
	private void sendJunHengQi(int index, int progress) {

//		if (!isUsbConnection()) {
//			return;
//		}
		usbDevicesUtil.sendParamterDataNew(index, progress);
	}

	@Override
	protected void initList() {
		list = listAddItem(
				getResources().getStringArray(R.array.set_RenShengBianDiao),
				imgsNormal);
		this.currentSetRes = R.id.tv_junHengQiCurrentSet;
		this.gridViewRes = R.id.gridview_renshengbiandiao;
	}

	@Override
	protected void initListener() {
		sb_renshengbiandiao.setOnSeekBarChangeListener(this);

//		listJunHengQiSeekBars = new ArrayList<SeekBar>(10);

	}

	@Override
	protected void notifyDataSetChanged(int position) {
		
		usbDevicesUtil.sendRenShengBiaoDiaoYuSheDate(position);
		updateUI();
	}
	public void save(View view){
		DialogUtil.showToast(this, "��ǰ���ñ���ɹ�");
	}
	@Override
	protected void updateUI() {

		byte[] data = usbDevicesUtil.getCurrentRenShengData();
		sb_renshengbiandiao.setProgress(data[0]);
		tv_renshengbiandiao.setText(""+data[0]);
	}

}
