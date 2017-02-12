package com.pan.tanglang.circlemenu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.pan.tanglang.circlemenu.model.CircleMenuStatus;
import com.pan.tanglang.circlemenu.model.UserEvent;

/**
 * @filename 文件名：CircleItemView.java
 * @description 描 述：Item项自定义控件，主要是为了实现Item的onFling
 * @author 作 者：SergioPan
 * @date 时 间：2017-2-11
 * @Copyright 版 权：塘朗山源代码，版权归塘朗山所有。
 */
public class CircleItemView extends LinearLayout implements OnGestureListener {

	public static final String TAG = "CircleItemView";
	private GestureDetector mDetector;
	private CircleMenu mParent;

	public CircleItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClickable(true);
		mDetector = new GestureDetector(context, this);

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);
		//将滑动状态从 STOP_ROTATING 置为 IDLE，多一个STOP_ROTATING，是ROTATING之后，TART_FLING之前的一个状态，目前意义不大，主要看用户怎么利用
		if (event.getAction() == MotionEvent.ACTION_UP) {
			CircleMenuStatus status = mParent.getStatus();
			if (status != CircleMenuStatus.IDLE &&status != CircleMenuStatus.START_FLING && status != CircleMenuStatus.FLING) {
				mParent.idle();
			}
		}
		return super.onTouchEvent(event);// 防止点击事件丢失
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if (mParent == null) {
			mParent = (CircleMenu) getParent();
		}
		if (mParent != null) {
			CircleMenuStatus status = mParent.getStatus();
			if (status == CircleMenuStatus.FLING) {
				mParent.stopFling();
			}
			if (status == CircleMenuStatus.ROTATING) {
				mParent.stopRotate();
			}
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (mParent != null) {
			mParent.startFling(e1, e2, velocityX, velocityY);
		}
		return false;
	}
	


}
