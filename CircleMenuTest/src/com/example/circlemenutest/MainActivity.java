package com.example.circlemenutest;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.pan.tanglang.circlemenu.model.CircleMenuStatus;
import com.pan.tanglang.circlemenu.view.CircleMenu;
import com.pan.tanglang.circlemenu.view.CircleMenu.OnMenuItemClickListener;
import com.pan.tanglang.circlemenu.view.CircleMenu.OnMenuStatusChangedListener;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

	private String[] mItemTexts = new String[] { "安全中心", "特殊服务", "投资理财", "转账汇款", "我的账户", "信用卡", "腾讯", "阿里", "百度" };
	private int[] mItemImgs = new int[] { R.drawable.foreign01, R.drawable.foreign02, R.drawable.foreign03,
			R.drawable.foreign04, R.drawable.foreign05, R.drawable.foreign06, R.drawable.foreign07,
			R.drawable.foreign08, R.drawable.foreign09 };

	private CircleMenu mCircleMenu;
	private ImageView ivCenter;

	private float startRotate;
	private float startFling;

	ObjectAnimator animRotate = null;
	ObjectAnimator animFling = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCircleMenu = (CircleMenu) findViewById(R.id.cm_main);
		ivCenter = (ImageView) findViewById(R.id.iv_center_main);
		ivCenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "圆盘中心", Toast.LENGTH_SHORT).show();

			}
		});
		mCircleMenu.setOnItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onClick(View view, int position) {
				Toast.makeText(MainActivity.this, mItemTexts[position], Toast.LENGTH_SHORT).show();
			}
		});
		mCircleMenu.setOnStatusChangedListener(new OnMenuStatusChangedListener() {

			@Override
			public void onStatusChanged(CircleMenuStatus status, double rotateAngle) {
				// TODO 可在此处定制各种动画
				odAnimation(status, (float)rotateAngle);
			}

		});
		List<ItemInfo> data = new ArrayList<>();
		ItemInfo item = null;
		for (int i = 0; i < mItemTexts.length; i++) {
			item = new ItemInfo(mItemImgs[i], mItemTexts[i]);
			data.add(item);
		}

		mCircleMenu.setAdapter(new CircleMenuAdapter(data));

	}

	private void odAnimation(CircleMenuStatus status, float rotateAngle) {

		switch (status) {
		case IDLE:
			Log.i(TAG, "--- -IDLE-----");
			animRotate.cancel();
			animRotate.cancel();
			break;
		case START_ROTATING:
			Log.i(TAG, "--- -START_ROTATING-----");
			break;
		case ROTATING:
			animRotate = ObjectAnimator.ofFloat(ivCenter, "rotation", startRotate, startRotate + rotateAngle);
			animRotate.setDuration(200).start();
			startRotate += rotateAngle;
			// Log.i(TAG, "--- -ROTATING-----");
			break;
		case STOP_ROTATING:
			Log.i(TAG, "--- -STOP_ROTATING-----");
			break;
		case START_FLING:
			Log.i(TAG, "--- -START_FLING-----");
			break;

		case FLING:
			// Log.i(TAG, "--- -FLING-----");
			animFling = ObjectAnimator.ofFloat(ivCenter, "rotation", startFling, startFling + rotateAngle);
			animFling.setDuration(200).start();
			startFling += rotateAngle;
			break;
		case STOP_FLING:
			Log.i(TAG, "--- -STOP_FLING-----");

			break;

		default:
			break;
		}

	}
}
