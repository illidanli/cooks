package com.njupt.mobile.cook.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njupt.mobile.cook.R;
import com.njupt.mobile.cook.adapter.TabFragmentAdapter;
import com.njupt.mobile.cook.bean.GoodsListBean;
import com.njupt.mobile.cook.bean.ResBuyItemNum;
import com.njupt.mobile.cook.event.MessageEvent;
import com.njupt.mobile.cook.fragment.GoodsFragment;
import com.njupt.mobile.cook.util.GlideUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *点餐活动
 */
public class ResActivity extends BaseActivity {

    public static final String RES_ID = "res_id";
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.res_img)
    ImageView resImgIv;
    @BindView(R.id.res_name)
    TextView resNameTv;
    @BindView(R.id.res_star)
    RatingBar ratingBar;
    @BindView(R.id.res_score)
    TextView resScore;
    @BindView(R.id.res_order_num)
    TextView resOrderNum;
    @BindView(R.id.res_deliver_time)
    TextView resDeliverTime;
    @BindView(R.id.res_description)
    TextView resDescriptionTv;
    @BindView(R.id.res_reduce_container)
    AutoLinearLayout resReduceContainer;
    @BindView(R.id.res_reduce)
    TextView resReduceTv;
    @BindView(R.id.res_special_num)
    TextView resSpecialNum;
    @BindView(R.id.return_btn)
    ImageView returnBtn;
    @BindView(R.id.more_iv)
    ImageView moreIv;
    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.shop_cart_main)
    RelativeLayout shopCartMain;
    @BindView(R.id.shop_cart_num)
    TextView shopCartNum;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.no_shop)
    TextView noShop;
    @BindView(R.id.how_money_to_delivery)
    TextView howMoneyToDelivery;
    @BindView(R.id.go_to_account)
    TextView goToCheckOut;
    @BindView(R.id.pop_rl)
    RelativeLayout popRl;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    //fragment列表
    private List<Fragment> mFragments = new ArrayList<>();
    //tab名的列表
    private List<String> mTitles = new ArrayList<>();

    private double totalMoney;
    private String resName="药水哥的深夜食堂";
    private Integer resImg=R.mipmap.hhh;
    private Integer resId=1;
    String resDescription ="没有得到刘波的赞助" ;

    private TabFragmentAdapter adapter;
    private ViewGroup anim_mask_layout;

    private GoodsListBean goodsListBean;

    public GoodsListBean getGoodsListBean() {
        return goodsListBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        setStatusBarTransparent();
    }

    @Override
    protected void initView() {
        super.initView();
        returnBtn.setOnClickListener(this);
        goToCheckOut.setOnClickListener(this);
        popRl.setOnClickListener(this);
        moreIv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        initOurs();
        initGoods();
    }

    /**
     * 设置餐厅图片和资料
     */
    private void initOurs(){
        resNameTv.setText(resName);
        if (resImg!=0){
            GlideUtil.load(this,resImg,resImgIv,GlideUtil.REQUEST_OPTIONS);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Drawable drawable = getResources().getDrawable(resImg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            collapsingToolbarLayout.setContentScrim(drawable);
                        }
                    });
                }
            }).start();
        }

        if (!TextUtils.isEmpty(resDescription)){
            resDescriptionTv.setText(resDescription);
        }
    }

    /**
     *  添加组件
     */
    private void initGoods(){
        goodsListBean=new GoodsListBean();
        goodsListBean.setResId(1);
        goodsListBean.setResName(resName);
        goodsListBean.setData(new ArrayList<GoodsListBean.GoodsCategoryBean>(){
            {
                add(new GoodsListBean.GoodsCategoryBean( 1,"吃","超级好吃",new ArrayList<GoodsListBean.GoodsCategoryBean.GoodsItemBean>(){
                    {
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(1,"古法糖芋苗",12,"nice",R.mipmap.a,0));
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(2,"江米扣肉",12,"nice",R.mipmap.b,0));
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(3,"酱鸭头",10,"nice",R.mipmap.c,0));
                    }
                }));
                add(new GoodsListBean.GoodsCategoryBean( 2,"特色","超级好吃",new ArrayList<GoodsListBean.GoodsCategoryBean.GoodsItemBean>(){
                    {
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(4,"金牌煎饺",12,"nice",R.mipmap.d,0));
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(5,"酒酿赤豆元宵",12,"nice",R.mipmap.e,0));
                        add(new GoodsListBean.GoodsCategoryBean.GoodsItemBean(6,"民国美龄粥",10,"nice",R.mipmap.f,0));
                    }
                }));
            }
        });
        progressBar.setVisibility(View.GONE);
        setViewPager();
    }

    /**
     * 设置点餐主页
     */
    private void setViewPager(){
        GoodsFragment goodsFragment = new GoodsFragment();
        mFragments.add(goodsFragment);
        mTitles.add(getResources().getString(R.string.order));

        adapter = new TabFragmentAdapter(getSupportFragmentManager(),mFragments,mTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        shopCartMain.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        shopCartMain.setVisibility(View.GONE);
                        break;
                    case 2:
                        shopCartMain.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 处理按键事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.return_btn:
                finish();
                break;
            case R.id.pop_rl:
                showSelectedDetailDialog();
                break;
            case R.id.go_to_account:
                    Intent accountIntent = new Intent(this,AccountActivity.class);
                    accountIntent.putExtra("res_id",resId);
                    accountIntent.putExtra("res_name",resName);
                    startActivity(accountIntent);
                break;
        }
    }

    /**
     * 添加 或者  删除  商品发送的消息处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event!=null){
            if(event.num>0){
                //double类型保留小数点后一位
                DecimalFormat df = new DecimalFormat("#0.0");
                shopCartNum.setText(String.valueOf(event.num));
                shopCartNum.setVisibility(View.VISIBLE);
                totalPrice.setVisibility(View.VISIBLE);
                noShop.setVisibility(View.GONE);
                //设置购买的总价钱
                int price2 = (int)event.price;
                totalMoney = event.price;
                if (event.price > price2){
                    totalPrice.setText("¥"+df.format(event.price));
                }else{
                    totalPrice.setText("¥"+price2);
                }
                if (event.price >=0){
                    howMoneyToDelivery.setVisibility(View.GONE);
                    goToCheckOut.setVisibility(View.VISIBLE);
                    goToCheckOut.setText(getString(R.string.go_to_account));
                }
            }else{
                shopCartNum.setVisibility(View.GONE);
                totalPrice.setVisibility(View.GONE);
                noShop.setVisibility(View.VISIBLE);
                goToCheckOut.setVisibility(View.GONE);
                howMoneyToDelivery.setVisibility(View.VISIBLE);
                String deliverMoney = getResources().getString(R.string.res_deliver_money);
                howMoneyToDelivery.setText(deliverMoney);
            }
        }

    }

    /**
     * 设置动画（点击添加商品）
     * @param v
     * @param startLocation
     */
    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        shopCartNum.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 50;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(400);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 初始化动画图层
     * @return
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 将View添加到动画图层
     * @param parent
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 展示点的菜
     */
    private void showSelectedDetailDialog(){
        List<ResBuyItemNum> list = DataSupport.where("resId = ?", String.valueOf(resId)).find(ResBuyItemNum.class);
        if (list.size() > 0){
            double packageMoney = 0;
            final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
            //填充对话框的布局
            View view = LayoutInflater.from(this).inflate(R.layout.popup_goods_detail, null);
            AutoLinearLayout itemLl = (AutoLinearLayout) view.findViewById(R.id.item_ll);
            TextView packageMoneyTv = (TextView) view.findViewById(R.id.good_package_money);
            RelativeLayout packageMoneyRl = (RelativeLayout) view.findViewById(R.id.package_money_rl);
            for (ResBuyItemNum resBuyItemNum : list){
                itemLl.addView(initGoodDetailItemView(resBuyItemNum.getItemName(),resBuyItemNum.getBuyNum(),resBuyItemNum.getItemPrice()*resBuyItemNum.getBuyNum()));
                packageMoney += 0;
            }

            if (packageMoney > 0){
                packageMoneyRl.setVisibility(View.VISIBLE);
                int price = (int) packageMoney;
                if (packageMoney > price){
                    packageMoneyTv.setText(""+packageMoney);
                }else{
                    packageMoneyTv.setText(""+price);
                }
            }else{
                packageMoneyRl.setVisibility(View.GONE);
            }
            dialog.setCancelable(true);
            //将布局设置给Dialog
            dialog.setContentView(view);
            //获取当前Activity所在的窗体
            Window dialogWindow = dialog.getWindow();
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = getResources().getDimensionPixelOffset(R.dimen.dimen_54dp);//设置Dialog距离底部的距离
            //设置dialog宽度满屏
            WindowManager m = dialogWindow.getWindowManager();
            Display d = m.getDefaultDisplay();
            lp.width = d.getWidth();
            //将属性设置给窗体
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

    /**
     * 展示商品具体的组件
     * @param goodNameText
     * @param num
     * @param goodPriceText
     * @return
     */
    private View initGoodDetailItemView(String goodNameText, int num, double goodPriceText){
        View view = LayoutInflater.from(this).inflate(R.layout.goods_detail_item,null);
        TextView goodName = (TextView) view.findViewById(R.id.good_name);
        TextView goodNum = (TextView) view.findViewById(R.id.good_num);
        TextView goodPrice = (TextView) view.findViewById(R.id.good_price);
        goodName.setText(goodNameText);
        goodNum.setText("×"+num);
        int price = (int) goodPriceText;
        if (goodPriceText > price){
            goodPrice.setText(""+goodPriceText);
        }else{
            goodPrice.setText(""+price);
        }
        return view;
    }


}
