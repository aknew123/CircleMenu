/**
 * 
 */
package com.pan.tanglang.circlemenu.model;
/**
 * @filename 文件名：CircleMenuStatus.java
 * @description 描    述：圆盘状态
 * @author 作    者：SergioPan
 * @date 时    间：2017-2-11
 * @Copyright 版    权：塘朗山源代码，版权归塘朗山所有。
 */
public enum CircleMenuStatus {
	/** 静止 **/
	IDLE,
	/** 开始旋转 **/
	START_ROTATING,
	/** 正在旋转 **/
	ROTATING,
	/** 暂停旋转 **/
	PAUSE_ROTATING,
	/** 叫停旋转，是ROTATING之后，START_FLING之前的一个状态，本项目中意义不大，看用户怎么利用 **/
	STOP_ROTATING,
	/** 开始飞转 **/
	START_FLING,
	/**正在飞转 **/
	FLING,
	/** 叫停飞转 **/
	STOP_FLING;
}

