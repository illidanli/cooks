package com.njupt.mobile.cook.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.njupt.mobile.cook.adapter.PopupPayWayAdapter;
import com.njupt.mobile.cook.bean.CouponBean;
import com.njupt.mobile.cook.bean.ResBuyItemNum;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.njupt.mobile.cook.R;
import butterknife.BindView;

/**
 * 结算活动
 */
public class AccountActivity extends BaseActivity {
    private static final int REQUEST_ADDRESS = 8080;
    private static final int REQUEST_COUPON = 8018;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.go_to_account)
    TextView goToAccount;
    @BindView(R.id.how_money_to_delivery)
    TextView howMoneyToDelivery;
    @BindView(R.id.no_shop)
    TextView noShop;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_all_price_bottom)
    TextView allPriceBottom;
    @BindView(R.id.tv_res_name)
    TextView resName;
    @BindView(R.id.tv_all_money)
    TextView allMoneyTv;
    @BindView(R.id.ll_buy_item_container)
    AutoLinearLayout buyItemContainer;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.pay_way)
    AutoRelativeLayout payWay;
    @BindView(R.id.tv_pay_selected)
    TextView paySelectedTv;
    @BindView(R.id.extra_info)
    EditText extraInfo;
    @BindView(R.id.rl_red_paper)
    AutoRelativeLayout rlRedPaper;
    @BindView(R.id.tv_red_paper)
    TextView redPaperTv;
    @BindView(R.id.rl_reduce)
    RelativeLayout reduceRl;
    @BindView(R.id.tv_reduce)
    TextView reduceTv;

    private int resId;
    private String resNameText;
    //用户购买的详细数据
    private List<ResBuyItemNum> list;

    private double allMoney;
    private List<String> payTvList;
    private List<Integer> payIvList;

    private Dialog payDialog;
    private CouponBean couponBean;
    private double reduceMoney;
    DecimalFormat df = new DecimalFormat("#0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        resId = getIntent().getIntExtra("res_id",1);
        resNameText = getIntent().getStringExtra("res_name");
        list = DataSupport.where("resId = ?", String.valueOf(resId)).find(ResBuyItemNum.class);
        for (ResBuyItemNum resBuyItemNum : list){
            View view = initBuyItem(resBuyItemNum);
            buyItemContainer.addView(view);
            allMoney += resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice();
        }

        progressBar.setVisibility(View.VISIBLE);
        goToAccount.setClickable(false);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("shop_id", String.valueOf(resId));
        allMoneyTv.setText(df.format((allMoney)));
        //初始化支付方式
        payTvList = new ArrayList<>();
        payTvList.add("支付宝");
        payTvList.add("银行卡支付");
        payTvList.add("微信支付");
        payTvList.add("QQ钱包");

        payIvList = new ArrayList<>();
        payIvList.add(R.mipmap.ali_pay);payIvList.add(R.mipmap.card_pay);payIvList.add(R.mipmap.v_pay);payIvList.add(R.mipmap.q_pay);
    }

    @Override
    protected void initView() {
        super.initView();
        goToAccount.setVisibility(View.VISIBLE);
        howMoneyToDelivery.setVisibility(View.GONE);
        noShop.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        allPriceBottom.setVisibility(View.VISIBLE);
        resName.setText(resNameText);
        paySelectedTv.setText(payTvList.get(0));
        backBtn.setOnClickListener(this);
        payWay.setOnClickListener(this);
        rlRedPaper.setOnClickListener(this);
        initPayDialog();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.go_to_account:
                    final long orderTimeId =  System.currentTimeMillis();
                    final int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1);
                    progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.pay_way:
                payDialog.show();
                break;
            case R.id.rl_red_paper:
                Intent couponIntent = new Intent(this,CouponActivity.class);
                couponIntent.putExtra("res_id",resId+"");
                couponIntent.putExtra("res_name",resNameText);
                couponIntent.putExtra("all_money",allMoney);
                startActivityForResult(couponIntent,REQUEST_COUPON);
                break;

        }
    }

    private View initBuyItem(ResBuyItemNum resBuyItemNum){
        View view = LayoutInflater.from(this).inflate(R.layout.buy_list_item,null);
        TextView name = (TextView) view.findViewById(R.id.account_item_name);
        TextView price = (TextView) view.findViewById(R.id.account_item_price);
        TextView num = (TextView) view.findViewById(R.id.account_item_num);
        name.setText(resBuyItemNum.getItemName());
        price.setText("￥"+resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice());
        num.setText("×"+resBuyItemNum.getBuyNum()+"");
        return view;
    }

    private void initPayDialog(){
        payDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_pay_bottom, null);
        RecyclerView payRecyclerView = (RecyclerView) view.findViewById(R.id.pay_recycler);
        ImageButton close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.dismiss();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        payRecyclerView.setLayoutManager(linearLayoutManager);
        final PopupPayWayAdapter adapter = new PopupPayWayAdapter(this,payTvList,payIvList);
        adapter.setOnItemClickListener(new PopupPayWayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                payDialog.dismiss();
                adapter.setSelected(position);
                progressBar.setVisibility(View.VISIBLE);
                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      progressBar.setVisibility(View.GONE);
                        paySelectedTv.setText(payTvList.get(position));
                    }
                },500);

            }
        });
        payRecyclerView.setAdapter(adapter);

        payDialog.setCancelable(false);
        //将布局设置给Dialog
        payDialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = payDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //设置dialog宽度满屏
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.width = d.getWidth();
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ADDRESS:
                if (resultCode == RESULT_OK){
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case REQUEST_COUPON:
                if (resultCode == RESULT_OK){
                    couponBean = (CouponBean) data.getSerializableExtra("coupon");
                    double price = Math.floor((1-couponBean.getDecise())*allMoney);
                    redPaperTv.setText("-￥"+price);
                    allMoneyTv.setText(df.format((allMoney - price)));
                }
                break;
        }
    }

}
