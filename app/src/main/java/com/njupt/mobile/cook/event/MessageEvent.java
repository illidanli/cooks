package com.njupt.mobile.cook.event;

import com.njupt.mobile.cook.bean.GoodsListBean;

import java.util.List;

/**
 * 消息事件
 */
public class MessageEvent {
    public int num;
    public double price;
    public List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goods;

    public MessageEvent(int totalNum, double price, List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goods) {
        this.num = totalNum;
        this.price = price;
        this.goods = goods;
    }
}
