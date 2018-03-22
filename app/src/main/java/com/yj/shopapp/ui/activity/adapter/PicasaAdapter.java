package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yj.shopapp.R;
import com.yj.shopapp.wbeen.Imglist;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.view.headfootrecycleview.IRecyclerViewIntermediary;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by jm on 2016/4/27.
 */
public class PicasaAdapter implements IRecyclerViewIntermediary {

    private Context mContext;

    private List<Imglist> notes;

    private BaseRecyclerView mListener;

    public PicasaAdapter(Context context, List<Imglist> noteList, BaseRecyclerView myItemClickListener) {

        mContext = context;

        notes = noteList;

        mListener = myItemClickListener;
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
        View v = View.inflate(viewGroup.getContext(), R.layout.activity_picase_item, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new HomeFragmentViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;  //any logic can go here
    }

    @Override
    public void populateViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        HomeFragmentViewHolder holder = (HomeFragmentViewHolder) viewHolder;
        Uri imageUri = Uri.parse(notes.get(position).getImgurl());
        //开始下载
        holder.simpleDraweeView.setImageURI(imageUri);

        holder.cameraStr.setText(notes.get(position).getStr());
        holder.cameraNumberid.setText(notes.get(position).getNumberid());
    }

    public class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        View view;
        @BindView(R.id.camera_iv)
        SimpleDraweeView simpleDraweeView;
        @BindView(R.id.camera_str)
        TextView cameraStr;
        @BindView(R.id.camera_numberid)
        TextView cameraNumberid;

        public HomeFragmentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getPosition());
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

//
//        extends  RecyclerView.Adapter<PicasaAdapter.ItemViewHolder> {
//
//    Context context;
//    List<string> list;
//    IOnLongItmClick onLongItmClick;
//
//    public PicasaAdapter(Context context, List<string> list, IOnLongItmClick onLongItmClick){
//        this.context = context;
//        this.list = list;
//        this.onLongItmClick = onLongItmClick;
//    }
//
//
//    @Override
//    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.activity_picase_item, null);
//        return new ItemViewHolder(view, viewType);//创建一个viewholder,然后将view传递进来！
//    }
//
//    @Override
//    public void onBindViewHolder(ItemViewHolder holder, final int position) {
//
//        Uri imageUri = Uri.parse(list.get(position));
//        //开始下载
//        holder.simpleDraweeView.setImageURI(imageUri);
//
//        holder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onLongItmClick.OnItemClick(position);
//            }
//        });
//
//        holder.simpleDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                onLongItmClick.OnLongItmClick(position);
//                return true;
//            }
//        });
//
//        holder.camerainfo.setText("位啊发顺丰近两年及司法");
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//
//}
