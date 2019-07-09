package com.njupt.mobile.cook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.njupt.mobile.cook.R;
import com.njupt.mobile.cook.adapter.CouponAdapter;
import com.njupt.mobile.cook.bean.CouponBean;

import butterknife.BindView;

/**
 * 红包活动
 */
public class CouponActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.classify_title)
    TextView title;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CouponAdapter adapter;
    private List<CouponBean> list;
    private double allMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        allMoney=getIntent().getDoubleExtra("all_money",0);
        list = new ArrayList<CouponBean>(){
            {
                add(new CouponBean(10,0.5,"折扣"));
                add(new CouponBean(10,0.8,"折扣"));
                add(new CouponBean(15,0.8,"折扣"));
            }
        };
        adapter = new CouponAdapter(CouponActivity.this,allMoney,list);
        adapter.setOnUseBtnClickListener(new CouponAdapter.OnUseBtnClickListener() {
            @Override
            public void useBtnClickListener(int position, CouponBean couponBean) {
                setResult(RESULT_OK,new Intent().putExtra("coupon",couponBean));
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CouponActivity.this));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        super.initView();
        title.setText("红包");
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
