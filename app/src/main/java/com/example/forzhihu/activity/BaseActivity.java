package com.example.forzhihu.activity;

import com.example.forzhihu.R;
import com.example.forzhihu.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity{
	private Button bt_menu, bt_set;
	private TextView tx_title;
	protected abstract int setContextView();
	protected abstract OnClickListener setMenuOnclick();
	protected abstract OnClickListener setSetOnclick();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.transStatus(this);
		setContentView(setContextView());
		initView();
	}

	private void initView() {
		Utils.Log("initView");
		bt_menu = (Button) findViewById(R.id.bt_title_menu);
		bt_menu.setOnClickListener(this.setMenuOnclick());

		bt_set = (Button) findViewById(R.id.bt_title_set);
		bt_set.setOnClickListener(this.setSetOnclick());

		tx_title = (TextView) findViewById(R.id.tx_title);
	}
	public void setTitlename(String name){
		tx_title.setText(name);
	}
	
	public void setVisble(int visibility){
		bt_set.setVisibility(visibility);
	}
	
}
