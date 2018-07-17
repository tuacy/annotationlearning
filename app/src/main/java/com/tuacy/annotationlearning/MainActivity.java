package com.tuacy.annotationlearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tuacy.annotationlearning.annotation.abstractprocessor.People;
import com.tuacy.annotationlearning.annotation.abstractprocessor.PeopleFactory;
import com.tuacy.annotationlearning.annotation.autowired.AutoWired;
import com.tuacy.annotationlearning.annotation.autowired.AutoWiredProcess;
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

	//自动创建对象，不用我们去new UserInfo()了
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

	/**
	 * 绑定点击事件
	 */
	@OnClick({R.id.button_click})
	public void buttonOnCLick(View view) {
		mTextView.setText(mInfo);
	}

	private void initData() {
		// 我们是没有显示的去new对象的，通过@AutoCreate注解来完成new
		mUserInfo.setName("AutoWired");

		// 测试下工厂类代码自动生成是否成功
		PeopleFactory peopleFactory = new PeopleFactory();
		People people = peopleFactory.create("Female");
		Log.d("tuacy", people.getName());
	}
}
