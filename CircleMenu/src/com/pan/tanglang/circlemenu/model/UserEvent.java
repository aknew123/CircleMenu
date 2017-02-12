package com.pan.tanglang.circlemenu.model;


/**
 * @filename 文件名：UserEvent.java
 * @description 描    述：用户输入事件
 * @author 作    者：SergioPan
 * @date 时    间：2017-2-11
 * @Copyright 版    权：塘朗山源代码，版权归塘朗山所有。
 */
public enum UserEvent {
	/** 无用事件 **/
	USELESS_ACTION,
	/** 点击事件 **/
	CLICK,
	/** 长按事件 **/
	LONG_CLICK,
	/** 滚动事件 **/
	SCROLL,
	/** 飞转事件 **/
	FLING,
	/** 停止飞转事件 **/
	STOP_FLING;

}
