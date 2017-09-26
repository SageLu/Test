package com.retrofit.rx.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.retrofit.rx.ItemClickListener;
import com.retrofit.rx.R;
import com.retrofit.rx.bean.baseBean;

import java.util.List;

//import butterknife.BindView;
//import butterknife.ButterKnife;

/**
 * Created by sunxx on 2016/6/20.
 */
public class BaseBeanAdapter extends RecyclerView.Adapter<BaseBeanAdapter.BaseHolder>{
    private List<baseBean.Pic> list;
    private ItemClickListener mListener;
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.base_item, null);
        BaseHolder holder = new BaseHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(list.get(position).url).into(holder.img);
        holder.tv_who.setText(list.get(position).who);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }


    public class BaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{
      //  @BindView(R.id.img)
        ImageView img;
      //  @BindView(R.id.tv_who)
        TextView tv_who;
        public BaseHolder(View itemView) {
            super(itemView);
         //   ButterKnife.bind(this,itemView);
            img= (ImageView) itemView.findViewById(R.id.img);
            tv_who= (TextView) itemView.findViewById(R.id.tv_who);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener!=null){
                mListener.onItemLongClick(v,getPosition());
            }
            return true;
        }
    }
    public void setData(baseBean mBean) {
        list = mBean.results;
        notifyDataSetChanged();
    }
}

