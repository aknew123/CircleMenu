package com.pan.tanglang.circlemenu.control;

import android.util.Log;
import android.view.MotionEvent;

import com.pan.tanglang.circlemenu.model.CircleMenuStatus;
import com.pan.tanglang.circlemenu.view.CircleMenu;
import com.pan.tanglang.circlemenu.view.CircleMenu.RotateDirection;

/**
 * @filename 文件名：RotateEngine.java
 * @description 描 述：随手势旋转引擎
 * @author 作 者：SergioPan
 * @date 时 间：2017-2-11
 * @Copyright 版 权：塘朗山源代码，版权归塘朗山所有。
 */
public class RotateEngine {

	public static final String TAG = "RotateEngine";
	private static RotateEngine instance = null;
	private float startX;
	private float startY;
	/** 请求重新布局的起始角度 **/
	private double mStartAngle;

	private RotateEngine() {
	}

	public static RotateEngine getInstance() {
		if (instance == null) {
			synchronized (RotateEngine.class) {
				if (instance == null) {
					instance = new RotateEngine();
				}
			}
		}
		return instance;
	}

	// 通过dispatch实现
	public void onCircleMenuTouch(MotionEvent ev, int radius, CircleMenu mCircleMenu) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			startY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			double start = getAngle(startX, startY, radius);
			if (mCircleMenu.getStatus() == CircleMenuStatus.STOP_FLING
					|| mCircleMenu.getStatus() == CircleMenuStatus.IDLE) {
				//	开始滚动
				mCircleMenu.startRotate();
				//	拿到Fling停下（自然停或按停）之后的开始角度
				mStartAngle = mCircleMenu.getmStartAngle();
			}
			float x = ev.getX();
			float y = ev.getY();
			toCircleMenuScroll(mCircleMenu, radius, start, ev);
			startX = x;
			startY = y;
			break;
		case MotionEvent.ACTION_UP:
			CircleMenuStatus status = mCircleMenu.getStatus();
			Log.i(TAG, "---ACTION_UP---status = " + status);
			if (status == CircleMenuStatus.ROTATING || status == CircleMenuStatus.PAUSE_ROTATING) {
				mCircleMenu.stopRotate();
			}

			break;

		default:
			break;
		}
	}

	public void toCircleMenuScroll(CircleMenu mCircleMenu, int radius, double start, MotionEvent e2) {
		float x = e2.getX();
		float y = e2.getY();
		// 一个连续的角度差值，用于判断滑动方向，得出顺时针、逆时针
		float directionAngle = 0;
		double end = getAngle(x, y, radius);
		// 如果是一、四象限，则直接end-start，角度值都是正值
		if (getQuadrant(x, y, radius) == 1 || getQuadrant(x, y, radius) == 4) {
			mStartAngle += end - start;
			directionAngle += end - start;
		} else {
			// 二、三象限，角度值是负值
			mStartAngle += start - end;
			directionAngle += start - end;
		}

		if (directionAngle > 0) {
			mCircleMenu.setmDirection(RotateDirection.CLOCKWISE);
		} else {
			mCircleMenu.setmDirection(RotateDirection.ANTICLOCKWISE);
		}
		// 旋转角度, 请求重新布局
		mCircleMenu.relayoutMenu(mStartAngle);
		if (startX != x || startY != y) {
			mCircleMenu.onRotating(directionAngle);
		} else {
			mCircleMenu.onPauseRotate();
		}
	}

	public double getAngle(float xTouch, float yTouch, int radius) {
		double x = xTouch - (radius / 2d);
		double y = yTouch - (radius / 2d);
		return (double) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	}

	public int getQuadrant(float x, float y, int radius) {
		int tmpX = (int) (x - radius / 2);
		int tmpY = (int) (y - radius / 2);
		if (tmpX >= 0) {
			return tmpY >= 0 ? 4 : 1;
		} else {
			return tmpY >= 0 ? 3 : 2;
		}

	}

}
