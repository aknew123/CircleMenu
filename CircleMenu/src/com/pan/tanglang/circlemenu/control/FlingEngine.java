package com.pan.tanglang.circlemenu.control;

import com.pan.tanglang.circlemenu.model.CircleMenuStatus;
import com.pan.tanglang.circlemenu.view.CircleMenu;
import com.pan.tanglang.circlemenu.view.CircleMenu.RotateDirection;


/**
 * @filename 文件名：FlingEngine.java
 * @description 描    述：飞转引擎
 * @author 作    者：SergioPan
 * @date 时    间：2017-2-11
 * @Copyright 版    权：塘朗山源代码，版权归塘朗山所有。
 */
public class FlingEngine implements Runnable {

	private static final String TAG = "FlingEngine";
	private int mVelocity;
	private int DELAY = 10;
	private double startAngle;
	private CircleMenu mCircleMenu;

	public void start(CircleMenu circleMenu, float velocity, double start) {
		mCircleMenu = circleMenu;
		mVelocity = (int) Math.abs(velocity);
		startAngle = start;
		if (circleMenu.getStatus() == CircleMenuStatus.START_FLING) {
			circleMenu.post(this);
		}
	}

	@Override
	public void run() {
		// 如果小于20,则停止
		if (mCircleMenu.getStatus() == CircleMenuStatus.STOP_FLING || mVelocity <= 0) {
			//叫停或自动停止
			mCircleMenu.idle();
			return;
		}
		double preStartAngle = startAngle;
		// 顺时针
		if (mCircleMenu.getmDirection() == RotateDirection.CLOCKWISE) {

			flingSlowDownByClockwise();
		} else if (mCircleMenu.getmDirection() == RotateDirection.ANTICLOCKWISE) {
			flingSlowDownByAnticlockwise();
		}
		mCircleMenu.relayoutMenu(startAngle);
		mCircleMenu.onMenuFling(startAngle - preStartAngle);
		mCircleMenu.postDelayed(this, DELAY);
	}

	// 逆时针减速
	private void flingSlowDownByAnticlockwise() {

		if (mVelocity > 10000) {
			mVelocity -= 1000;
			startAngle -= 10;
		} else if (mVelocity > 5000) {
			mVelocity -= 100;
			startAngle -= 8;
		} else if (mVelocity > 1000) {
			mVelocity -= 50;
			startAngle -= 6;
		} else if (mVelocity > 500) {
			mVelocity -= 10;
			startAngle -= 4;
		} else if (mVelocity > 100) {
			mVelocity -= 5;
			startAngle -= 2;
		} else {
			mVelocity--;
			startAngle--;
		}
	}

	// 顺时针减速
	private void flingSlowDownByClockwise() {

		if (mVelocity > 10000) {
			mVelocity -= 1000;
			startAngle += 10;
		} else if (mVelocity > 5000) {
			mVelocity -= 100;
			startAngle += 8;
		} else if (mVelocity > 1000) {
			mVelocity -= 50;
			startAngle += 6;
		} else if (mVelocity > 500) {
			mVelocity -= 10;
			startAngle += 4;
		} else if (mVelocity > 100) {
			mVelocity -= 5;
			startAngle += 2;
		} else {
			mVelocity--;
			startAngle++;
		}
	}

	public int getQuadrant(float angle, int radius) {
		float x = Math.round(radius * Math.cos(Math.toRadians(angle)));
		float y = Math.round(radius * Math.sin(Math.toRadians(angle)));
		int tmpX = (int) (x - radius / 2);
		int tmpY = (int) (y - radius / 2);
		if (tmpX >= 0) {
			return tmpY >= 0 ? 4 : 1;
		} else {
			return tmpY >= 0 ? 3 : 2;
		}

	}

}
