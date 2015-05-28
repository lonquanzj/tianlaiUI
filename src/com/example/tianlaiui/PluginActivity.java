package com.example.tianlaiui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.lu.demo.annotation.ContentView;
import com.lu.demo.annotation.ViewInjectUtil;

@SuppressWarnings("deprecation")
@ContentView(value = R.layout.activity_main)
public class PluginActivity extends TabActivity{

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intiTabHost();
	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void intiTabHost() {
		ViewInjectUtil.inject(this);
		mTabHost = getTabHost();
		
		
		mTabHost.addTab(mTabHost.newTabSpec("������").setIndicator("������")
				.setContent(new Intent(this,HunXiangQiActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("�������").setIndicator("�������")
				.setContent(new Intent(this,RenShengBianDiaoActivity.class)));
//
		mTabHost.addTab(mTabHost.newTabSpec("��������").setIndicator("��������")
				.setContent(new Intent(this,RenShengJunHengQiActivity.class)));
		 mTabHost.addTab(mTabHost.newTabSpec("���־���").setIndicator("���־���")
		 .setContent(new Intent(this,JunHengQiActivity.class)));
		mTabHost.setCurrentTab(0);

	}

}
