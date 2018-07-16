package com.tuacy.annotationlearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tuacy.annotationlearning.annotation.process.AutoWired;
import com.tuacy.annotationlearning.annotation.process.AutoWiredProcess;
import com.tuacy.annotationlearning.annotation.butterknife.BindString;
import com.tuacy.annotationlearning.annotation.butterknife.BindView;
import com.tuacy.annotationlearning.annotation.butterknife.OnClick;
import com.tuacy.annotationlearning.annotation.butterknife.ButterKnifeProcess;

public class MainActivity extends AppCompatActivity {

	//自动绑定view
	@BindView(R.id.text_abstract_processor)
	TextView mTextView;

	//自动绑定String
	@BindString(R.string.click_already)
	String mInfo;

	//自动创建对象
	@AutoWired
	UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnifeProcess.bind(this);
		AutoWiredProcess.bind(this);
		initData();
	}

	@OnClick({R.id.button_click})
	public void buttonOnCLick(View view) {
		mTextView.setText(mInfo);
	}

	private void initData() {
		// 我们是没有显示的去new对象的，通过@AutoCreate注解来完成new
		mUserInfo.setName("AutoWired");
	}
}
