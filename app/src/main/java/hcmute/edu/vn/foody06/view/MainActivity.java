package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.QuanAdapter;
import hcmute.edu.vn.foody06.model.Database;
import hcmute.edu.vn.foody06.model.Quan;

public class MainActivity extends AppCompatActivity {

    public static Database db;
    RecyclerView rvQuan;
    ArrayList<Quan> arrayQuan;
    QuanAdapter quanAdapter;
    Button btnTimQuan;
    ProgressBar progressBar;
    Button btnTinhthanh;
    public static String tenTinhThanh;
    public static int curTime;
    final public static int REQUEST_PHONE_CALL_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDataBase();
        if(!haveDataInDB()){
            insertData();
        }
        requestPermission();
        tinhThoiGianHienTai();

        btnTinhthanh =  findViewById(R.id.btn_chonthanhpho);
        rvQuan =  findViewById(R.id.recyclerview_main);
        btnTimQuan =  findViewById(R.id.btntimkiem);
        progressBar = findViewById(R.id.progressBar_main);

        getTinhThanhWithGPS();

        //Xử lý sự kiện chọn tỉnh thành
        btnTinhthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chontinhthanh = new Intent(MainActivity.this, TinhThanhActivity.class);
                startActivity(chontinhthanh);
            }
        });

        //Xử lý xự kiện tìm kiếm quán
        btnTimQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timquan = new Intent(MainActivity.this, TimQuanActivity.class);
                startActivity(timquan);
            }
        });
    }


    //xử lý cho sự kiện chọn thành phố và quay về màn hình chính
    @Override
    protected void onPostResume() {
        btnTinhthanh.setText(tenTinhThanh);
        showData();
        super.onPostResume();
    }

    private void showData(){
        arrayQuan = new ArrayList<>();
        getAllDataQuanTheoTinhThanh();
        quanAdapter = new QuanAdapter(this, arrayQuan);
        rvQuan.setLayoutManager(new GridLayoutManager(this, 2));
        rvQuan.setAdapter(quanAdapter);
    }

    private void createDataBase(){
        db = new Database(this,"foody_06.sqlite",null,1);
        db.QueryData("CREATE TABLE IF NOT EXISTS QUAN(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenQuan VARCHAR(200), AnhDaiDien VARCHAR(500), DiaChi VARCHAR(200), ThanhPho VARCHAR(50), LoaiHinh VARCHAR(50), GiaThapNhat VARCHAR(20), GiaCaoNhat VARCHAR(20), GioMoCua VARCHAR(20), GioDongCua VARCHAR(20), Wifi VARCHAR(50), MatKhauWifi VARCHAR(50), SDT VARCHAR(20),GioiThieu VARCHAR(300));");
        db.QueryData("CREATE TABLE IF NOT EXISTS MON (Id INTEGER PRIMARY KEY AUTOINCREMENT,TenMon VARCHAR(200), IdQuan INTEGER REFERENCES QUAN (Id),DanhMuc VARCHAR (100),Gia VARCHAR (20), HinhAnh VARCHAR (500));");
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

    private void tinhThoiGianHienTai(){
        Calendar c = Calendar.getInstance();
        @SuppressLint({"SimpleTimeFormat", "SimpleDateFormat"}) SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String ThoiGianHienTai = df.format(c.getTime()).replace(":", "");
        curTime = Integer.parseInt(ThoiGianHienTai);
    }

    private void getTinhThanhWithGPS()
    {
        progressBar.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                int lastestLocationIndex = locationResult.getLocations().size() - 1;
                double Lat =  locationResult.getLocations().get(lastestLocationIndex).getLatitude();
                double Long =  locationResult.getLocations().get(lastestLocationIndex).getLongitude();

                //Get LatLong from Loation
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Lat, Long, 1);
                    tenTinhThanh = addresses.get(0).getAddressLine(0);
                    tenTinhThanh = chuanHoaTen(tenTinhThanh);
                    btnTinhthanh.setText(tenTinhThanh);
                    showData();

                } catch (IOException e) {
                    Log.e("GeocodingLocation", "Unable to connect to Geocoder", e);
                }
                progressBar.setVisibility(View.GONE);
            }
        }, Looper.getMainLooper());
    }
    private String chuanHoaTen(String ten){
        String[] str = ten.split(",");
        int maxindex = -1;
        for(String s: str){
            maxindex++;
        }
        ten = str[maxindex-1];
        ten = ten.trim();
        if(ten.equals("Hồ Chí Minh")){
            ten = "TP HCM";
        }
        else if(ten.equals("Bà Rịa - Vũng Tàu")){
            ten = "Vũng Tàu";
        }
        return ten;
    }

    private boolean haveDataInDB(){
        Cursor data = db.GetData("SELECT Id FROM Quan Where Id = 1;");
        return data.getCount() != 0;
    }

    private void insertData(){
        //Thêm dữ liệu xuống database
        //1->5
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bún bò Huế Cô Mười', 'https://i.ibb.co/YP97BZ8/bunbohue.jpg','123 Võ Văn Ngân Quận Thủ Đức TP HCM','TP HCM','Quán ăn','20.000','50.000','6:00','22:00','BunBoCo10','11223344','097456421','Bún bò gốc Huế với hương vị đậm đà')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Thiên Lý - Cơm Niêu Singapore - Cửu Long', 'https://i.ibb.co/zZHNQq9/thienly.png','87A Cửu Long, P. 15, Quận 10, TP. HCM','TP HCM','Quán ăn','25.000','100.000','10:00','21:00','ComNieuThienLy','444555666','097456422','Cơm niêu với cách chế biến theo hương vị Singapore')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Mỳ Quảng Cô Ba', 'https://i.ibb.co/98xCHw4/miquangga.jpg','333 Đặng Văn Bi Quận Thủ Đức TP HCM','TP HCM','Quán ăn','20.000','100.000','7:00','20:00','MyQuangCo3','678678678','097456423','Hương vị đậm đà xứ Quảng')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Thìn', 'https://i.ibb.co/5hW8vMh/phobohanoi.jpg','1 Phố Hàng Bún Hà Nội','Hà Nội','Quán ăn','35.000','70.000','6:30','22:30','PhoThin','678678678','097456424','Phở Gia Truyền Hà Nội');");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Gà Cô Linh', 'https://i.ibb.co/s915wSw/phogahanoi.jpg','12 Phố Hàng Mã Hà Nội','Hà Nội','Quán ăn','35.000','70.000','6:30','22:30','PhoHai','123321321','097456424','Phở Gia Truyền Hà Nội');");

        //6->10
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Tiệm Bánh Thanh Xuân', 'https://i.ibb.co/7V8mkRB/cake.jpg','104 K8 Ngõ 6B Thành Công, Quận Ba Đình, Hà Nội','Hà Nội','Ăn vặt','30.000','70.000','09:00','20:00','ThanhXuan','22223333','0980241572','Tiệm bánh với nhiều loại bánh khác nhau, ngon và đẹp')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Chè Ngon Hà Nội & Món Ngon Đường Phố - Tô Hiệu', 'https://i.ibb.co/SJ9rH7r/che.jpg','297 Tô Hiệu, Quận Cầu Giấy, Hà Nội','Hà Nội','Ăn vặt','15.000','40.000','09:00','23:00','CheNgonHaNoi','CheNgonHaNoi123','','Đa dạng loại chè từ truyền thống tới hiện đại')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'McDonald - Hồ Gươm', 'https://i.ibb.co/F7LSRmv/mac.jpg','2 Hàng Bài, Quận Hoàn Kiếm, Hà Nội','Hà Nội','Nhà hàng','50.000','200.000','08:00','22:00','McDonaldHoanKiem','123456789','','Nhà hàng thức ăn nhanh')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Royaltea Vietnam By Hongkong - Giảng Võ', 'https://i.ibb.co/X7Y00Mr/roy.jpg','165 Giảng Võ, Quận Đống Đa, Hà Nội','Hà Nội','Cafe','25:00','65:00','08:00','21:30','Royaltea','123123123','','Hương vị trà sữa đến từ HongKong')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Highlands Coffee - SG Centre', 'https://i.ibb.co/d5sg4dR/high.png','Tầng Hầm 2, Tầng Hầm 2 - 15 TTTM Takashimaya,92 - 94 Nam Kỳ Khởi Nghĩa,P. Bến Nghé,Quận 1,TP HCM','TP HCM','Cafe','9.000','65.000','09:30','21:00','Highlands','2lands123','','Thương hiệu trải dài khắp đất nước')");

        //11->15
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Cơm Tấm Phúc Lộc Thọ - Tô Ngọc Vân', 'https://i.ibb.co/0K46YLj/plt.jpg','124 - 132 Tô Ngọc Vân, P. Linh Tây, Quận Thủ Đức, TP. HCM','TP HCM','Quán ăn','20.000','200.000','06:00','22:00','PhucLocTho','PLT778899','','Hương vị đặc biệt, kích thích vị giác của bạn')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Thu Hằng - Cơm Văn Phòng', 'https://i.ibb.co/qDsxj2s/1.jpg','102 C16 Nguyễn Quý Đức, Quận Thanh Xuân, Hà Nội','Hà Nội','Quán ăn','45.000','50.000','10:00','21:00','ThuHang','1234567','','Cơm trưa-tối cho văn phòng')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bếp Việt - Cơm Văn Phòng - Khương Đình', 'https://i.ibb.co/WPNnPLd/1.png','10C Ngõ 376 Khương Đình, Quận Thanh Xuân, Hà Nội','Hà Nội','Quán ăn','35.000','85.000','09:00','13:00','BepViet','01010101','0999111333','Chuyên cơm trưa văn phòng')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Chicken July - Chân Gà Rút Xương & Ăn Vặt', 'https://i.ibb.co/187MSVN/1.jpg','95 Bế Văn Đàn, Quận Hà Đông, Hà Nội','Hà Nội','Ăn vặt','20.000','50.000','08:00','22:00','July','11223344','0222333444','Chuyên ăn vặt từ gà tới ốc')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Ăn vặt Hà Nội', 'https://i.ibb.co/0Fs1CSR/20.jpg','127 Bế Văn Đàn, Quận Hà Đông, Hà Nội','Hà Nội','Ăn vặt','25.000','150.000','08:00','22:00','ananan','999888111','','Sỉ lẻ các món ăn vặt ngon nhất Hà Nội')");

        //16->20
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Tea House - Trần Hưng Đạo', 'https://i.ibb.co/WnwSgGg/1.jpg','780 Trần Hưng Đạo, P. 7, Quận 5, TP. HCM','TP HCM','Cafe','23.000','55.000','09:00','22:00','TeaHouse','90909090','','Tận hưởng không gian thoải mái cùng ly trà ngon')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Patít - Trà & Bánh Ngọt', 'https://i.ibb.co/BsKG2zn/1.jpg','85 Đường Số 41, P.6, Quận 4, TP. HCM','TP HCM','Cafe','30.000','50.000','08:00','22:00','Patit','44556677','','Trà đài loan cùng bánh ngọt')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Nhà Ông Nhít - Đồ Tráng Miệng Người Hoa ', 'https://i.ibb.co/RTXWHJy/1.jpg','55 Nguyễn Phúc Chu, P. 15, Quận Tân Bình, TP. HCM','TP HCM','Ăn vặt','15.000','50.000','17:00','22:00','CheHoa','1234512345','','Chè người hoa với hương vị trung hoa')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Buzza Pizza - Nguyễn Trung Trực', 'https://i.ibb.co/0hPk3Dz/0.jpg','Tầng 3, Tầng 3, 5-7-9 Nguyễn Trung Trực, P. Bến Thành, Quận 1, TP. HCM','TP HCM','Nhà hàng','60.000','600.000','09:00','21:00','BuzzaPizza','19191919','0989111222','Đem đến cho bạn bữa ăn thượng hạng')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Healthy Food', 'https://i.ibb.co/vX5bHnC/0.jpg','Tầng 1, Tầng 1, B004 The Manor, Mễ Trì, Quận Nam Từ Liêm, Hà Nội','Hà Nội','Nhà hàng','100.000','500.000','08:00','22:00','HealthyFood','70707070','0111999222','Chuyên về các món Salad tốt cho sức khởe')");

        //21->25
        db.QueryData("INSERT INTO QUAN VALUES(null, 'ABC Pasta', 'https://i.ibb.co/0yxzHj6/0.jpg','17 - 19 Xã Đàn, Quận Đống Đa, Hà Nội','Hà Nội','Nhà hàng','150.000','250.000','16:00','22:00','ABC','abc12345','0888111222','Nhà hàng chuyên phục vụ các món pasta')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Simisi', 'https://i.ibb.co/z4L4YJq/0.jpg','18 Chùa Láng, Quận Đống Đa, Hà Nội','Hà Nội','Quán ăn','50.000','200.000','16:00','22:00','Simisi','101101','','Cửa hàng hàn quốc')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Dasushi', 'https://i.ibb.co/zVQy5w8/0.jpg','266 - 268 Nguyễn Văn Linh, Quận Thanh Khê, Đà Nẵng','Đà Nẵng','Nhà hàng','20.000','500.000','10:00','22:00','Dasushi','998877','','Nhà hàng sushi đậm chất nhật bản')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Cloudy - Nitrogen Ice Cream & Drinks', 'https://i.ibb.co/1R63QyL/7.jpg','91 Phan Châu Trinh, Quận Hải Châu, Đà Nẵng','Đà Nẵng','Cafe','10.000','30.000','08:00','22:00','Cloudy','112233','','Cửa hàng trà sữa Cloudy')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phố Cổ Hà Nội - Triệu Nữ Vương', 'https://i.ibb.co/5Y7PNw1/0.jpg','152 Triệu Nữ Vương, Quận Hải Châu, Đà Nẵng','Đà Nẵng','Ăn vặt','3.000','35.000','18:00','23:00','PhocoHN','123123','','Các món ăn vặt đậm chất hà nội')");

        //26->30
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bubble Tea & Fast Food - Trần Bình Trọng', 'https://i.ibb.co/XJGpxV8/0.jpg','88 Trần Bình Trọng, Quận Hải Châu, Đà Nẵng','Đà Nẵng','Quán ăn','15.000','50.000','16:00','22:00','Bubble','676767','','Quán ăn với hương vị châu âu')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Kem Baskin Robbins - Bạch Đằng', 'https://i.ibb.co/rZ03gh5/0.jpg','74 Bạch Đằng, Quận Hải Châu, Đà Nẵng','Đà Nẵng','Cafe','45.000','110.000','09:00','22:00','Kem','12341234','','Nhiều hương vị cho nhiều màu sắc')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Duc Thanh Bakery', 'https://i.ibb.co/9TdM2NN/0.jpg','91 Lý Thái Tổ, Quận Thanh Khê, Đà Nẵng','Đà Nẵng','Cafe','100.000','300.000','08:00','22:00','DucThanh','989898','','Chuyên các bánh Macaron')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Thai Market - Lê Hồng Phong', 'https://i.ibb.co/R6kHkzc/0.jpg','17 Lê Hồng Phong, Quận Hải Châu, Đà Nẵng','Đà Nẵng','Quán ăn','45.000','80.000','10:00','22:00','ThaiMarket','345345345','','Nhà hàng thái với các món đa dạng khác nhau')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phúc An Vegetarian & Coffee - Nhà Hàng Chay', 'https://i.ibb.co/ncsNVz3/0.jpg','547 Nguyễn Tất Thành, Quận Thanh Khê, Đà Nẵng','Đà Nẵng','Nhà hàng','40.000','100.000','09:00','21:00','PhucAn','789789789','0912345678','Nhà hàng chay phục vụ đa dạng món ăn')");





        //Dữ liệu món ăn của từng quán
        //1.
        db.QueryData("INSERT INTO MON VALUES(null, 'Bún bắp bò',1 ,'Bún bò', '35.000', 'https://i.ibb.co/wBXbq4L/bunbo1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bún bò tô chà bá lửa',1 ,'Bún bò', '45.000', 'https://i.ibb.co/LDwHLpR/bunbo2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bún bò nạm giò chả',1 ,'Bún bò', '40.000', 'https://i.ibb.co/qjwhYky/bunbo3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê sữa',1 ,'Thức uống', '25.000', 'https://i.ibb.co/zmS8LKB/bunbo4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà lê táo đỏ',1 ,'Thức uống', '30.000', 'https://i.ibb.co/XF837Xh/bunbo5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nước sắn dây quất',1 ,'Thức uống', '20.000', 'https://i.ibb.co/KFgkr5C/bunbo6.jpg');");
        //2.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm cháy xốt thịt bằm',2 ,'Món cơm', '35.000', 'https://i.ibb.co/0mZzC03/Thienly11.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cá bống kho tiêu',2 ,'Món chính', '45.000', 'https://i.ibb.co/FDfX2NR/thienly2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bò sốt nấm',2 ,'Món chính', '45.000', 'https://i.ibb.co/3Yw4Brq/thienly1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm cháy bò lúc lắc',2 ,'Món cơm', '35.000', 'https://i.ibb.co/17vXV2P/thienly12.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Mực rim nước mắm',2 ,'Món chính', '45.000', 'https://i.ibb.co/RcqmHcb/thienly4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trứng chiên',2 ,'Món chính', '25.000', 'https://i.ibb.co/dWwFy9X/thienly7.png');");
        //3.
        db.QueryData("INSERT INTO MON VALUES(null, 'Mỳ Quảng gà lớn',3 ,'Mỳ Quảng', '35.000', 'https://i.ibb.co/rGJbWZ4/myquang1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Mỳ Quảng tôm thịt',3 ,'Mỳ Quảng', '35.000', 'https://i.ibb.co/khFKw5t/myquang2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Mỳ Quảng cá lóc',3 ,'Mỳ Quảng', '35.000', 'https://i.ibb.co/MGG4MZZ/myquang3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh tráng cuốn thịt bắp',3 ,'Món cuốn', '100.000', 'https://i.ibb.co/1GmxQC3/myquang4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh tráng cuốn cá nục hấp',3 ,'Món cuốn', '84.000', 'https://i.ibb.co/RTKDvC0/myquang5.jpg');");
        //4.
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở tái gầu',4 ,'Phở bò', '55.000', 'https://i.ibb.co/x2h97YW/phothin1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở sốt vang',4 ,'Phở bò', '35.000', 'https://i.ibb.co/g75NHXr/phothin2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở thập cẩm',4 ,'Phở bò', '65.000', 'https://i.ibb.co/mJ6Hkps/phothin3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở tái nạm',4 ,'Phở bò', '45.000', 'https://i.ibb.co/4W6nd4D/phothin4.jpg');");
        //5.
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở khô lườn',5 ,'Phở gà', '40.000', 'https://i.ibb.co/G0BNZFk/phoga1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở gà đùi',5 ,'Phở gà', '45.000', 'https://i.ibb.co/72hDTKR/phoga2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở gà lườn',5 ,'Phở gà', '40.000', 'https://i.ibb.co/RvpcsHv/phoga3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bún thang lườn',5 ,'Bún', '40.000', 'https://i.ibb.co/NNZzdFc/phoga4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bún thang đùi',5 ,'Bún', '45.000', 'https://i.ibb.co/P1mtsG9/phoga5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở lòng mề gà',5 ,'Phở gà', '35.000', 'https://i.ibb.co/j6NqsSV/phoga6.jpg');");
        //6.
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh bông lan',6 ,'Bánh', '40.000', 'https://i.ibb.co/44p0zhT/cake1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh trân châu',6 ,'Bánh', '50.000', 'https://i.ibb.co/DYRB4zw/cake2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Tiramisu',6 ,'Bánh', '45.000', 'https://i.ibb.co/1bJ5BxF/cake3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh Sô cô la',6 ,'Bánh', '30.000', 'https://i.ibb.co/4T28V03/cak4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh su kem',6 ,'Bánh', '30.000', 'https://i.ibb.co/HVjYb3Z/cake6.jpg');");
        //7.
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè khúc bạch',7 ,'Chè', '35.000', 'https://i.ibb.co/ZXfkJwF/che1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè thái',7 ,'Chè', '25.000', 'https://i.ibb.co/mhbJB4x/che2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè hoa cau',7 ,'Chè', '20.000', 'https://i.ibb.co/Qr8TZ3J/che3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh flan trái cây',7 ,'Bánh flan', '20.000', 'https://i.ibb.co/ZNxW2pK/che4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè dừa non bánh lọt',7 ,'Chè', '25.000', 'https://i.ibb.co/nRVGqTv/che5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè đậu xanh bánh lọt',7 ,'Chè', '30.000', 'https://i.ibb.co/ZHXCF5L/che6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh flan hạt chia',7 ,'Bánh flan', '20.000', 'https://i.ibb.co/L902MXk/che7.jpg');");
        //8.
        db.QueryData("INSERT INTO MON VALUES(null, 'Chicken Burger',8 ,'Burger', '29.000', 'https://i.ibb.co/PWsjmPM/mac2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Triple Cheeseburger',8 ,'Burger', '76.000', 'https://i.ibb.co/YdSvrX0/mac4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo For1',8 ,'Combo', '79.000', 'https://i.ibb.co/pyr30wG/mac5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo For2C',8 ,'Combo', '179.000', 'https://i.ibb.co/Rb1y634/mac6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo For2A',8 ,'Combo', '129.000', 'https://i.ibb.co/4FjbrLG/mac7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, '9pcs Fried Chickens',8 ,'Fried Chicken', '289.000', 'https://i.ibb.co/XY2Rzs7/mac8.jpg');");
        //9.
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long đào Royal',9 ,'Tea', '35.000', 'https://i.ibb.co/zFky74K/roy1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà sữa kem cheese',9 ,'MilkTea', '29.000', 'https://i.ibb.co/XZ0m91W/roy2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Hồng trà kem cheese',9 ,'Tea',  '30.000','https://i.ibb.co/vwy4BT6/roy3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sữa tươi trân châu đường đen',9 ,'MilkTea', '25.000', 'https://i.ibb.co/ggD0yRL/roy4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà sữa trân châu đen',9 ,'MilkTea', '25.000', 'https://i.ibb.co/rdpDmP0/roy5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà hoa hồng Royal',9 ,'Tea',  '30.000','https://i.ibb.co/zJnYCz5/roy6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long dưa hấu kem cheese',9 ,'Tea', '35.000', 'https://i.ibb.co/37DrKJB/roy7.jpg');");
        //10.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê sữa đá',10 ,'Cafe', '35.000', 'https://i.ibb.co/f0HWtVz/high1.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê đen nóng',10 ,'Cafe', '30.000', 'https://i.ibb.co/xDtP9tL/high2.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê sữa nóng',10 ,'Cafe', '32.000', 'https://i.ibb.co/Fw0dzcr/high3.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Campuchino nóng',10 ,'Cafe', '40.000', 'https://i.ibb.co/QM51qgj/high4.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long hạt sen',10 ,'Tea', '45.000', 'https://i.ibb.co/5sYP65S/high5.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà đào',10 ,'Tea', '40.000','https://i.ibb.co/wwj1TGJ/high6.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà vải khúc bạch',10 ,'Tea', '50.000', 'https://i.ibb.co/S7Xf886/high7.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh mỳ xíu mại',10 ,'Bánh mỳ', '30.000', 'https://i.ibb.co/rx4LrXr/high8.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh mỳ thịt quay',10 ,'Bánh mỳ', '32.000', 'https://i.ibb.co/nwWVYjj/high9.png');");
        //11.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn chả',11 ,'Cơm', '45.000', 'https://i.ibb.co/PrxyR3V/plt1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn que',11 ,'Cơm', '60.000', 'https://i.ibb.co/VgC8h4D/plt2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn',11 ,'Cơm',  '32.000','https://i.ibb.co/gT7DBsN/plt3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm đùi gà',11 ,'Cơm',  '35.000','https://i.ibb.co/YjhcDsR/plt4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm ba rọi',11 ,'Cơm',  '37.000','https://i.ibb.co/720J7qD/plt5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo 2 người',11 ,'Combo', '139.000', 'https://i.ibb.co/YT4QQmQ/plt6.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ba rọi thêm',11 ,'Đồ ăn thêm', '30.000', 'https://i.ibb.co/sVvKjCH/plt7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Đùi thêm',11 ,'Đồ ăn thêm', '28.000', 'https://i.ibb.co/GvfpD7j/plt8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nước ngọt sprite',11 ,'Nước', '15.000', 'https://i.ibb.co/bzRrVqz/plt9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nước ngọt coca',11 ,'Nước', '15.000', 'https://i.ibb.co/v4HYsnj/plt10.jpg');");
        //12.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm gà chua ngọt',12 ,'Cơm', '40.000', 'https://i.ibb.co/096sDMC/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm bò sốt vang',12 ,'Cơm', '40.000', 'https://i.ibb.co/1MhRnZQ/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm thịt rang cháy cạnh',12 ,'Cơm', '35.000', 'https://i.ibb.co/zJt9d7n/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm gà rang gừng xả',12 ,'Cơm', '35.000', 'https://i.ibb.co/Pc318C8/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm cá kho tương',12 ,'Cơm', '30.000', 'https://i.ibb.co/ypBymNQ/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm cá chiên xù trứng cuộn',12 ,'Cơm', '40.000', 'https://i.ibb.co/sP5Bvv4/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm gà xào xả ớt',12 ,'Cơm', '35.000', 'https://i.ibb.co/1ZsJFCr/8.jpg');");
        //13.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm thịt rang chả lá lốt',13 ,'Cơm', '40.000', 'https://i.ibb.co/41GFXh1/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm thịt rang cháy cạnh',13 ,'Cơm', '40.000', 'https://i.ibb.co/Hg7gd3h/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm thịt kho trứng',13 ,'Cơm', '35.000', 'https://i.ibb.co/2Sv1Shg/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm trứng đúc thịt',13 ,'Cơm', '40.000', 'https://i.ibb.co/Ms5D6Ct/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm gà rang gừng',13 ,'Cơm', '35.000', 'https://i.ibb.co/HYB1s3r/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm tôm rang thịt',13 ,'Cơm', '45.000', 'https://i.ibb.co/Fm4YR94/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm gà rán',13 ,'Cơm', '42.000', 'https://i.ibb.co/tQfZ9jb/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà muối',13 ,'Thêm', '5.000', 'https://i.ibb.co/N6nZkBH/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Dưa muối',13 ,'Thêm', '5.000', 'https://i.ibb.co/tC3vVm1/10.jpg');");
        //14.
        db.QueryData("INSERT INTO MON VALUES(null, 'Chân gà rang muối rút xương',14 ,'Chân gà', '50.000', 'https://i.ibb.co/n7jPxmw/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chân gà mắm cay rút xương',14 ,'Chân gà', '45.000', 'https://i.ibb.co/qsSmjGY/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cánh gà chiên mắm',14 ,'Chân gà', '47.000', 'https://i.ibb.co/DMRpq9Y/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cánh gà rang muối',14 ,'Chân gà', '50.000', 'https://i.ibb.co/WKYc00H/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chân gà rút xương trộn thính',14 ,'Chân gà', '40.000', 'https://i.ibb.co/Jdx9dr3/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ốc vặn luộc',14 ,'Ốc', '30.000', 'https://i.ibb.co/KjCC5Wf/14.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ngao hấp dứa sả',14 ,'Ốc', '32.000', 'https://i.ibb.co/n8YpHjB/15.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ốc mít luộc',14 ,'Ốc', '35.000', 'https://i.ibb.co/TTs9Pp1/16.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ốc vặn xào me',14 ,'Ốc', '35.000', 'https://i.ibb.co/QNNRtyK/17.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ngao xào me',14 ,'Ốc', '30.000', 'https://i.ibb.co/5TKwvSQ/18.jpg');");
        //15.
        db.QueryData("INSERT INTO MON VALUES(null, 'Ăn gì cũng được 1',15 ,'Combo', '132.000', 'https://i.ibb.co/FYCtmhg/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ăn gì cũng được 2',15 ,'Combo', '99.000', 'https://i.ibb.co/0CpF53N/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ốc vặn luộc',15 ,'Ốc', '30.000', 'https://i.ibb.co/KjCC5Wf/14.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ốc vặn xào me',15 ,'Ốc', '30.000', 'https://i.ibb.co/QNNRtyK/17.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ngao xào cay',15 ,'Ốc', '30.000', 'https://i.ibb.co/QmHxXss/19.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cút lộn xào me',15 ,'Ăn vặt', '15.000', 'https://i.ibb.co/2NR5RcJ/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua rán',15 ,'Ăn vặt', '15.000', 'https://i.ibb.co/yXLDBJY/10.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Khoai lang kén',15 ,'Ăn vặt', '15.000', 'https://i.ibb.co/LxBTCq9/11.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gà xé lá chanh',15 ,'Ăn vặt', '15.000', 'https://i.ibb.co/dQ69HLD/12.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Xúc xích',15 ,'Ăn vặt', '15.000', 'https://i.ibb.co/dBQYw48/13.jpg');");
        //16.
        db.QueryData("INSERT INTO MON VALUES(null, 'Lục trà sữa dâu',16 ,'Tea', '35.000', 'https://i.ibb.co/CWyHh0Q/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Lục trà sữa Matcha',16 ,'Tea', '35.000', 'https://i.ibb.co/r23Wc0w/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Lục trà sữa Xoài',16 ,'Tea', '35.000', 'https://i.ibb.co/YZj4tvB/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà lài cam thạch anh đào',16 ,'Tea', '40.000', 'https://i.ibb.co/WcPwPQc/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà xanh hoa quả tươi',16 ,'Tea', '42.000', 'https://i.ibb.co/F6f7cHX/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Soda thanh long đỏ',16 ,'Soda', '45.000', 'https://i.ibb.co/TKCmqH6/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Soda thơm bưởi',16 ,'Soda', '40.000', 'https://i.ibb.co/dKSzfs7/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Soda chanh tắc',16 ,'Soda', '35.000', 'https://i.ibb.co/GkMF8Pq/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Yalkult dâu',16 ,'Soda', '45.000', 'https://i.ibb.co/Pm7QZ3F/10.jpg');");
        //17.
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà dâu tươi kem cheese',17 ,'Tea', '30.000', 'https://i.ibb.co/p2wgnF7/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Hồng trà kem phô mai',17 ,'Tea', '30.000', 'https://i.ibb.co/GCq8WWY/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Matcha kem phô mai',17 ,'Tea', '35.000', 'https://i.ibb.co/dGXYzvb/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà đào kem phô mai',17 ,'Tea', '35.000', 'https://i.ibb.co/b3pTGrv/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Lục trà vải',17 ,'Tea', '30.000', 'https://i.ibb.co/gJjQ8qg/6.jpg');");
        //18.
        db.QueryData("INSERT INTO MON VALUES(null, 'Chè dưỡng nhan',18 ,'Tráng miệng', '25.000', 'https://i.ibb.co/FxHmv0m/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Hột gà trà',18 ,'Tráng miệng', '25.000', 'https://i.ibb.co/6HcNb17/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh Flan',18 ,'Tráng miệng', '12.000', 'https://i.ibb.co/0CG0FQr/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pudding đậu nành trân châu đường đen',18 ,'Tráng miệng', '20.000', 'https://i.ibb.co/QmTx9NL/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cao quy vạn tiền',18 ,'Tráng miệng', '30.000', 'https://i.ibb.co/QvNhD5b/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua rán',18 ,'Ăn vặt', '20.000', 'https://i.ibb.co/whGKNCC/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Mực viên hàn quốc',18 ,'Ăn vặt', '20.000', 'https://i.ibb.co/0mKDXrL/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Hồ lô biến',18 ,'Ăn vặt', '15.000', 'https://i.ibb.co/9q2Fb9v/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Khoai lang kén',18 ,'Ăn vặt', '15.000', 'https://i.ibb.co/3fFzfvj/10.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Lục trà tắc',18 ,'Tráng miệng', '20.000', 'https://i.ibb.co/98QMyhJ/11.jpg');");
        //19.
        db.QueryData("INSERT INTO MON VALUES(null, 'Fried Chicken',19 ,'COMBO LA SMOKE', '77.000', 'https://i.ibb.co/0tjjxj6/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sausage Sandwich',19 ,'COMBO LA SMOKE', '165.000', 'https://i.ibb.co/VpB94jV/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Seafood Platter',19 ,'COMBO LA SMOKE', '429.000', 'https://i.ibb.co/FBxcjSZ/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'M Platter',19 ,'COMBO LA SMOKE', '539.000', 'https://i.ibb.co/cN4grgr/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Full Baby Ribs',19 ,'COMBO LA SMOKE', '605.000', 'https://i.ibb.co/dKV8yrS/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Half Platter Chicken Leg Quarter',19 ,'COMBO LA SMOKE', '242.000', 'https://i.ibb.co/B6MhXgD/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set Xúc Xích Anh Xông Khói - Sausage England Set',19 ,'COMBO LA SMOKE', '76.000', 'https://i.ibb.co/fpSHpwR/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo US - Sline Steak/ Bò Lát Đảo Lửa 1',19 ,'COMBO LA SMOKE', '72.000', 'https://i.ibb.co/zFRHJ6T/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'COMBO SIÊU NGON',19 ,'Combo2', '459.000', 'https://i.ibb.co/cJbKvhp/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'COMBO SIÊU THỊT',19 ,'Combo2', '569.000', 'https://i.ibb.co/2KrmxNg/11.jpg');");
        //20.
        db.QueryData("INSERT INTO MON VALUES(null, 'Set1',20 ,'Set', '550.000', 'https://i.ibb.co/PCRbnRS/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set2',20 ,'Set', '600.000', 'https://i.ibb.co/8M44FK3/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set3',20 ,'Set', '590.000', 'https://i.ibb.co/1Lry9Fy/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Green Salad',20 ,'Salad', '150.000', 'https://i.ibb.co/6JH2rTc/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chicken Salad',20 ,'Salad', '170.000', 'https://i.ibb.co/YTQ82Gd/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Buzza Salad',20 ,'Salad', '200.000', 'https://i.ibb.co/tq1XCLh/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Beef Salad',20 ,'Salad', '300.000', 'https://i.ibb.co/1ZZwrgd/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Butter garlic seafood salad',20 ,'Salad', '350.000', 'https://i.ibb.co/MST4txt/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Fried shrimp salad',20 ,'Salad', '400.000', 'https://i.ibb.co/h7SH0YT/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pumpkin soup',20 ,'Soup', '100.000', 'https://i.ibb.co/ZY30KVg/10.jpg');");
        //21.
        db.QueryData("INSERT INTO MON VALUES(null, 'Bolognese Pasta',21 ,'pasta', '123.000', 'https://i.ibb.co/7S9KR6f/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Meatball Pasta',21 ,'pasta', '125.000', 'https://i.ibb.co/X8FCSRS/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pescatore Pasta',21 ,'pasta', '150.000', 'https://i.ibb.co/qdX8PkP/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pomodoro Pasta',21 ,'pasta', '170.000', 'https://i.ibb.co/q7g5Bbg/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Seafood cheese oven pasta',21 ,'pasta', '200.000', 'https://i.ibb.co/nRJs7gx/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Seafood nurungu pasta',21 ,'pasta', '139.000', 'https://i.ibb.co/BZmKZHW/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Beef cheese oven pasta',21 ,'pasta', '147.000', 'https://i.ibb.co/1fhV3Ds/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Carbonara pasta',21 ,'pasta', '185.000', 'https://i.ibb.co/5WQdJs6/8.jpg');");
        //22.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm trộn',22 ,'', '40.000', 'https://i.ibb.co/rkZ9n4n/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Lẩu bạch tuộc',22 ,'', '200.000', 'https://i.ibb.co/w0Pd6r0/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gimbap',22 ,'', '35.000', 'https://i.ibb.co/9szSzQY/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bạch tuộc phô mai',22 ,'', '70.000', 'https://i.ibb.co/ZT1YXqs/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm trộn mực',22 ,'', '50.000', 'https://i.ibb.co/jgtnYzb/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm chiên kim chi bò',22 ,'', '45.000', 'https://i.ibb.co/CtxGK5h/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm cuộn trứng',22 ,'', '35.000', 'https://i.ibb.co/rpsZvPz/7.jpg');");
        //23.
        db.QueryData("INSERT INTO MON VALUES(null, 'Sashimi salad',23 ,'sushi', '105.000', 'https://i.ibb.co/BzK0j5d/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Avocado Salad',23 ,'sushi', '83.000', 'https://i.ibb.co/0KK0NRL/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Fish skin salad',23 ,'sushi', '94.000', 'https://i.ibb.co/4pwWWHy/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Tuna salad',23 ,'sushi', '65.000', 'https://i.ibb.co/Npv7FHw/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Grouper head',23 ,'sushi', '131.000', 'https://i.ibb.co/tBwr6G4/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cầu gai nhật',23 ,'sushi', '164.000', 'https://i.ibb.co/JH1Yjfp/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Uni deluxe sashimi',23 ,'sushi', '439.000', 'https://i.ibb.co/4YYVfxW/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Flying fish eggs',23 ,'sushi', '131.000', 'https://i.ibb.co/YZPYg17/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bụng cá hồi khè lửa',23 ,'sushi', '61.000', 'https://i.ibb.co/vHbChT0/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm nắm sò đỏ',23 ,'sushi', '61.000', 'https://i.ibb.co/RCQN1tW/10.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Swett shrimp',23 ,'sushi', '77.000', 'https://i.ibb.co/b1q4MRL/11.jpg');");
        //24.
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà sữa trân châu',24 ,'MilkTea', '10.000', 'https://i.ibb.co/yfr5SNF/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà xoài kem sữa',24 ,'MilkTea', '10.000', 'https://i.ibb.co/zrknj71/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sữa tươi trân châu đường đen',24 ,'MilkTea', '15.000', 'https://i.ibb.co/HYWgMMq/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà dưa hấu',24 ,'Tea', '20.000', 'https://i.ibb.co/9WjrTFR/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà hoa quả nhiệt đới',24 ,'Tea', '20.000', 'https://i.ibb.co/5n7knkM/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Milo dino flan',24 ,'MilkTea', '20.000', 'https://i.ibb.co/QnZ3TpZ/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long sen vàng',24 ,'Tea', '15.000', 'https://i.ibb.co/1mmWLh1/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sữa tươi kem trứng trân châu',24 ,'MilkTea', '25.000', 'https://i.ibb.co/NnvVJtw/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cacao sữa tươi trân châu',24 ,'MilkTea', '20.000', 'https://i.ibb.co/C7gKFTx/10.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà đào',24 ,'Tea', '15.000', 'https://i.ibb.co/0B4MQp7/11.jpg');");
        //25.
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo',25 ,'Ăn vặt', '159.000', 'https://i.ibb.co/qm3Yznh/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua rán',25 ,'Ăn vặt', '45.000', 'https://i.ibb.co/WBJtfRd/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua nhân phô mai',25 ,'Ăn vặt', '20.000', 'https://i.ibb.co/SPVMXRs/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua rán nguyên cây',25 ,'Ăn vặt', '50.000', 'https://i.ibb.co/vDdnYt7/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phô mai que',25 ,'Ăn vặt', '30.000', 'https://i.ibb.co/HBrKrkH/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cá viên',25 ,'Ăn vặt', '25.000', 'https://i.ibb.co/9TRP67z/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sữa tươi chiên',25 ,'Ăn vặt', '20.000', 'https://i.ibb.co/VM053Wp/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Chạo tôm',25 ,'Ăn vặt', '20.000', 'https://i.ibb.co/gg90DMp/8.jpg');");

        //26.
        db.QueryData("INSERT INTO MON VALUES(null, 'Hotdog nướng phô mai',26 ,'Ăn vặt', '25.000', 'https://i.ibb.co/Kssy86q/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem chua rán',26 ,'Ăn vặt', '30.000', 'https://i.ibb.co/5xR9gRG/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bắp xào tép',26 ,'Ăn vặt', '20.000', 'https://i.ibb.co/0mLzTxj/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phô mai que',26 ,'Ăn vặt', '15.000', 'https://i.ibb.co/T08x7s9/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pizza thịt bò',26 ,'Pizza', '50.000', 'https://i.ibb.co/m6dFb29/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pizza xúc xích',26 ,'Pizza', '45.000', 'https://i.ibb.co/9bsyS1X/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pizza thanh cua',26 ,'Pizza', '40.000', 'https://i.ibb.co/prHFwng/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm trộn hàn quốc',26 ,'Món hàn', '50.000', 'https://i.ibb.co/7G0VscD/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cheese Rabokki',26 ,'Món hàn', '50.000', 'https://i.ibb.co/z7LVqyp/9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Tokbokki đặc biệt',26 ,'Món hàn', '40.000', 'https://i.ibb.co/rdrb2Bf/10.jpg');");
        //27.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cookies Cream',27 ,'Kem', '65.000', 'https://i.ibb.co/3p9f73W/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Green Tea',27 ,'Kem', '65.000', 'https://i.ibb.co/4m1HVDm/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Mint Chocolate Chip',27 ,'Kem', '65.000', 'https://i.ibb.co/28fBfXs/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Vanilla',27 ,'Kem', '65.000', 'https://i.ibb.co/bdQ20YL/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Strawberry Cheesecake',27 ,'Kem', '65.000', 'https://i.ibb.co/TkhVsrJ/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Love Potion ',27 ,'Kem', '65.000', 'https://i.ibb.co/Hzf8G86/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'World Class Chocolate',27 ,'Kem', '65.000', 'https://i.ibb.co/khHZGNW/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Rainbow Sherbet',27 ,'Kem', '65.000', 'https://i.ibb.co/xM5Q3db/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Very Berry Strawberry',27 ,'Kem', '65.000', 'https://i.ibb.co/7GsydKn/9.jpg');");
        //28.
        db.QueryData("INSERT INTO MON VALUES(null, 'Set1',28 ,'Bánh', '300.000', 'https://i.ibb.co/nbxcVj9/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set2',28 ,'Bánh', '300.000', 'https://i.ibb.co/GpKWNnQ/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set3',28 ,'Bánh', '300.000', 'https://i.ibb.co/Dw4G3WY/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set4',28 ,'Bánh', '300.000', 'https://i.ibb.co/zfmzQ1p/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set5',28 ,'Bánh', '300.000', 'https://i.ibb.co/b2k0hPn/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set6',28 ,'Bánh', '150.000', 'https://i.ibb.co/41Hmfbh/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set7',28 ,'Bánh', '300.000', 'https://i.ibb.co/crHMFGC/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Set8',28 ,'Bánh', '300.000', 'https://i.ibb.co/QNhpWXg/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Crepe sầu riêng',28 ,'Bánh', '100.000', 'https://i.ibb.co/ThDPCj9/9.jpg');");
        //29.
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi miến hải sản',29 ,'Gỏi', '85.000', 'https://i.ibb.co/SPs9wsp/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi đu đủ',29 ,'Gỏi', '45.000', 'https://i.ibb.co/DGP1836/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi nghêu',29 ,'Gỏi', '70.000', 'https://i.ibb.co/9T91kTY/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi tôm sống',29 ,'Gỏi', '70.000', 'https://i.ibb.co/CnxSvxp/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi xoài xanh',29 ,'Gỏi', '60.000', 'https://i.ibb.co/SmZGN3n/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi thịt bò',29 ,'Gỏi', '80.000', 'https://i.ibb.co/k82q9gZ/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi hải sản',29 ,'Gỏi', '85.000', 'https://i.ibb.co/zZz9Myv/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Phở xào',29 ,'Cơm-Mỳ', '80.000', 'https://i.ibb.co/JymjvP0/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm chiên ruôc lạp xưởng',29 ,'Cơm-Mỳ', '70.000', 'https://i.ibb.co/JB4yHZ2/9.jpg');");
        //30.
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi bưởi',30 ,'Gỏi', '70.000', 'https://i.ibb.co/tCWD6nD/1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi hoa chuối',30 ,'Gỏi', '60.000', 'https://i.ibb.co/Lv3XpRP/2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Salad xà lách',30 ,'Gỏi', '60.000', 'https://i.ibb.co/pKvBDZR/3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nấm bào ngư salad',30 ,'Gỏi', '70.000', 'https://i.ibb.co/cCgsCmH/4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi đu đủ thái',30 ,'Gỏi', '65.000', 'https://i.ibb.co/ggXkrJG/5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Gỏi khổ qua nấm tuyết',30 ,'Gỏi', '60.000', 'https://i.ibb.co/7XSL4KQ/6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Miến trộn',30 ,'Khai vị', '65.000', 'https://i.ibb.co/cgZX8Nw/7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Pad Thái',30 ,'Khai vị', '70.000', 'https://i.ibb.co/p144G4B/8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nem rán',30 ,'Khai vị', '80.000', 'https://i.ibb.co/C1Z2cgP/9.jpg');");
    }
}
