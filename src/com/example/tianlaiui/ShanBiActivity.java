package com.example.tianlaiui;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.OnClick;
import com.lu.demo.annotation.ViewInject;
import com.lu.demo.util.DialogUtil;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

@ContentView(value=R.layout.shanbi)
public class ShanBiActivity extends BaseActivity{
	
	@ViewInject(value = R.id.sb_shanbimenxian)
	private SeekBar sb_shanbimenxian;
	@ViewInject(value = R.id.sb_shanbispeed)
	private SeekBar sb_shanbispeed;
	@ViewInject(value = R.id.sb_shanbilevel)
	private SeekBar sb_shanbilevel;

	@ViewInject(value = R.id.btn_shanbi_setting)
	private ImageButton btn_shanbi_setting;
	@ViewInject(value = R.id.btn_shanbi_reset)
	private ImageButton btn_shanbi_reset;
	
	@ViewInject(value = R.id.tv_shanbimx)
	private TextView tv_shanbimx;
	@ViewInject(value = R.id.tv_shanbi_speed)
	private TextView tv_shanbi_speed;
	@ViewInject(value = R.id.tv_shanbi_level)
	private TextView tv_shanbi_level;
	
	
	@ViewInject(R.id.cb_shanbi)
	private CheckBox cb_shanbi;
	

	/**
	 * …Ë÷√…¡±‹≈‰÷√
	 * 
	 * @param index
	 */
	private void setShanBi(int index) {
		tv_shanbi_level.setText("0");
		tv_shanbi_speed.setText("0");
		tv_shanbimx.setText("0");
		sb_shanbilevel.setProgress(0);
		sb_shanbimenxian.setProgress(0);
		sb_shanbispeed.setProgress(0);

		switch (index) {
		case 0:
			tv_shanbi_level.setText("55");
			sb_shanbilevel.setProgress(55);
			break;
		case 1:
			tv_shanbi_speed.setText("33");
			sb_shanbispeed.setProgress(33);
			break;
		case 2:
			tv_shanbimx.setText("66");
			sb_shanbimenxian.setProgress(66);
			break;

		default:
			break;
		}
	}
	
	@OnClick({ R.id.btn_shanbi_reset, R.id.btn_shanbi_setting })
	public void clickBtnInvoked(View view) {

		switch (view.getId()) {
		case R.id.btn_shanbi_reset:
			break;
		case R.id.btn_shanbi_setting:
			showShanBiDialog();
			break;
		default:
			break;
		}
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.sb_shanbimenxian:
			tv_shanbimx.setText(String.valueOf(seekBar.getProgress()));
			sendShanBi(1, seekBar.getProgress());
			break;
		case R.id.sb_shanbispeed:
			tv_shanbi_speed.setText(String.valueOf(seekBar.getProgress()));
			sendShanBi(2, seekBar.getProgress());
			break;
		case R.id.sb_shanbilevel:
			tv_shanbi_level.setText(String.valueOf(seekBar.getProgress()));
			sendShanBi(3, seekBar.getProgress());
			break;
		}
		
	}
	private void showShanBiDialog() {
		DialogUtil.showAlertDialog(this, "…¡±‹‘§…Ë", R.array.set_ShanBi, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setShanBi(which);
				DialogUtil.closeAlertDialog();
			}
		});
	}
	/**
	 * ∑¢ÀÕ…¡±‹ ˝æ›
	 * 
	 * @param index
	 * @param progress
	 */
	private void sendShanBi(int index, int progress) {
		
		if(!isUsbConnection()){
			return ;
		}	
//		usbDevicesUtil.sendParameterData(4, index, progress);
	}
	@Override
	protected void initList() {
		
	}
	@Override
	protected void isShanBi() {
		isShanBi=true;
	};
	@Override
	protected void initListener() {
		sb_shanbilevel.setOnSeekBarChangeListener(this);
		sb_shanbimenxian.setOnSeekBarChangeListener(this);
		sb_shanbispeed.setOnSeekBarChangeListener(this);
		
		cb_shanbi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(!isUsbConnection()){
					return ;
				}
				
				if(isChecked){
					cb_shanbi.setText("ø™");
				}else{
					cb_shanbi.setText("πÿ");
				}
				
			}
		});
		
	}

	@Override
	protected void notifyDataSetChanged(int position) {
		
	}

	@Override
	protected void updateUI() {
		
	}
}
