package com.haoke.selectcity.page;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haoke.selectcity.R;
import com.haoke.selectcity.adapter.OnItemClickListener;
import com.haoke.selectcity.adapter.SearchCityAdapter;
import com.haoke.selectcity.adapter.SelectCityAdapter;
import com.haoke.selectcity.adapter.DividerItemDecoration;
import com.haoke.selectcity.bean.SelectCityBean;
import com.haoke.selectcity.bean.SelectCityHeaderBean;
import com.haoke.selectcity.adapter.CommonAdapter;
import com.haoke.selectcity.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.haoke.selectcity.adapter.ViewHolder;
import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 介绍： 高仿美团选择城市页面
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 2016/11/7.
 */
public class SelectCityActivity extends AppCompatActivity {
    private static final String TAG = "zxt";
    private Context mContext;
    private RecyclerView mRv;
    private SelectCityAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;

    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas;
    //头部数据源
    private List<SelectCityHeaderBean> mHeaderDatas;
    //主体部分数据源（城市数据）
    private List<SelectCityBean> mBodyDatas;
    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;
    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;

    private SearchCityAdapter searchCityAdapter;
    private ListView searchListView;
    private List<SelectCityBean> searchDatas;
    private EditText editText;
    private View search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        mContext = this;
        search_view=findViewById(R.id.search_view);
        editText = (EditText) findViewById(R.id.searchView);
        searchListView = (ListView) findViewById(R.id.listView);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        searchDatas = new ArrayList<>();
        mSourceDatas = new ArrayList<>();
        mHeaderDatas = new ArrayList<>();
        searchCityAdapter = new SearchCityAdapter(this, searchDatas);
        searchListView.setAdapter(searchCityAdapter);

        List<String> locationCity = new ArrayList<>();
        mHeaderDatas.add(new SelectCityHeaderBean(locationCity, "全国", ""));
        List<String> recentCitys = new ArrayList<>();
        mHeaderDatas.add(new SelectCityHeaderBean(recentCitys, "定位/最近访问", "定位"));
        List<String> hotCitys = new ArrayList<>();
        mHeaderDatas.add(new SelectCityHeaderBean(hotCitys, "热门城市", "热门"));
        mSourceDatas.addAll(mHeaderDatas);

        mAdapter = new SelectCityAdapter(this, R.layout.slect_city_item_select_city, mBodyDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                switch (layoutId) {
                    case R.layout.slect_city_item_header:
                        final SelectCityHeaderBean meituanHeaderBean = (SelectCityHeaderBean) o;
                        //网格
                        RecyclerView recyclerView = holder.getView(R.id.rvCity);
                        recyclerView.setAdapter(
                                new CommonAdapter<String>(mContext, R.layout.slect_city_item_header_item, meituanHeaderBean.getCityList()) {
                                    @Override
                                    public void convert(ViewHolder holder, final String cityName) {
                                        holder.setText(R.id.tvName, cityName);
                                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(mContext, cityName, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                        break;

                    default:
                        break;
                }
            }
        };

        //设置头部视图
        mHeaderAdapter.setHeaderView(0, R.layout.slect_city_item_header, mHeaderDatas.get(0));
        mHeaderAdapter.setHeaderView(1, R.layout.slect_city_item_header, mHeaderDatas.get(1));
        mHeaderAdapter.setHeaderView(2, R.layout.slect_city_item_header, mHeaderDatas.get(2));

        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffcccccc)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()))
                .setColorTitleFont(mContext.getResources().getColor(R.color.color_city_black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mRv.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
//        mTvSideBarHint.setTextColor(getResources().getColor(R.color.color_LAN));
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());

        initDatas(getResources().getStringArray(R.array.city_array));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                final SelectCityBean meiTuanBean = (SelectCityBean) o;
                Toast.makeText(mContext, meiTuanBean.getCity(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d("cityss", "-------------");
                if (editText.getText().toString().trim().equals("")) {
//                    searchDatas.clear();
                    search_view.setVisibility(View.GONE);
//                    searchCityAdapter.notifyDataSetChanged();
                } else {
                    if (mBodyDatas != null) {
                        searchDatas.clear();
                        for (int i = 0; i < mBodyDatas.size(); i++) {
                            if (mBodyDatas.get(i).getCity().contains(editText.getText().toString().trim())) {
                                searchDatas.add(mBodyDatas.get(i));
                                Log.d("cityss", "--3-----------");
                            }
                        }
                        Log.d("cityss", "--2-----------");
                        if (searchDatas.size() > 0) {
                            search_view.setVisibility(View.VISIBLE);
                            searchCityAdapter.notifyDataSetChanged();
                        } else {
                            search_view.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                search_view.setVisibility(View.GONE);
                finish();
                overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
            }
        });

    }


    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {
        //延迟两秒 模拟加载数据中....
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBodyDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    SelectCityBean cityBean = new SelectCityBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mBodyDatas.add(cityBean);
                }
                //先排序
                mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);

                mAdapter.setDatas(mBodyDatas);
                mHeaderAdapter.notifyDataSetChanged();
                mSourceDatas.addAll(mBodyDatas);

                mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                        .invalidate();
                mDecoration.setmDatas(mSourceDatas);
            }
        }, 200);

        //延迟两秒加载头部
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                SelectCityHeaderBean header1 = mHeaderDatas.get(0);
                header1.getCityList().clear();
                header1.getCityList().add("全国");

                SelectCityHeaderBean header2 = mHeaderDatas.get(1);
                List<String> recentCitys = new ArrayList<>();
                recentCitys.add("深圳");
                header2.setCityList(recentCitys);

                SelectCityHeaderBean header3 = mHeaderDatas.get(2);
                List<String> hotCitys = new ArrayList<>();
                hotCitys.add("广州");
                hotCitys.add("东莞");
                hotCitys.add("惠州");
                hotCitys.add("佛山");
                hotCitys.add("长沙");
                hotCitys.add("武汉");
                hotCitys.add("上海");
                hotCitys.add("珠海");
                hotCitys.add("武汉");
                header3.setCityList(hotCitys);

                mHeaderAdapter.notifyItemRangeChanged(1, 3);

            }
        }, 0);


    }

//    /**
//     * 更新数据源
//     *
//     * @param view
//     */
//    public void updateDatas(View view) {
//        for (int i = 0; i < 5; i++) {
//            mBodyDatas.add(new SelectCityBean("东京"));
//            mBodyDatas.add(new SelectCityBean("大阪"));
//        }
//        //先排序
//        mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);
//        mSourceDatas.clear();
//        mSourceDatas.addAll(mHeaderDatas);
//        mSourceDatas.addAll(mBodyDatas);
//
//        mHeaderAdapter.notifyDataSetChanged();
//        mIndexBar.invalidate();
//    }
}
