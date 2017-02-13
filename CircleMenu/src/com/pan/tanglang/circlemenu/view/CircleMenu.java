package com.pan.tanglang.circlemenu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.pan.tanglang.circlemenu.control.FlingEngine;
import com.pan.tanglang.circlemenu.control.RotateEngine;
import com.pan.tanglang.circlemenu.model.CircleMenuStatus;
import com.pan.tanglang.circlemenu.model.UserEvent;

/**
 * @filename 文件名：CircleMenu.java
 * @description 描 述：圆盘菜单容器,圆盘菜单功能的核心文件
 * @author 作 者：SergioPan
 * @date 时 间：2017-2-11
 * @Copyright 版 权：塘朗山源代码，版权归塘朗山所有。
 */
public class CircleMenu extends ViewGroup implements OnGestureListener {

	public static final String TAG = "CircleMenu";
	/** 圆盘半径，那么圆心为（mRadius， mRadius） **/
	public int mRadius;
	private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 2f;
	private static final float RADIO_PADDING_LAYOUT = 1 / 20f;
	/** 内边距，默认为mRadius/20 **/
	public float mPadding = -1;
	private double mStartAngle = 0;
	private OnMenuItemClickListener mListener;
	private ListAdapter mAdapter;
	private CircleMenuStatus mStatus = CircleMenuStatus.IDLE;
	private OnMenuStatusChangedListener mStatusListener;
	private GestureDetector mDetector;
	private RotateEngine mRotate;
	private RotateDirection mDirection = RotateDirection.CLOCKWISE;
	private FlingEngine mFlingEngine;
	// 用户输入事件，默认为无用行为，主要用于解决Fling后的按停，此时屏蔽点击事件
	private UserEvent mUserEvent = UserEvent.USELESS_ACTION;

