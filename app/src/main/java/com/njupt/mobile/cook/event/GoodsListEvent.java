package com.njupt.mobile.cook.event;

/**
 * 消息事件
 */
public class GoodsListEvent {
    public int[] buyNums;

    public GoodsListEvent(int[] buyNums) {
        this.buyNums = buyNums;
    }
}
