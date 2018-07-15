package com.tuacy.annotationlearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tuacy.annotationlearning.annotation.AutoCreate;
import com.tuacy.annotationlearning.annotation.process.AutoCreateProcess;
import com.tuacy.annotationlearning.annotation.viewloaf.BindString;
import com.tuacy.annotationlearning.annotation.viewloaf.BindView;
import com.tuacy.annotationlearning.annotation.viewloaf.OnClick;
import com.tuacy.annotationlearning.annotation.viewloaf.ViewLoaf;

public class MainActivity extends AppCompatActivity {

	//自动绑定view
	@BindView(R.id.text_abstract_processor)
	TextView mTextView;

	//自动绑定String
	@BindString(R.string.click_already)
	String mInfo;

	//自动创建对象
	@AutoCreate
	UserInfo mUserInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewLoaf.bind(this);
		AutoCreateProcess.autoCreateInit(this);
		initData();
	}

	@OnClick({R.id.button_click})
	public void buttonOnCLick(View view) {
		mTextView.setText(mInfo);
	}

	private void initData() {
		// 我们是没有显示的去new对象的，通过@AutoCreate注解来完成new
		mUserInfo.setName("AutoCreate");
	}
}
