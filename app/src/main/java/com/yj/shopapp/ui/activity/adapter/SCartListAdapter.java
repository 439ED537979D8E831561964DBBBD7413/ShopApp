package com.yj.shopapp.ui.activity.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yj.shopapp.ubeen.gMinMax;
import com.yj.shopapp.ui.activity.shopkeeper.SCartListActivity;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jm on 2016/5/12.
 */
public class SCartListAdapter implements IRecyclerViewIntermediary {


    private SCartListActivity mContext;

    private List<CartList> notes;

    private CardListRecyclerView mListener;

    private List<Integer> chooseArray;
    private List<gMinMax> gMinMaxes;
    int minnum, maxnum;

    public SCartListAdapter(SCartListActivity context, List<CartList> noteList, CardListRecyclerView myItemClickListener, List<Integer> chooseArray, List<gMinMax> gMinMaxes) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;

        this.chooseArray = chooseArray;
        this.gMinMaxes = gMinMaxes;
    }

    /**
     * 当前点击操作的加减数量数否已经选中
     *
     * @return
     */
    private boolean isselect(int position) {


        return chooseArray.get(position) == 1 ? true : false;

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
        return new HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int quantity = Integer.parseInt(notes.get(position).getItemcount());
        final HomeFragmentViewHolder holder = (HomeFragmentViewHolder) viewHolder;
        final CartList cartList = notes.get(position);
        // final double prise=Double.parseDouble(cartList.getPrice());
        holder.toolNumberTxt.setText(quantity + "");
//        holder.shopsnameTv.setText(cartList.getBigtypename());
        holder.goodsnameTv.setText(cartList.getName());
        holder.numberTv.setText(cartList.getItemnumber());
        holder.priceTv.setText(cartList.getMoneysum());
        holder.unitpriceTv.setText("￥" + cartList.getPrice());
        Uri imageUri = Uri.parse(cartList.getImgurl());
        //开始下载
        holder.simpleDraweeView.setImageURI(imageUri);
        holder.addRe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!gMinMaxes.get(position).getMaxnum().equals("")) {
                    maxnum = Integer.parseInt(gMinMaxes.get(position).getMaxnum());
                }
                final KProgressHUD progressDialog = mContext.growProgress(Contants.Progress.SUMBIT_ING);
                int quantity1 = Integer.parseInt(holder.toolNumberTxt.getText().toString());
                Log.e("m_tag","maxnum"+maxnum);
                if (maxnum != 0) {
                    if (quantity1 < maxnum) {
                        quantity1++;
                    }
                } else {
                    quantity1++;
                }

                final int quantity2 = quantity1;
                mContext.changeNumber(cartList.getId(), quantity1 + "", new OkHttpResponseHandler<String>(mContext) {

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
                        Gson gson = new Gson();
                        Log.d("m_tag", "+++++" + json);
                        if (JsonHelper.isRequstOK(json, mContext)) {
                            CartRequest request1 = gson.fromJson(json, CartRequest.class);
                            holder.toolNumberTxt.setText(quantity2 + "");
                            holder.priceTv.setText(request1.getMoneysum());
                            cartList.setItemcount(quantity2 + "");
                            cartList.setMoneysum(request1.getMoneysum());

                            if (isselect(position)) {
                                if (mListener != null) {
//                    mListener.onItemClick(getPosition());

                                    mListener.chooseItem(position, 1);


                                }
                                //  mContext.carttotalpriceTv.setText( holder.priceTv.getText().toString());
                            }
                        } else {
                            Toast.makeText(mContext, JsonHelper.errorMsg(json), Toast.LENGTH_SHORT).show();
                        }//
                    }
                });

            }
        });
        holder.lessenRe.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (!gMinMaxes.get(position).getMinnum().equals("")) {
                    minnum = Integer.parseInt(gMinMaxes.get(position).getMinnum());
                }
                final KProgressHUD progressDialog = mContext.growProgress(Contants.Progress.SUMBIT_ING);
                int quantity1 = Integer.parseInt(holder.toolNumberTxt.getText().toString());
                if (quantity1 == 1) {
                    return;
                }
                Log.e("m_tag","minnum"+minnum);
                if (minnum != 0) {
                    if (quantity1 > minnum) {
                        quantity1--;
                    }
                } else {
                    quantity1--;
                }
                final int quantity2 = quantity1;
                mContext.changeNumber(cartList.getId(), quantity1 + "", new OkHttpResponseHandler<String>(mContext) {

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
                        Gson gson = new Gson();
                        Log.d("m_tag", json);
                        if (JsonHelper.isRequstOK(json, mContext)) {
                            CartRequest request1 = gson.fromJson(json, CartRequest.class);
                            holder.toolNumberTxt.setText(quantity2 + "");
                            holder.priceTv.setText(request1.getMoneysum());
                            cartList.setItemcount(quantity2 + "");
                            cartList.setMoneysum(request1.getMoneysum());
                            if (isselect(position)) {
                                if (mListener != null) {
//                    mListener.onItemClick(getPosition());

                                    mListener.chooseItem(position, 1);


                                }
                                //  mContext.carttotalpriceTv.setText( holder.priceTv.getText().toString());
                            }
                        }
                    }
                });

            }
        });
        if (chooseArray.get(position) == 0) {
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
        } else {
            holder.choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
        }

    }


    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.choose)
        ImageView choose;
        @BindView(R.id.choose_re)
        RelativeLayout chooseRe;
        @BindView(R.id.simpleDraweeView)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.goodsnameTv)
        TextView goodsnameTv;
        @BindView(R.id.unitpriceTv)
        TextView unitpriceTv;
        @BindView(R.id.numberTv)
        TextView numberTv;
        @BindView(R.id.jine_txt)
        TextView jineTxt;
        @BindView(R.id.priceTv)
        TextView priceTv;
        @BindView(R.id.add_txt)
        ImageView addTxt;
        @BindView(R.id.lessen_re)
        RelativeLayout lessenRe;
        @BindView(R.id.tool_number_txt)
        TextView toolNumberTxt;
        @BindView(R.id.lessen_txt)
        ImageView lessenTxt;
        @BindView(R.id.add_re)
        RelativeLayout addRe;
        @BindView(R.id.num_re)
        RelativeLayout numRe;


        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            choose.setOnClickListener(this);
            chooseRe.setOnClickListener(this);
//            choose_item.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.choose || v.getId() == R.id.choose_re) {

                if (chooseArray.get(getPosition()) == 0) {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_hook));
                    chooseArray.set(getPosition(), 1);
                    mListener.chooseItem(getPosition(), 1);
                } else {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
                    chooseArray.set(getPosition(), 0);
                    mListener.chooseItem(getPosition(), 0);
                }

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

