package com.example.tianlaiui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInject;
import com.lu.demo.util.DialogUtil;
import com.lu.demo.util.LogUtil;

@ContentView(value = R.layout.hunxiangqi)
public class HunXiangQiActivity extends BaseActivity {





	@ViewInject(value = R.id.sb_yanchi)
	private SeekBar sb_yanchi;
	@ViewInject(value = R.id.tv_yanchi)
	private TextView tv_yanchi;
	
	@ViewInject(value = R.id.sb_canxiang)
	private SeekBar sb_canxiang;
	@ViewInject(value = R.id.tv_canxiang)
	private TextView tv_canxiang;
	
	@ViewInject(value = R.id.sb_ganshi)
	private SeekBar sb_ganshi;
	@ViewInject(value = R.id.tv_ganshi)
	private TextView tv_ganshi;
	
	
	
	@ViewInject(value = R.id.sb_kongjian)
	private SeekBar sb_kongjian;
	@ViewInject(value = R.id.tv_kongjian)
	private TextView tv_kongjian;
	
	@ViewInject(value = R.id.sb_kuosan)
	private SeekBar sb_kuosan;
	@ViewInject(value = R.id.tv_kuosan)
	private TextView tv_kuosan;
	
	@ViewInject(value = R.id.sb_shuchu)
	private SeekBar sb_shuchu;
	@ViewInject(value = R.id.tv_shuchu)
	private TextView tv_shuchu;

	private List<SeekBar> listHunXiangQiSeekBars;
	private List<TextView> listTextViews= new ArrayList<TextView>();

	@Override
	protected void initList() {
		list = listAddItem(getResources()
				.getStringArray(R.array.set_HunXiangQi), imgsNormal);
		this.currentSetRes = R.id.tv_hunXiangQiCurrentSet;
		this.gridViewRes = R.id.gridview_hunxiangqi;
	};

	@Override
	protected void initListener() {
		sb_ganshi.setOnSeekBarChangeListener(this);
		sb_canxiang.setOnSeekBarChangeListener(this);
		sb_shuchu.setOnSeekBarChangeListener(this);
		sb_yanchi.setOnSeekBarChangeListener(this);
		sb_kongjian.setOnSeekBarChangeListener(this);
		sb_kuosan.setOnSeekBarChangeListener(this);

		listHunXiangQiSeekBars = new ArrayList<SeekBar>();
		listHunXiangQiSeekBars.add(sb_yanchi);
		listHunXiangQiSeekBars.add(sb_canxiang);
		
		listHunXiangQiSeekBars.add(sb_ganshi);
		listHunXiangQiSeekBars.add(sb_kongjian);
		listHunXiangQiSeekBars.add(sb_kuosan);
		listHunXiangQiSeekBars.add(sb_shuchu);
		
		listTextViews.add(tv_yanchi);
		listTextViews.add(tv_canxiang);
		listTextViews.add(tv_ganshi);
		listTextViews.add(tv_kongjian);
		listTextViews.add(tv_kuosan);
		listTextViews.add(tv_shuchu);
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		LogUtil.i(this.getClass(), "fromUser=" + fromUser);
		//������û������Ļ�ı们��ֵ
		if(!fromUser){
//			matchYuShe();
			return;
		}
		
			switch (seekBar.getId()) {
			case R.id.sb_yanchi:
				tv_yanchi.setText(progress + "ms");
				sendHunXiangQi(10, progress);
				break;
			case R.id.sb_canxiang:
				tv_canxiang.setText(progress + "ms");
				sendHunXiangQi(11, progress);
				break;
			case R.id.sb_ganshi:
				sendHunXiangQi(13, progress);
				
				tv_ganshi.setText(progress +"");
				break;
			case R.id.sb_kongjian:
				sendHunXiangQi(14, progress);
				tv_kongjian.setText(progress +"");
				break;
			case R.id.sb_kuosan:
				sendHunXiangQi(15, progress);
				tv_kuosan.setText(progress +"");
				break;
			case R.id.sb_shuchu:
				sendHunXiangQi(16, progress);
				tv_shuchu.setText(progress +"");
				break;
			}
	}
	/**
	 * ƥ��Ԥ���ֵ
	 */
//	private void matchYuShe(){
//		//��ȡ���л�������ֵ
//		int[] temp=new int[10];
//		for(int i=0;i<temp.length;i++){
//			temp[i]=listHunXiangQiSeekBars.get(i).getProgress();
//		}
//		
//		
//		//�Ƿ�������������
//		
//		int result=findSet(yuSheDate,temp);
//		LogUtil.i(this.getClass(), "result="+result);
//		//�������������
//		if(result!=-1){
//			gridView.setSelection(result);
//			for(int i=0;i<list.size(); i++){
//				if(i==result){
//					list.get(i).isSelected=true;
//				}else{
//					list.get(i).isSelected=false;
//				}
//				
//			}
//			
//			currentSet.setText("��ǰ���� ��" + list.get(result).name);
//			luAdapter.notifyDataSetChanged();
//		}else{
//			for(Item item:list){
//				item.isSelected=false;
//			}
//			currentSet.setText("��ǰ���� ��");
//			luAdapter.notifyDataSetChanged();
//		}
//	}
//	private int findSet(int[][] set,int[] a){
//		int result=-1;
//		boolean flag=false;
//		for(int i=0;i<set.length;i++){
//			flag=false;
//			for(int j=0;j<set[i].length;j++){
//				if(a[j]!=set[i][j]){
//					flag=false;
//					break;
//				}else{
//					flag=true;
//				}
//			}
//			if(flag){
//				result=i;
//				break;
//			}
//		}
//		return result;
//	}
	private void sendHunXiangQi(int i, int progress) {
//		if (!isUsbConnection()) {
//			return;
//		}
		
		usbDevicesUtil.sendParamterDataNew(i, progress);
		
	}
	public void save(View view){
		byte[] data=new byte[6];
		for(int i=0;i<data.length;i++){
			data[i]=(byte) listHunXiangQiSeekBars.get(i).getProgress();
		}
		usbDevicesUtil.saveHunXiangData(data);
		DialogUtil.showToast(this, "��ǰ���ñ���ɹ�");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		updateUI();
	}
	@Override
	protected void notifyDataSetChanged(int position) {
//		setHunXiangQi(position);
		
		usbDevicesUtil.sendHunXiangQiYuSheData(position);
		updateUI();
	}

	@Override
	protected void updateUI() {
		byte[] date = usbDevicesUtil.getCurrentHunXiangData();
		for (int i = 0; i < 6; i++) {
			listHunXiangQiSeekBars.get(i).setProgress(date[i]);
			listTextViews.get(i).setText(""+date[i]);
		}

	}

}
