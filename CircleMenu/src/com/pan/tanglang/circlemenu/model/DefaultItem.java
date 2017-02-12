package com.pan.tanglang.circlemenu.model;

/**
 * Item信息实体类
 * @author Administrator
 */
public  class DefaultItem {
	
	private int imgId;
	private String text;
	
	
	public DefaultItem() {
		super();
	}
	
	public DefaultItem(int imgId, String text) {
		super();
		this.imgId = imgId;
		this.text = text;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "BaseItem [imgId=" + imgId + ", text=" + text + "]";
	}
	
	
	

}
