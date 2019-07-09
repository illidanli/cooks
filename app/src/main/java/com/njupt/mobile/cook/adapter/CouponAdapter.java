package com.njupt.mobile.cook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njupt.mobile.cook.R;
import com.njupt.mobile.cook.activity.ResActivity;
import com.njupt.mobile.cook.bean.CouponBean;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{
    private Context mContext;
    private List<CouponBean> list;
    private OnUseBtnClickListener onUseBtnClickListener;
    private double allMoney;

    public CouponAdapter(Context context, double allMoney, List<CouponBean> list){
        this.mContext = context;
        this.list = list;
        this.allMoney = allMoney;
    }

    public interface OnUseBtnClickListener{
        void useBtnClickListener(int position, CouponBean couponBean);
    }

    public void setOnUseBtnClickListener(OnUseBtnClickListener onUseBtnClickListener){
        this.onUseBtnClickListener = onUseBtnClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        double price =list.get(position).getDecise()*10;
        holder.couponMoney.setText(price+"");
        holder.couponName.setText(list.get(position).getName()+"红包");
        holder.minUse.setText("满"+list.get(position).getMiniPrice()+"元可用");
        holder.use.setVisibility(View.VISIBLE);
        if (allMoney>=list.get(position).getMiniPrice()){
            if (onUseBtnClickListener != null){
                holder.use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUseBtnClickListener.useBtnClickListener(position,list.get(position));
                    }
                });
            }
            holder.use.setBackground(mContext.getResources().getDrawable(R.drawable.red_ban_yuan));
        }else{
            holder.use.setBackground(mContext.getResources().getDrawable(R.drawable.grey_ban_yuany));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.coupon_item,parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView couponName;
        TextView couponMoney;
        TextView minUse;
        TextView couponData;
        TextView use;
        public ViewHolder (View root){
            super(root);
            couponName = (TextView) root.findViewById(R.id.coupon_name);
            couponMoney = (TextView)root.findViewById(R.id.coupon_money);
            minUse = (TextView) root.findViewById(R.id.min_use);
            couponData = (TextView) root.findViewById(R.id.coupon_date);
            use = (TextView) root.findViewById(R.id.use);
        }
    }
}
