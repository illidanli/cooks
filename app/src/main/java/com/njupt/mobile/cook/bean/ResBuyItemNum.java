package com.njupt.mobile.cook.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 记录买的菜品
 */
public class ResBuyItemNum extends DataSupport implements Serializable {
    private int id;
    private String resId;
    private String resName;
    private String categoryId;
    @SerializedName("gs_id")
    private String goodId;
    @SerializedName("quantity")
    private int buyNum;
    @SerializedName("gs_name")
    private String itemName;
    @SerializedName("gs_newprice")
    private double itemPrice;
    @SerializedName("gs_pic")
    private Integer itemImg;

    public static void add(String resId, String categoryId, String goodId, int buyNum, String itemName, double itemPrice, Integer itemImg, String resName){
        ResBuyItemNum resBuyItemNum = new ResBuyItemNum();
        resBuyItemNum.setResId(resId);
        resBuyItemNum.setCategoryId(categoryId);
        resBuyItemNum.setGoodId(goodId);
        resBuyItemNum.setBuyNum(buyNum);
        resBuyItemNum.setItemName(itemName);
        resBuyItemNum.setItemPrice(itemPrice);
        resBuyItemNum.setItemImg(itemImg);
        resBuyItemNum.setResName(resName);
        resBuyItemNum.save();

    }


    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getItemImg() {
        return itemImg;
    }

    public void setItemImg(Integer itemImg) {
        this.itemImg = itemImg;
    }

}
