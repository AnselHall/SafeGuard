package com.safe.safeguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safe.safeguard.R;

public class SetUp3Activity extends SetBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	@Override
	public void next_activity() {
		Intent intent = new Intent(this,SetUp4Activity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void pre_activity() {
		Intent intent = new Intent(this,SetUp2Activity.class);
		startActivity(intent);
		finish();		
	}
}
