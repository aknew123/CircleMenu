package com.pan.tanglang.circlemenu.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pan.tanglang.circlemenu.R;
import com.pan.tanglang.circlemenu.model.DefaultItem;


/**
 * @filename 文件名：FlingEngine.java
 * @description 描    述：圆盘菜单默认适配器，如有其他需求，用户自行定义适配器，继承BaseAdapter就可以，自行编写xml文件
 * @author 作    者：SergioPan
 * @date 时    间：2017-2-11
 * @Copyright 版    权：塘朗山源代码，版权归塘朗山所有。
 */
public class DefaultMenuAdapter extends BaseAdapter {

	private List<DefaultItem> items;

	public DefaultMenuAdapter(List<DefaultItem> data) {
		items = data;
	}

	@Override
	public int getCount() {
		if (items == null) {
			return 0;
		}
		return items.size();
	}

	@Override
	public Object getItem(int position) {

		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(), R.layout.item_default, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_default_circle_menu_item);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_default_circle_menu_item);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		DefaultItem item = items.get(position);
		if (item != null) {
			holder.iv.setImageResource(item.getImgId());
			holder.tv.setText(item.getText());
		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}

}
