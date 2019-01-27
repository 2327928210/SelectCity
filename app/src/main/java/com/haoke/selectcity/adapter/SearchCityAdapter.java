package com.haoke.selectcity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoke.selectcity.R;
import com.haoke.selectcity.bean.SelectCityBean;

import java.util.List;

/**
 * Created by haier on 2019/1/25.
 */

public class SearchCityAdapter extends BaseAdapter {
    private Context context;
    private List<SelectCityBean> data;

    public SearchCityAdapter(Context context, List<SelectCityBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.search_item_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(data.get(i).getCity());
        return view;
    }

    class ViewHolder {
        private TextView textView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.tvCity);
        }

    }

}