	public CircleMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPadding(0, 0, 0, 0);
		setClickable(true);
		mDetector = new GestureDetector(context, this);
		mRotate = RotateEngine.getInstance();

	}

	/** 转动方向 **/
	public enum RotateDirection {
		/** 顺时针 **/
		CLOCKWISE,
		/** 逆时针 **/
		ANTICLOCKWISE;
	}

	//依附到窗口上
	@Override
	protected void onAttachedToWindow() {
		if (mAdapter != null) {
			buildMenuItems();
		}
		super.onAttachedToWindow();
	}

	/**
	 * 菜单重新布局
	 * 
	 * @param startAngle
	 */
	public void relayoutMenu(double startAngle) {
		mStartAngle = startAngle;
		requestLayout();
	}

	// 构建菜单项
	@SuppressLint("NewApi")
	private void buildMenuItems() {
		if (mAdapter.getCount() <= 0) {
			return;
		}
		for (int i = 0; i < mAdapter.getCount(); i++) {

			CircleItemView itemView = (CircleItemView) mAdapter.getView(i, null, this);
			final int position = i;
			itemView.setClickable(true);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mUserEvent == UserEvent.FLING) {
						// 正在飞转时，接收用户的点击事件，则视为停止飞转动作，而屏蔽点击事件
						mUserEvent = UserEvent.USELESS_ACTION;
						return;
					}
					// 非飞转时响应点击事件
					if (mListener != null) {
						mListener.onClick(v, position);
					}

				}
			});
			addView(itemView);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 测量自身
		// measureMyself(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量子View
		measureChildViews();

	}

	private void measureChildViews() {
		if (mAdapter.getCount() <= 0) {
			return;
		}

		// 获取半径,
		mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight()) / 2;
		final int count = getChildCount();
		// 取mRadius/2为Item宽度
		int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
		int childMode = MeasureSpec.EXACTLY;
		int makeMeasureSpec = -1;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
			// 设置为正方形
			child.measure(makeMeasureSpec, makeMeasureSpec);
		}
		// 取mRadius/10为默认内边距
		if (mPadding == -1) {
			mPadding = RADIO_PADDING_LAYOUT * mRadius;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mAdapter.getCount() <= 0) {
			return;
		}
		final int childCount = getChildCount();
		int left, top, halfDiagonal;
		// 限制Item的宽高
		int itemWidth = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
		float angleDelay = 360 / childCount;
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			mStartAngle %= 360;
			// 取Item对角线的一半为Item中心到圆盘圆周的距离
			halfDiagonal = (int) (itemWidth / Math.sqrt(2));
			float distanceFromCenter = mRadius - halfDiagonal - mPadding;
			left = mRadius
					+ (int) Math.round(distanceFromCenter * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * itemWidth);
			top = mRadius
					+ (int) Math.round(distanceFromCenter * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * itemWidth);
			// 重新Layout
			child.layout(left, top, left + itemWidth, top + itemWidth);
			mStartAngle += angleDelay;
		}
		
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = mDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mStatus != CircleMenuStatus.START_FLING && mStatus != CircleMenuStatus.FLING) {
				idle();
			}
		}
		return result;
	}

	// 因为父view的dispatchTouchEvent先获得事件，所以在这里可以连带子view的滚动事件一起处理
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mRotate != null) {
			mRotate.onCircleMenuTouch(event, mRadius, this);
		}
		return super.dispatchTouchEvent(event);

	}

	public void startMenuFling() {
		mStatus = CircleMenuStatus.START_FLING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	public void stopFling() {
		mStatus = CircleMenuStatus.STOP_FLING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	public void onMenuFling(double angle) {
		mStatus = CircleMenuStatus.FLING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, angle);
			mUserEvent = UserEvent.FLING;
		}
	}

	public void startRotate() {
		mStatus = CircleMenuStatus.START_ROTATING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	public void onRotating(float angle) {
		mStatus = CircleMenuStatus.ROTATING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, angle);
		}
	}

	public void onPauseRotate() {
		mStatus = CircleMenuStatus.PAUSE_ROTATING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	public void stopRotate() {
		mStatus = CircleMenuStatus.STOP_ROTATING;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	public void idle() {
		mStatus = CircleMenuStatus.IDLE;
		if (mStatusListener != null) {
			mStatusListener.onStatusChanged(mStatus, 0);
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		if (mStatus == CircleMenuStatus.FLING) {
			stopFling();
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		startFling(e1, e2, velocityX, velocityY);
		return false;
	}

	/**
	 * 开始飞转
	 * @param e1
	 * @param e2
	 * @param velocityX
	 * @param velocityY
	 */
	public void startFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (mStatus == CircleMenuStatus.STOP_ROTATING) {
			startMenuFling();
			float velocity = Math.abs(velocityX) > Math.abs(velocityY) ? velocityX : velocityY;
			double start = mRotate.getAngle(e2.getX(), e2.getY(), mRadius);
			if (mFlingEngine == null) {
				mFlingEngine = new FlingEngine();
			}
			mFlingEngine.start(this, velocity, start);
		}
	}

	public RotateDirection getmDirection() {
		return mDirection;
	}

	public void setmDirection(RotateDirection mDirection) {
		this.mDirection = mDirection;
	}

	public void setStatus(CircleMenuStatus status) {
		mStatus = status;
	}

	public CircleMenuStatus getStatus() {
		return mStatus;
	}

	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
	}

	public ListAdapter getAdapter() {
		return mAdapter;
	}

	public void setOnItemClickListener(OnMenuItemClickListener listener) {
		this.mListener = listener;
	}

	public void setOnStatusChangedListener(OnMenuStatusChangedListener statusListener) {
		this.mStatusListener = statusListener;
	}

	public UserEvent getmUserEvent() {
		return mUserEvent;
	}

	public void setmUserEvent(UserEvent mUserEvent) {
		this.mUserEvent = mUserEvent;
	}

	public double getmStartAngle() {
		return mStartAngle;
	}

	public float getmPadding() {
		return mPadding;
	}

	public void setmPadding(float mPadding) {
		this.mPadding = mPadding;
	}

	/**
	 * 圆盘菜单状态改变监听器，用于定制外围或圆心动画
	 * 
	 * @author SergioPan
	 */
	public interface OnMenuStatusChangedListener {

		/**
		 * @param status
		 *            状态
		 * @param rotateAngle
		 *            旋转量（角度）
		 */
		public void onStatusChanged(CircleMenuStatus status, double rotateAngle);

	}

	/**
	 * Item点击事件监听器
	 * 
	 * @author SergioPan
	 */
	public interface OnMenuItemClickListener {

		public void onClick(View view, int position);
	}

}
