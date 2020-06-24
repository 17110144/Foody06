package hcmute.edu.vn.foody06.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import hcmute.edu.vn.foody06.R;
import hcmute.edu.vn.foody06.adapter.RecyclerViewAdapter;
import hcmute.edu.vn.foody06.model.Database;
import hcmute.edu.vn.foody06.model.Quan;

public class MainActivity extends AppCompatActivity {

    public static Database db;
    RecyclerView rvQuan;
    ArrayList<Quan> arrayQuan;
    RecyclerViewAdapter quanAdapter;
    Button btnTimQuan;

    Button btnTinhthanh;
    public static String tenTinhThanh="TP HCM";
    public static int curTime;
    final public static int REQUEST_PHONE_CALL_LOCATION = 0144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        Calendar c = Calendar.getInstance();
        @SuppressLint({"SimpleTimeFormat", "SimpleDateFormat"}) SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String ThoiGianHienTai = df.format(c.getTime()).replace(":", "");
        curTime = Integer.parseInt(ThoiGianHienTai);

        createDataBase();
        //insertData();

        btnTinhthanh =  findViewById(R.id.btn_chonthanhpho);
        rvQuan =  findViewById(R.id.recyclerview_main);
        btnTimQuan =  findViewById(R.id.btntimkiem);


        btnTinhthanh.setText(tenTinhThanh);
        arrayQuan = new ArrayList<>();
        getAllDataQuanTheoTinhThanh();
        quanAdapter = new RecyclerViewAdapter(this, arrayQuan);
        rvQuan.setLayoutManager(new GridLayoutManager(this, 2));
        rvQuan.setAdapter(quanAdapter);





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
        arrayQuan = new ArrayList<>();
        getAllDataQuanTheoTinhThanh();
        quanAdapter = new RecyclerViewAdapter(this, arrayQuan);
        rvQuan.setLayoutManager(new GridLayoutManager(this, 2));
        rvQuan.setAdapter(quanAdapter);
        super.onPostResume();
    }

    private void createDataBase(){
        db = new Database(this,"foody06.sqlite",null,1);
        db.QueryData("CREATE TABLE IF NOT EXISTS QUAN(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenQuan VARCHAR(200), AnhDaiDien VARCHAR(500), DiaChi VARCHAR(200), ThanhPho VARCHAR(50), LoaiHinh VARCHAR(50), GiaThapNhat VARCHAR(20), GiaCaoNhat VARCHAR(20), GioMoCua VARCHAR(20), GioDongCua VARCHAR(20), Wifi VARCHAR(50), MatKhauWifi VARCHAR(50), SDT VARCHAR(20),GioiThieu VARCHAR(300));");
        db.QueryData("CREATE TABLE IF NOT EXISTS MON (Id INTEGER PRIMARY KEY AUTOINCREMENT,TenMon VARCHAR (200), IdQuan INTEGER REFERENCES QUAN (Id),DanhMuc VARCHAR (100),Gia VARCHAR (20), HinhAnh VARCHAR (500));");
    }

    private void insertData(){
        //Thêm dữ liệu Quán xuống database
        //1->10
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Bún bò Huế Cô Mười', 'https://i.ibb.co/YP97BZ8/bunbohue.jpg','123 Võ Văn Ngân Quận Thủ Đức TP HCM','TP HCM','Quán ăn','20.000','50.000','6:00','22:00','BunBoCo10','11223344','097456421','Bún bò gốc Huế với hương vị đậm đà')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Thiên Lý - Cơm Niêu Singapore - Cửu Long', 'https://i.ibb.co/zZHNQq9/thienly.png','87A Cửu Long, P. 15, Quận 10, TP HCM','TP HCM','Quán ăn','25.000','100.000','10:00','21:00','ComNieuThienLy','444555666','097456422','Cơm niêu với cách chế biến theo hương vị Singapore')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Mỳ Quảng Cô Ba', 'https://i.ibb.co/98xCHw4/miquangga.jpg','333 Đặng Văn Bi Quận Thủ Đức TP HCM','TP HCM','Quán ăn','20.000','100.000','6:00','10:00','MyQuangCo3','678678678','097456423','Hương vị đậm đà xứ Quảng')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Thìn', 'https://i.ibb.co/5hW8vMh/phobohanoi.jpg','1 Phố Hàng Bún Hà Nội','Hà Nội','Quán ăn','35.000','70.000','6:30','22:30','PhoThin','678678678','097456424','Phở Gia Truyền Hà Nội');");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Phở Gà Cô Linh', 'https://i.ibb.co/s915wSw/phogahanoi.jpg','12 Phố Hàng Mã Hà Nội','Hà Nội','Quán ăn','35.000','70.000','6:30','10:30','PhoHai','123321321','097456424','Phở Gia Truyền Hà Nội');");

        //6->10
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Tiệm Bánh Thanh Xuân', 'https://i.ibb.co/7V8mkRB/cake.jpg','104 K8 Ngõ 6B Thành Công, Quận Ba Đình, Hà Nội','Hà Nội','Ăn vặt','30.000','70.000','09:00','20:00','ThanhXuan','22223333','0980241572','Tiệm bánh với nhiều loại bánh khác nhau, ngon và đẹp')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Chè Ngon Hà Nội & Món Ngon Đường Phố - Tô Hiệu', 'https://i.ibb.co/SJ9rH7r/che.jpg','297 Tô Hiệu, Quận Cầu Giấy, Hà Nội','Hà Nội','Ăn vặt','15.000','40.000','09:00','23:00','CheNgonHaNoi','CheNgonHaNoi123','sdt','Đa dạng loại chè từ truyền thống tới hiện đại')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'McDonald - Hồ Gươm', 'https://i.ibb.co/F7LSRmv/mac.jpg','2 Hàng Bài, Quận Hoàn Kiếm, Hà Nội','Hà Nội','Nhà hàng','50.000','200.000','08:00','22:00','McDonaldHoanKiem','123456789','sdt','Nhà hàng thức ăn nhanh')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Royaltea Vietnam By Hongkong - Giảng Võ', 'https://i.ibb.co/X7Y00Mr/roy.jpg','165 Giảng Võ, Quận Đống Đa, Hà Nội','Hà Nội','Cafe','25:00','65:00','08:00','21:30','Royaltea','123123123','sdt','Hương vị trà sữa đến từ HongKong')");
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Highlands Coffee - SG Centre', 'https://i.ibb.co/d5sg4dR/high.png','Tầng Hầm 2, Tầng Hầm 2 - 15 TTTM Takashimaya,92 - 94 Nam Kỳ Khởi Nghĩa,P. Bến Nghé,Quận 1,TP HCM','TP HCM','Cafe','9.000','65.000','09:30','21:00','Highlands','2lands123','sdt','Thương hiệu trải dài khắp đất nước')");

        //11->15
        db.QueryData("INSERT INTO QUAN VALUES(null, 'Cơm Tấm Phúc Lộc Thọ - Tô Ngọc Vân', 'https://i.ibb.co/0K46YLj/plt.jpg','124 - 132 Tô Ngọc Vân, P. Linh Tây, Quận Thủ Đức, TP HCM','TP HCM','Quán ăn','20.000','200.000','06:00','22:00','PhucLocTho','PLT778899','sdt','Hương vị đặc biệt, kích thích vị giác của bạn')");


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
        db.QueryData("INSERT INTO MON VALUES(null, 'Hồng trà kem cheese',9 ,'Tea', '30.000','https://i.ibb.co/vwy4BT6/roy3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Sữa tươi trân châu đường đen',9 ,'MilkTea', '25.000', 'https://i.ibb.co/ggD0yRL/roy4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà sữa trân châu đen',9 ,'MilkTea', '25.000', 'https://i.ibb.co/rdpDmP0/roy5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà hoa hồng Royal',9 ,'Tea', '30.000','https://i.ibb.co/zJnYCz5/roy6.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long dưa hấu kem cheese',9 ,'Tea', '35.000', 'https://i.ibb.co/37DrKJB/roy7.jpg');");
        //10.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê sữa đá',10 ,'Cafe', '35.000', 'https://i.ibb.co/f0HWtVz/high1.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê đen nóng',10 ,'Cafe', '30.000', 'https://i.ibb.co/xDtP9tL/high2.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cà phê sữa nóng',10 ,'Cafe', '32.000', 'https://i.ibb.co/Fw0dzcr/high3.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Campuchino nóng',10 ,'Cafe', '40.000', 'https://i.ibb.co/QM51qgj/high4.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà ô long hạt sen',10 ,'Tea', '45.000', 'https://i.ibb.co/5sYP65S/high5.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà đào',10, 'Tea', '40.000','https://i.ibb.co/wwj1TGJ/high6.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Trà vải khúc bạch',10 ,'Tea', '50.000', 'https://i.ibb.co/S7Xf886/high7.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh mỳ xíu mại',10 ,'Bánh mỳ', '30.000', 'https://i.ibb.co/rx4LrXr/high8.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Bánh mỳ thịt quay',10 ,'Bánh mỳ', '32.000', 'https://i.ibb.co/nwWVYjj/high9.png');");
        //11.
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn chả',11 ,'Cơm', '45.000', 'https://i.ibb.co/PrxyR3V/plt1.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn que',11 ,'Cơm', '60.000', 'https://i.ibb.co/VgC8h4D/plt2.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm sườn',11 ,'Cơm','32.000','https://i.ibb.co/gT7DBsN/plt3.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm đùi gà',11 ,'Cơm','35.000','https://i.ibb.co/YjhcDsR/plt4.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Cơm ba rọi',11 ,'Cơm','37.000','https://i.ibb.co/720J7qD/plt5.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Combo 2 người',11 ,'Combo', '139.000', 'https://i.ibb.co/YT4QQmQ/plt6.png');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Ba rọi thêm',11 ,'Đồ ăn thêm', '30.000', 'https://i.ibb.co/sVvKjCH/plt7.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Đùi thêm',11 ,'Đồ ăn thêm', '28.000', 'https://i.ibb.co/GvfpD7j/plt8.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nước ngọt sprite',11 ,'Nước', '15.000', 'https://i.ibb.co/bzRrVqz/plt9.jpg');");
        db.QueryData("INSERT INTO MON VALUES(null, 'Nước ngọt coca',11 ,'Nước', '15.000', 'https://i.ibb.co/v4HYsnj/plt10.jpg');");
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
}
