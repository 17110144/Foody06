package hcmute.edu.vn.foody06.adapter;

import android.content.Context;
import android.content.Intent;
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

import hcmute.edu.vn.foody06.view.QuanActivity;
import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.model.Quan;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Quan> mData;


    public RecyclerViewAdapter(Context mContext, List<Quan> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_quan,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //final Context context = null;
        Quan data = mData.get(position);
        holder.txtTenQuan.setText(data.getTenQuan());
        holder.txtDescription.setText(data.getDescription());

        Picasso.with(this.mContext).load(data.getUrlAnhDaiDien())
                .into(holder.img_quan_thumbnail,new com.squareup.picasso.Callback(){

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
                Intent intent = new Intent(mContext, QuanActivity.class);
                Quan data = mData.get(position);
                //Gửi đữ liệu đến QuanActivity
                intent.putExtra("IdQuan",data.getIdQuan());
                intent.putExtra("TenQuan",data.getTenQuan());
                intent.putExtra("TinhThanh",data.getTinhThanh());
                intent.putExtra("GioMoCua",data.getGioMoCua());
                intent.putExtra("GioDongCua",data.getGioDongCua());
                intent.putExtra("DiaChi",data.getDiaChi());
                intent.putExtra("LoaiHinh",data.getLoaiHinh());
                intent.putExtra("GiaThapNhat",data.getGiaThapNhat());
                intent.putExtra("GiaCaoNhat",data.getGiaCaoNhat());
                intent.putExtra("TenWifi",data.getWifi());
                intent.putExtra("MatKhauWifi",data.getMatKhauWifi());
                intent.putExtra("SDT",data.getSDT());
                //Start the activity
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    //Hiển thị các item trên màn hình MainActivity
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTenQuan,txtDescription;
        ImageView img_quan_thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtTenQuan = itemView.findViewById(R.id.cardview_quan_title);
            txtDescription =  itemView.findViewById(R.id.cardview_quan_description);
            img_quan_thumbnail =  itemView.findViewById(R.id.cardview_quan_img);
            cardView =  itemView.findViewById(R.id.cardview_item_quan);
        }
    }
}
