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

import hcmute.edu.vn.foody06.view.MainActivity;
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

        holder.txtTenQuan.setText(mData.get(position).getTenQuan());
        holder.txtDescription.setText(mData.get(position).getDescription());

        Picasso.with(this.mContext).load(mData.get(position).getUrlAnhDaiDien()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
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
                //Gửi đữ liệu đến QuanActivity
                intent.putExtra("TenQuan",mData.get(position).getTenQuan());
                intent.putExtra("TinhThanh",mData.get(position).getTinhThanh());
                intent.putExtra("GioMoCua",mData.get(position).getGioMoCua());
                intent.putExtra("GioDongCua",mData.get(position).getGioDongCua());
                intent.putExtra("DiaChi",mData.get(position).getDiaChi());
                intent.putExtra("LoaiHinh",mData.get(position).getLoaiHinh());
                intent.putExtra("GiaThapNhat",mData.get(position).getGiaThapNhat());
                intent.putExtra("GiaCaoNhat",mData.get(position).getGiaCaoNhat());
                intent.putExtra("TenWifi",mData.get(position).getWifi());
                intent.putExtra("MatKhauWifi",mData.get(position).getMatKhauWifi());
                intent.putExtra("SDT",mData.get(position).getSDT());
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

            txtTenQuan = (TextView) itemView.findViewById(R.id.cardview_quan_title);
            txtDescription = (TextView)  itemView.findViewById(R.id.cardview_quan_description);
            img_quan_thumbnail = (ImageView) itemView.findViewById(R.id.cardview_quan_img);
            cardView = (CardView) itemView.findViewById(R.id.cardview_item_quan);
        }
    }

}
