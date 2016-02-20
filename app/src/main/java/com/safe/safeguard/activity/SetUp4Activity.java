package com.safe.safeguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.safe.safeguard.R;

public class SetUp4Activity extends SetBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	}
	@Override
	public void next_activity() {
		
	}
	@Override
	public void pre_activity() {
		Intent intent = new Intent(this,SetUp3Activity.class);
		startActivity(intent);
		finish();
	}
	
}
