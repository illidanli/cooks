package com.njupt.mobile.cook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.njupt.mobile.cook.R;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {
    //private List<ResDetailBean> homeRecResDetailList;

    /**
     * 创建时展示欢迎视图
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * 销毁后进入主菜单
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(WelcomeActivity.this, ResActivity.class);
        intent.putExtra("data","");
        startActivity(intent);
        finish();
    }
}
