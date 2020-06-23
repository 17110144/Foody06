package hcmute.edu.vn.foody06.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hcmute.edu.vn.foody06.model.Mon;
import hcmute.edu.vn.foody06.R;

public class RecyclerViewAdapterMon extends RecyclerView.Adapter<RecyclerViewAdapterMon.MonViewHolder>  {

    private Context mContext;
    private List<Mon> mData;


    public RecyclerViewAdapterMon(Context mContext, List<Mon> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_mon,parent,false);

        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonViewHolder holder, final int position) {
        holder.txtTenMon.setText(mData.get(position).getTenMon());
        Picasso.with(this.mContext).load(mData.get(position).getURLHinhAnh())
                .into(holder.imgAnhMon,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //sự kiện click vào hình ảnh của 1 món, chọn món và thực hiện việc đặt món,...
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    //Hiển thị các item trên màn hình MainActivity
    public static class MonViewHolder extends RecyclerView.ViewHolder{

        TextView txtTenMon;
        ImageView imgAnhMon;
        CardView cardView;

        public MonViewHolder(View itemView) {
            super(itemView);

            txtTenMon =  itemView.findViewById(R.id.cardview_mon_txttenmon);
            imgAnhMon =  itemView.findViewById(R.id.cardview_mon_img);
            cardView =  itemView.findViewById(R.id.cardview_item_mon);
        }
    }

}
