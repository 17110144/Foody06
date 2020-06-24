package hcmute.edu.vn.foody06.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hcmute.edu.vn.foody06.view.QuanActivity;
import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.model.Quan;

import static hcmute.edu.vn.foody06.view.MainActivity.curTime;

public class RecyclerViewAdapterTimKiem extends RecyclerView.Adapter<RecyclerViewAdapterTimKiem.MyViewHolder>  {

    private Activity mActivity;
    private Context mContext;
    private List<Quan> mData;


    public RecyclerViewAdapterTimKiem(Context mContext, Activity activity, List<Quan> mData) {
        this.mContext = mContext;
        this.mActivity = activity;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_ketquatimkiem_quan,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //Nhận dữ liệu và đổ vào view holder
        final Quan data = mData.get(position);
        final Location quanLocation = new Location("Vị trí quán");
        final Location curLocation = new Location("Vị trí hiện tại");

        holder.txtTenQuan.setText(data.getTenQuan());
        holder.txtDiaChi.setText(data.getDiaChi());
        holder.txtLoaiHinh.setText(data.getLoaiHinh());
        Picasso.with(this.mContext).load(data.getUrlAnhDaiDien())
                .into(holder.img_kqtkquan_thumbnail,new com.squareup.picasso.Callback(){

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


        //Xử lý tính khoảng cách
        holder.progressBar.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        LocationServices.getFusedLocationProviderClient(mActivity).requestLocationUpdates(locationRequest, new LocationCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(mActivity).removeLocationUpdates(this);
                int lastestLocationIndex = locationResult.getLocations().size() - 1;
                curLocation.setLatitude(locationResult.getLocations().get(lastestLocationIndex).getLatitude());
                curLocation.setLongitude(locationResult.getLocations().get(lastestLocationIndex).getLongitude());

                //Get LatLong from Loation
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                try {
                    List addressList = geocoder.getFromLocationName(data.getDiaChi(), 1);
                    Address address = (Address) addressList.get(0);

                    quanLocation.setLatitude(address.getLatitude());
                    quanLocation.setLongitude(address.getLongitude());

                } catch (IOException e) {
                    Log.e("GeocodingLocation", "Unable to connect to Geocoder", e);
                }
                float distance = curLocation.distanceTo(quanLocation)/1000;
                holder.txtKhoangCach.setText(String.format("%.2f",distance)+" km");
                holder.progressBar.setVisibility(View.GONE);
            }
        }, Looper.getMainLooper());

        //Xử lý tính trạng thái mở cửa và xử lý sự kiện cho nút chấm Xanh
        String gionmocua = data.getGioMoCua().replace(":", "");
        int time1 = Integer.parseInt(gionmocua);
        String giondongcua = data.getGioDongCua().replace(":", "");
        int time2 = Integer.parseInt(giondongcua);
        if (time1 < curTime && curTime < time2) {
            holder.img_trangthai.setBackgroundResource(R.drawable.ic_trangthai_mocua_24);
        } else {
            holder.img_trangthai.setBackgroundResource(R.drawable.ic_trangthai_dongcua_24);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    //Hiển thị các item trên màn hình MainActivity
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTenQuan,txtDiaChi,txtLoaiHinh,txtKhoangCach;
        ProgressBar progressBar;
        ImageView img_kqtkquan_thumbnail,img_trangthai;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTenQuan              = itemView.findViewById(R.id.txttenquan_ketquatimkiem);
            txtDiaChi               = itemView.findViewById(R.id.txtdiachi_quan_ketquatimkiem);
            txtLoaiHinh             = itemView.findViewById(R.id.txtloaihinh_quan_ketquatimkiem);
            txtKhoangCach           = itemView.findViewById(R.id.txtkhoangcach_quan_ketquatimkiem);
            img_kqtkquan_thumbnail  = itemView.findViewById(R.id.img_quan_ketquatimkiem);
            cardView                = itemView.findViewById(R.id.cardview_item_ketquatimkiem);
            progressBar             = itemView.findViewById(R.id.progressBar_ketquatimkiem);
            img_trangthai           = itemView.findViewById(R.id.img_trangthai_ketquatimkiem);
        }

    }

}
