package com.safe.safeguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safe.safeguard.R;

public class SetUp2Activity extends SetBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
	}
	@Override
	public void next_activity() {
		Intent intent = new Intent(this,SetUp3Activity.class);
		startActivity(intent);
		finish();		
	}
	@Override
	public void pre_activity() {
		Intent intent = new Intent(this,SetUp1Activity.class);
		startActivity(intent);
		finish();
	}
}
