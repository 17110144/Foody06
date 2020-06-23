package hcmute.edu.vn.foody06.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.RecyclerViewAdapter;
import hcmute.edu.vn.foody06.adapter.RecyclerViewAdapterMon;
import hcmute.edu.vn.foody06.model.Database;
import hcmute.edu.vn.foody06.model.Mon;
import hcmute.edu.vn.foody06.model.Quan;
import hcmute.edu.vn.foody06.service.Constants;
import hcmute.edu.vn.foody06.service.LocationIntentService;

import static hcmute.edu.vn.foody06.view.MainActivity.db;

public class QuanActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txtTenQuan, txtTinhThanh, txtTrangThai, txtGioMoCua, txtGioDongCua, txtDiaChi, txtKhoangCach, txtLoaiHinh, txtGiaThapNhat, txtGiaCaoNhat;
    private Button btnCall,btnBack,btnTenWifi, btnMatKhauWifi,btnThucDon;
    private int IdQuan;
    private String TenQuan,TinhThanh,GioMoCua,GioDongCua,DiaChi,LoaiHinh,GiaThapNhat,GiaCaoNhat,TenWifi,MatKhauWifi,SDT;
    private Location curLocation = new Location("Vị trí hiện tại");
    private Location quanLocation = new Location("Vị trí quán");


    RecyclerView rvMon;
    ArrayList<Mon> arrayMon;
    RecyclerViewAdapterMon monAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan);

        anhXa();
        nhanDuLieu();
        capNhatDuLieu();

        arrayMon = new ArrayList<>();
        getAllDataMonTheoQuan();
        monAdapter = new RecyclerViewAdapterMon(this,arrayMon);
        rvMon.setLayoutManager(new GridLayoutManager(this,2));
        rvMon.setAdapter(monAdapter);


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!kiemTraSDT()) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + SDT));
                if (ContextCompat.checkSelfPermission(QuanActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(QuanActivity.this, new String[]{Manifest.permission.CALL_PHONE},MainActivity.REQUEST_PHONE_CALL_LOCATION);
                }
                else
                {
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnTenWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapNhatWifi(TenWifi,MatKhauWifi,IdQuan);
            }
        });
        btnMatKhauWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapNhatWifi(TenWifi,MatKhauWifi,IdQuan);
            }
        });

        btnThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thucdon = new Intent(QuanActivity.this,ThucDonActivity.class);
                thucdon.putExtra("IdQuan",IdQuan);
                thucdon.putExtra("TenQuan",TenQuan);
                startActivity(thucdon);
            }
        });
    }

    public void CapNhatWifi(String ten, final String pass, final int id){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_wifi);

        final EditText editTenWifi = (EditText) dialog.findViewById(R.id.dialog_txtten_wifi);
        final EditText editMatKhauWifi = (EditText) dialog.findViewById(R.id.dialog_txtpass_wifi);
        Button btnXacNhan = (Button) dialog.findViewById(R.id.dialog_btncapnhat_wifi);


        editTenWifi.setText(ten);
        editMatKhauWifi.setText(pass);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMoi = editTenWifi.getText().toString().trim();
                String passMoi = editMatKhauWifi.getText().toString().trim();
                db.QueryData("UPDATE QUAN SET Wifi = '"+ tenMoi +"' WHERE Id = '"+ id +"'");
                db.QueryData("UPDATE QUAN SET MatKhauWifi = '"+ passMoi +"' WHERE Id = '"+ id +"'");
                Toast.makeText(QuanActivity.this,"Đã cập nhật!",Toast.LENGTH_SHORT).show();
                getWifiDataAndUpdateUI();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void testClickEvent()
    {
        Toast.makeText(this,"Clicked!",Toast.LENGTH_SHORT).show();
    }

    private void nhanDuLieu() {
        //Nhận dữ liệu từ Intent trước đó
        Intent intent = getIntent();
        IdQuan = Objects.requireNonNull(intent.getExtras()).getInt("IdQuan");
        TenQuan = intent.getExtras().getString("TenQuan");
        TinhThanh = intent.getExtras().getString("TinhThanh");
        GioMoCua = intent.getExtras().getString("GioMoCua");
        GioDongCua = intent.getExtras().getString("GioDongCua");
        DiaChi = intent.getExtras().getString("DiaChi");
        LoaiHinh = intent.getExtras().getString("LoaiHinh");
        GiaThapNhat = intent.getExtras().getString("GiaThapNhat");
        GiaCaoNhat = intent.getExtras().getString("GiaCaoNhat");
        TenWifi = intent.getExtras().getString("TenWifi");
        MatKhauWifi = intent.getExtras().getString("MatKhauWifi");
        SDT = intent.getExtras().getString("SDT");
    }

    @SuppressLint("SetTextI18n")
    private void capNhatDuLieu() {
        //Cập nhật dữ liệu lên giao diện
        txtTenQuan.setText(TenQuan);
        txtTinhThanh.setText(TinhThanh);
        txtGioMoCua.setText(GioMoCua);
        txtGioDongCua.setText(GioDongCua);
        txtTrangThai.setText(tinhTrangThaiMoCua(GioMoCua, GioDongCua));
        txtDiaChi.setText(DiaChi);
        tinhKhoangCach();
        txtLoaiHinh.setText(LoaiHinh);
        txtGiaThapNhat.setText(GiaThapNhat+"đ");
        txtGiaCaoNhat.setText(GiaCaoNhat+"đ");
        btnTenWifi.setText(TenWifi);
        btnMatKhauWifi.setText(MatKhauWifi);
    }

    private Boolean kiemTraSDT(){
        if(SDT.equals("")){
            Toast.makeText(this,"Quán chưa cập nhật số điện thoại!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void anhXa(){
        rvMon = findViewById(R.id.recyclerView_mon_quan);
        progressBar = findViewById(R.id.progressBar_quan);
        txtTenQuan = findViewById(R.id.txttilte_quan);
        txtTinhThanh = findViewById(R.id.txttinhthanh_quan);
        txtTrangThai = findViewById(R.id.txttrangthai_quan);
        txtGioMoCua = findViewById(R.id.txtthoigianmo_quan);
        txtGioDongCua = findViewById(R.id.txtthoigiandong_quan);
        txtDiaChi = findViewById(R.id.txtdiachi_quan);
        txtKhoangCach = findViewById(R.id.txtkhoangcach_quan);
        txtLoaiHinh = findViewById(R.id.txtloaihinh_quan);
        txtGiaThapNhat = findViewById(R.id.txtgiathapnhat_quan);
        txtGiaCaoNhat = findViewById(R.id.txtgiacaonhat_quan);
        btnTenWifi = findViewById(R.id.btnthem_tenwifi_quan);
        btnMatKhauWifi = findViewById(R.id.btnthem_passwifi_quan);
        btnCall = findViewById(R.id.btnlienhe_quan);
        btnBack = findViewById(R.id.btnBack_quan);
        btnThucDon = findViewById(R.id.btnthucdon_quan);
    }

    private String tinhTrangThaiMoCua(String ThoiGianMo,String ThoiGianDong) {
        ThoiGianMo = ThoiGianMo.replace(":", "");
        int time1 = Integer.parseInt(ThoiGianMo);
        ThoiGianDong = ThoiGianDong.replace(":", "");
        int time2 = Integer.parseInt(ThoiGianDong);

        Calendar c = Calendar.getInstance();
        @SuppressLint({"SimpleTimeFormat", "SimpleDateFormat"}) SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String ThoiGianHienTai = df.format(c.getTime()).replace(":", "");
        int curtime = Integer.parseInt(ThoiGianHienTai);

        if (time1 < curtime && curtime < time2) {
            return "Đang mở cửa";
        } else {
            return "Chưa mở cửa";
        }
    }

    private void tinhKhoangCach() {
        progressBar.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        LocationServices.getFusedLocationProviderClient(QuanActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(QuanActivity.this).removeLocationUpdates(this);
                int lastestLocationIndex = locationResult.getLocations().size() - 1;
                curLocation.setLatitude(locationResult.getLocations().get(lastestLocationIndex).getLatitude());
                curLocation.setLongitude(locationResult.getLocations().get(lastestLocationIndex).getLongitude());

                //Get LatLong from Loation
                Geocoder geocoder = new Geocoder(QuanActivity.this, Locale.getDefault());
                try {
                    List addressList = geocoder.getFromLocationName(DiaChi, 1);
                    Address address = (Address) addressList.get(0);
                    quanLocation.setLatitude(address.getLatitude());
                    quanLocation.setLongitude(address.getLongitude());

                } catch (IOException e) {
                    Log.e("GeocodingLocation", "Unable to connect to Geocoder", e);
                }
                float distance = curLocation.distanceTo(quanLocation)/1000;
                txtKhoangCach.setText(String.format("%.2f",distance)+" km");
                progressBar.setVisibility(View.GONE);
            }
        }, Looper.getMainLooper());
    }

    private void getAllDataMonTheoQuan(){
        Cursor dataQuan = db.GetData("SELECT * FROM Mon Where IdQuan ="+IdQuan+";");
        arrayMon.clear();
        while (dataQuan.moveToNext()){
            String urlanh = dataQuan.getString(5);
            String gia = dataQuan.getString(4);
            String danhmuc = dataQuan.getString(3);
            int idquan = dataQuan.getInt(2);
            String tenmon = dataQuan.getString(1);
            int id = dataQuan.getInt(0);
            arrayMon.add(new Mon(id,tenmon,idquan,danhmuc,gia,urlanh));
        }
    }

    private void getWifiDataAndUpdateUI(){
        Cursor datawifi = db.GetData("SELECT Wifi,MatKhauWifi FROM Quan Where Id ="+IdQuan+"");
        while (datawifi.moveToNext()){
            String passwifi = datawifi.getString(1);
            String tenwifi = datawifi.getString(0);
            btnTenWifi.setText(tenwifi);
            btnMatKhauWifi.setText(passwifi);
            TenWifi = tenwifi;
            MatKhauWifi = passwifi;
        }
    }
}
