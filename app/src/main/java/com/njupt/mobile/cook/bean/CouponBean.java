package com.njupt.mobile.cook.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * 红包
 */
public class CouponBean implements Serializable {
    //门槛
    @SerializedName("mini_consume")
    private double miniPrice;
    //折扣
    @SerializedName("amount")
    private double decise;
    //红包名
    private String name;

    public CouponBean(double miniPrice, double decise, String name) {
        this.miniPrice = miniPrice;
        this.decise = decise;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDecise() {
        return decise;
    }

    public void setDecise(double decise) {
        this.decise = decise;
    }

    public double getMiniPrice() {
        return miniPrice;
    }

    public void setMiniPrice(double miniPrice) {
        this.miniPrice = miniPrice;
    }
}
