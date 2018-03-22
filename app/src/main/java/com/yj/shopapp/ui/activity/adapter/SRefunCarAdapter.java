package com.yj.shopapp.ui.activity.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.presenter.CardListRecyclerView;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ubeen.CartRequest;
import com.yj.shopapp.ui.activity.shopkeeper.SRefundCarList;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huanghao on 2016/11/21.
 */

public class SRefunCarAdapter implements IRecyclerViewIntermediary {



    private SRefundCarList mContext;

    private List<CartList> notes;

    private CardListRecyclerView mListener;

    private List<Integer> chooseArray;


    public SRefunCarAdapter(SRefundCarList context, List<CartList> noteList, CardListRecyclerView myItemClickListener,List<Integer> chooseArray) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;

        this.chooseArray = chooseArray;
    }

    /**
     * 当前点击操作的加减数量数否已经选中
     * @return
     */
    private boolean isselect(int position)
    {


        return chooseArray.get(position)==1?true:false;

    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int type) {
        View v = View.inflate(viewGroup.getContext(), R.layout.sactivity_cartlist_item, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new SRefunCarAdapter.HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int  quantity=Integer.parseInt(notes.get(position).getItemcount());
        final SRefunCarAdapter.HomeFragmentViewHolder holder = (SRefunCarAdapter.HomeFragmentViewHolder) viewHolder;
        final CartList cartList = notes.get(position);
        // final double prise=Double.parseDouble(cartList.getPrice());
        holder.tool_number_txt.setText(quantity+"");
        holder.shopsnameTv.setText(cartList.getBigtypename());
        holder.goodsnameTv.setText(cartList.getName());
        holder.numberTv.setText(cartList.getItemnumber());
        holder.priceTv.setText(cartList.getMoneysum());
        holder.unitpriceTv.setText("￥"+cartList.getPrice());
        Uri imageUri = Uri.parse(cartList.getImgurl());
        //开始下载
        holder.simpleDraweeView.setImageURI(imageUri);
        holder.add_re.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final KProgressHUD progressDialog = mContext.growProgress(Contants.Progress.SUMBIT_ING);
                int quantity1=Integer.parseInt(holder.tool_number_txt.getText().toString());

                quantity1++;
                final int quantity2=quantity1;
                mContext.changeNumber(cartList.getId(),quantity1+"",new OkHttpResponseHandler<String>(mContext){

                    @Override
                    public void onBefore() {
                        super.onBefore();
                        progressDialog.show();
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                    }

                    @Override
                    public void onResponse(Request request, String json) {
                        super.onResponse(request, json);
                        Gson gson=new Gson();

                        if (JsonHelper.isRequstOK(json,mContext)) {
                            CartRequest request1=gson.fromJson(json,CartRequest.class);
                            holder.tool_number_txt.setText(quantity2+"");

                            holder.priceTv.setText(request1.getMoneysum());
                            cartList.setItemcount(quantity2 + "");
                            cartList.setMoneysum(request1.getMoneysum());

                            if (isselect(position))
                            {
                                if (mListener != null) {
//                    mListener.onItemClick(getPosition());

                                    mListener.chooseItem(position,1);


                                }
                                //  mContext.carttotalpriceTv.setText( holder.priceTv.getText().toString());
                            }
                        }//
                    }
                });

            }
        });
        holder. lessen_re.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final KProgressHUD progressDialog = mContext.growProgress(Contants.Progress.SUMBIT_ING);
                int quantity1=Integer.parseInt(holder.tool_number_txt.getText().toString());
                if (quantity1==1)
                {return;}
                quantity1--;
                final int quantity2=quantity1;
                mContext.changeNumber(cartList.getId(),quantity1+"",new OkHttpResponseHandler<String>(mContext){

                    @Override
                    public void onBefore() {
                        super.onBefore();
                        progressDialog.show();
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                    }

                    @Override
                    public void onResponse(Request request, String json) {
                        super.onResponse(request, json);
                        if (JsonHelper.isRequstOK(json,mContext)) {
                            holder.tool_number_txt.setText(quantity2+"");
                            holder.priceTv.setText(quantity2*Double.parseDouble(cartList.getPrice())+"");
                            cartList.setItemcount(quantity2+"");
                            cartList.setMoneysum(quantity2*Double.parseDouble(cartList.getPrice())+"");
                            if (isselect(position))
                            {
                                if (mListener != null) {
//                    mListener.onItemClick(getPosition());

                                    mListener.chooseItem(position,1);


                                }
                                //  mContext.carttotalpriceTv.setText( holder.priceTv.getText().toString());
                            }
                        }
                    }
                });

            }
        });
        if(chooseArray.get(position)==0){
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
        }else{
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
        }

    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.choose_re)
        RelativeLayout choose_re;
        @BindView(R.id.choose)
        ImageView choose;
        @BindView(R.id.shopsnameTv)
        TextView shopsnameTv;
        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.numberTv)
        TextView numberTv;
        @BindView(R.id.priceTv)
        TextView priceTv;
        @BindView(R.id.add_re)
        RelativeLayout add_re;
        @BindView(R.id.lessen_re)
        RelativeLayout lessen_re;
        @BindView(R.id.tool_number_txt)
        TextView tool_number_txt;
        @BindView(R.id.unitpriceTv)
        TextView unitpriceTv;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            choose.setOnClickListener(this);
            choose_re.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if(v.getId()==R.id.choose_re){

                if(chooseArray.get(getPosition())==0){
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
                    chooseArray.set(getPosition(),1);
                    mListener.chooseItem(getPosition(),1);
                }else{
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
                    chooseArray.set(getPosition(),0);
                    mListener.chooseItem(getPosition(),0);
                }


//            }else{
//                if (mListener != null) {
////                    mListener.onItemClick(getPosition());
//                    if(chooseArray.get(getPosition())==0){
//                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
//                        chooseArray.set(getPosition(),1);
//                        mListener.chooseItem(getPosition(),1);
//                    }else{
//                        choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
//                        chooseArray.set(getPosition(),0);
//                        mListener.chooseItem(getPosition(),0);
//                    }
//
//                }
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (mListener != null) {
                mListener.onLongItemClick(getPosition());
            }
            return true;
        }
    }

}
