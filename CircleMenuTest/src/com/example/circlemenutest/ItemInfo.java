package com.example.circlemenutest;

public class ItemInfo {

	private int imgId;
	private String text;
	
	public ItemInfo() {
		super();
	}
	public ItemInfo(int imgId, String text) {
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
		return "ItemData [imgId=" + imgId + ", text=" + text + "]";
	}
	
	
}
