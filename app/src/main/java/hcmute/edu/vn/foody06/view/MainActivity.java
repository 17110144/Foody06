package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.RecyclerViewAdapter;
import hcmute.edu.vn.foody06.model.Database;
import hcmute.edu.vn.foody06.model.Quan;

public class MainActivity extends AppCompatActivity {

    Database db;
    RecyclerView rvQuan;
    ArrayList<Quan> arrayQuan;
    RecyclerViewAdapter quanAdapter;

    Button btnTinhthanh;
    public static String tenTinhThanh="TP HCM";

    final public static int REQUEST_PHONE_CALL_LOCATION = 0144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        btnTinhthanh = (Button) findViewById(R.id.btn_chonthanhpho);
        rvQuan = (RecyclerView) findViewById(R.id.recyclerview_main);

        arrayQuan = new ArrayList<>();
        quanAdapter = new RecyclerViewAdapter(this,arrayQuan);
        rvQuan.setLayoutManager(new GridLayoutManager(this,2));
        rvQuan.setAdapter(quanAdapter);

        createDataBase();
        insertData();

        btnTinhthanh.setText(tenTinhThanh);
        getAllDataQuanTheoTinhThanh();



        //Xử lý sự kiện chọn tỉnh thành
        btnTinhthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chontinhthanh = new Intent(MainActivity.this,TinhThanhActivity.class);
                startActivity(chontinhthanh);
            }
        });

    }


    //xử lý cho sự kiện chọn thành phố và quay về màn hình chính
    @Override
    protected void onPostResume() {
        btnTinhthanh.setText(tenTinhThanh);
        arrayQuan = new ArrayList<>();
        quanAdapter = new RecyclerViewAdapter(this,arrayQuan);
        rvQuan.setLayoutManager(new GridLayoutManager(this,2));
        rvQuan.setAdapter(quanAdapter);
        getAllDataQuanTheoTinhThanh();
        super.onPostResume();
    }

    private void createDataBase(){
        db = new Database(this,"foody06.sqlite",null,1);
        db.QueryData("CREATE TABLE IF NOT EXISTS QUAN(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenQuan VARCHAR(200), AnhDaiDien VARCHAR(500), DiaChi VARCHAR(200), ThanhPho VARCHAR(50), LoaiHinh VARCHAR(50), GiaThapNhat VARCHAR(20), GiaCaoNhat VARCHAR(20), GioMoCua VARCHAR(20), GioDongCua VARCHAR(20), Wifi VARCHAR(50), MatKhauWifi VARCHAR(50), SDT VARCHAR(20),GioiThieu VARCHAR(300));");
        db.QueryData("CREATE TABLE IF NOT EXISTS DANHMUCTHUCDON (Id INTEGER PRIMARY KEY AUTOINCREMENT, IdQuan INTEGER REFERENCES QUAN (Id), TenDanhMuc VARCHAR(100));");
        db.QueryData("CREATE TABLE IF NOT EXISTS MON (Id INTEGER PRIMARY KEY AUTOINCREMENT, IdDanhMuc INTEGER  REFERENCES DanhMucThucDon (Id),TenMon VARCHAR(200), Gia VARCHAR(20), HinhAnh VARCHAR(500));");
    }

    private void insertData(){
        //Thêm dữ liệu xuống database
        //1->10
//        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bún bò Huế Cô Mười', 'https://i.ibb.co/YP97BZ8/bunbohue.jpg','123 Võ Văn Ngân Quận Thủ Đức TP HCM','TP HCM','Tiệm ăn','20000','40000','6:00','22:00','BunBoCo10','11223344','097456421','Bún bò gốc Huế với hương vị đậm đà')");
//        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bún riêu cua Cô Bảy', 'https://i.ibb.co/pxqb1L6/bunrieucua.jpg','321 Đỗ Xuân Hợp Quận 9 TP HCM','TP HCM','Tiệm ăn','22000','45000','6:00','21:00','BunRieuCo7','444555666','097456422','Bún riêu cua miền Nam hương vị hấp dẫn')");
//        db.QueryData("INSERT INTO QUAN VALUES(null, 'Mỳ Quảng Cô Ba', 'https://i.ibb.co/98xCHw4/miquangga.jpg','333 Đặng Văn Bi Quận Thủ Đức TP HCM','TP HCM','Tiệm ăn','20000','35000','7:00','20:00','MyQuangCo3','678678678','097456423','Hương vị đậm đà xứ Quảng')");
//        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Thìn', 'https://i.ibb.co/5hW8vMh/phobohanoi.jpg','1 Phố Hàng Bún Hà Nội','Hà Nội','Tiệm ăn','35000','70000','6:30','22:30','PhoThin','678678678','097456424','Phở Gia Truyền Hà Nội');");
//        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Gà Cô Linh', 'https://i.ibb.co/s915wSw/phogahanoi.jpg','12 Phố Hàng Mã Hà Nội','Hà Nội','Tiệm ăn','35000','70000','6:30','22:30','PhoHai','123321321','097456424','Phở Gia Truyền Hà Nội');");

    }

    private void getAllDataQuan(){
        Cursor dataQuan = db.GetData("SELECT * FROM QUAN");
        arrayQuan.clear();
        while (dataQuan.moveToNext()){
            String gioithieu = dataQuan.getString(13);
            String sdt = dataQuan.getString(12);
            String matkhauwifi = dataQuan.getString(11);
            String tenwifi = dataQuan.getString(10);
            String giodongcua = dataQuan.getString(9);
            String giomocua = dataQuan.getString(8);
            String giacaonhat = dataQuan.getString(7);
            String giathapnhat = dataQuan.getString(6);
            String loaihinh = dataQuan.getString(5);
            String thanhpho = dataQuan.getString(4);
            String diachi = dataQuan.getString(3);
            String anhdaidien = dataQuan.getString(2);
            String tenquan = dataQuan.getString(1);
            int id = dataQuan.getInt(0);
            arrayQuan.add(new Quan(id,tenquan,anhdaidien,diachi,thanhpho,loaihinh,giathapnhat,giacaonhat,giomocua,giodongcua,tenwifi,matkhauwifi,sdt,gioithieu));
        }
    }

    private void getAllDataQuanTheoTinhThanh(){
        Cursor dataQuan = db.GetData("SELECT * FROM QUAN Where ThanhPho ='"+tenTinhThanh+"';");
        arrayQuan.clear();
        while (dataQuan.moveToNext()){
            String gioithieu = dataQuan.getString(13);
            String sdt = dataQuan.getString(12);
            String matkhauwifi = dataQuan.getString(11);
            String tenwifi = dataQuan.getString(10);
            String giodongcua = dataQuan.getString(9);
            String giomocua = dataQuan.getString(8);
            String giacaonhat = dataQuan.getString(7);
            String giathapnhat = dataQuan.getString(6);
            String loaihinh = dataQuan.getString(5);
            String thanhpho = dataQuan.getString(4);
            String diachi = dataQuan.getString(3);
            String anhdaidien = dataQuan.getString(2);
            String tenquan = dataQuan.getString(1);
            int id = dataQuan.getInt(0);
            arrayQuan.add(new Quan(id,tenquan,anhdaidien,diachi,thanhpho,loaihinh,giathapnhat,giacaonhat,giomocua,giodongcua,tenwifi,matkhauwifi,sdt,gioithieu));
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PHONE_CALL_LOCATION);
    }

    private void loadImageFromURL(String url){
//        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(imageView,new com.squareup.picasso.Callback(){
//
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });
    }


}
