package com.njupt.mobile.cook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.OnHeaderClickListener;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;

import com.njupt.mobile.cook.R;
import com.njupt.mobile.cook.activity.ResActivity;
import com.njupt.mobile.cook.adapter.BigramHeaderAdapter;
import com.njupt.mobile.cook.adapter.GoodsCategoryRecyclerAdapter;
import com.njupt.mobile.cook.adapter.GoodsItemRecyclerAdapter;
import com.njupt.mobile.cook.bean.GoodsListBean;
import com.njupt.mobile.cook.bean.ResBuyCategoryNum;
import com.njupt.mobile.cook.bean.ResBuyItemNum;
import com.njupt.mobile.cook.event.GoodsListEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品组件
 */
public class GoodsFragment extends Fragment implements OnHeaderClickListener {

    private RecyclerView goodsCategoryRecycler;
    private GoodsCategoryRecyclerAdapter mGoodsCategoryRecyclerAdapter;

    private List<GoodsListBean.GoodsCategoryBean> goodsCategoryList = new ArrayList<>();

    private List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goodsItemList = new ArrayList<>();

    private List<Integer> categoryFirstItemPosi = new ArrayList<>();

    private RecyclerView goodsItemRecycler;
    private GoodsItemRecyclerAdapter goodsItemRecyclerAdapter;
    private StickyHeadersItemDecoration top;
    private BigramHeaderAdapter headerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private GoodsListBean dataList;

    private View root;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.goods_fragment, container, false);
        initView();
        initData();
        return root;
    }

    private void initView() {
        goodsCategoryRecycler = (RecyclerView) root.findViewById(R.id.goods_category_list);
        goodsItemRecycler = (RecyclerView) root.findViewById(R.id.goods_item_list);
    }

    private void initData() {
        dataList = ((ResActivity)getActivity()).getGoodsListBean();
        List<ResBuyCategoryNum> resBuyCategoryNumList = DataSupport.where("resId = ?",String.valueOf(dataList.getResId())).find(ResBuyCategoryNum.class);
        for (int i = 0;i<resBuyCategoryNumList.size();i++){
            String categoryId = resBuyCategoryNumList.get(i).getCategoryId();
            int butNum = resBuyCategoryNumList.get(i).getBuyNum();
            for (int j=0;j<dataList.getData().size();j++){
                if (Integer.parseInt(categoryId) == dataList.getData().get(j).getCategoryId()){
                    dataList.getData().get(j).setBuyNum(butNum);
                    List<ResBuyItemNum> resBuyItemNumList = DataSupport.where("resId = ? and categoryId =? ",String.valueOf(dataList.getResId()),
                            String.valueOf(categoryId)).find(ResBuyItemNum.class);
                    for (int k=0;k<resBuyItemNumList.size();k++){
                        String goodId = resBuyItemNumList.get(k).getGoodId();
                        int buyNum = resBuyItemNumList.get(k).getBuyNum();
                        for (int m=0;m<dataList.getData().get(j).getGoodsItemList().size();m++){
                            if (Integer.parseInt(goodId) == dataList.getData().get(j).getGoodsItemList().get(m).getGoodId())
                                dataList.getData().get(j).getGoodsItemList().get(m).setBuyNum(buyNum);
                        }
                    }
                }
            }
        }

        int i = 0;
        int j = 0;
        boolean isFirst;
        for (GoodsListBean.GoodsCategoryBean dataItem : dataList.getData()) {
            goodsCategoryList.add(dataItem);
            isFirst = true;
            for (GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean : dataItem.getGoodsItem()) {
                if (isFirst) {
                    categoryFirstItemPosi.add(j);
                    isFirst = false;
                }
                j++;
                goodsItemBean.setId(i);
                goodsItemBean.setCategoryId(dataItem.getCategoryId());               //同一个范畴下的商品的categoryId是一样的
                goodsItemList.add(goodsItemBean);
            }
            i++;
        }
        mGoodsCategoryRecyclerAdapter = new GoodsCategoryRecyclerAdapter(goodsCategoryList,getActivity());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        goodsCategoryRecycler.setLayoutManager(linearLayoutManager);
        goodsCategoryRecycler.setAdapter(mGoodsCategoryRecyclerAdapter);
        mGoodsCategoryRecyclerAdapter.setOnItemClickListener(new GoodsCategoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //如果直接使用recyclerView的scrollToPosition有些情况下不会置顶该位置，所以用这种方法
                mLinearLayoutManager.scrollToPositionWithOffset(categoryFirstItemPosi.get(position),0);
                mGoodsCategoryRecyclerAdapter.setCheckPosition(position);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        goodsItemRecycler.setLayoutManager(mLinearLayoutManager);
        goodsItemRecyclerAdapter = new GoodsItemRecyclerAdapter(getActivity(),goodsItemList,goodsCategoryList);
        headerAdapter = new BigramHeaderAdapter(getActivity(),goodsItemList,goodsCategoryList);
        top = new StickyHeadersBuilder()
                .setAdapter(goodsItemRecyclerAdapter)
                .setRecyclerView(goodsItemRecycler)
                .setStickyHeadersAdapter(headerAdapter)
                .setOnHeaderClickListener(this)
                .build();
        goodsItemRecycler.addItemDecoration(top);
        goodsItemRecycler.setAdapter(goodsItemRecyclerAdapter);
        goodsItemRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i=0;i<categoryFirstItemPosi.size();i++){
                    if(mLinearLayoutManager.findFirstVisibleItemPosition() >= categoryFirstItemPosi.get(i)){
                        mGoodsCategoryRecyclerAdapter.setCheckPosition(i);
                        goodsCategoryRecycler.smoothScrollToPosition(i);
                    }
                }

            }
        });

    }

    @Override
    public void onHeaderClick(View header, long headerId) {
        TextView text = (TextView)header.findViewById(R.id.goods_category_title);
        Toast.makeText(getActivity(), "Click on " + text.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加 或者  删除  商品发送的消息处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GoodsListEvent event) {
        if(event.buyNums.length>0){
            for (int i=0;i<event.buyNums.length;i++){
                goodsCategoryList.get(i).setBuyNum(event.buyNums[i]);
            }
            mGoodsCategoryRecyclerAdapter.changeData(goodsCategoryList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
