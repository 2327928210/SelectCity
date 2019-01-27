package com.haoke.selectcity.adapter;

import android.content.Context;

import com.haoke.selectcity.R;
import com.haoke.selectcity.bean.SelectCityBean;

import java.util.List;



/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class SelectCityAdapter extends CommonAdapter<SelectCityBean> {
    public SelectCityAdapter(Context context, int layoutId, List<SelectCityBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final SelectCityBean cityBean) {
        holder.setText(R.id.tvCity, cityBean.getCity());
    }
}