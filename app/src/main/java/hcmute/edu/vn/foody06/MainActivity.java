package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Database db;
    List<Quan> lsQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tạo database
        //db = new Database(this,"foody.sqlite",null,1);

        //Tạo bảng theo tuần tự vì có quan hệ khóa ngoại
        //db.QueryData("CREATE TABLE IF NOT EXISTS QUAN(Id INTEGER PRIMARY KEY NOT NULL UNIQUE, Ten STRING NOT NULL, DiaChi STRING NOT NULL, ThanhPho STRING NOT NULL, LoaiHinh STRING NOT NULL, GioMoCua TIME NOT NULL, GioDongCua TIME NOT NULL, Wifi STRING, MatKhauWifi STRING) WITHOUT ROWID;");
        //db.QueryData("CREATE TABLE IF NOT EXISTS DANHMUCTHUCDON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdQuan INTEGER REFERENCES QUAN (Id) NOT NULL, TenDanhMuc STRING NOT NULL) WITHOUT ROWID;");
        //db.QueryData("CREATE TABLE IF NOT EXISTS MON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdDanhMuc INTEGER NOT NULL REFERENCES DanhMucThucDon (Id), Gia INTEGER NOT NULL, HinhAnh STRING) WITHOUT ROWID;");


        //hien thi view
        lsQuan = new ArrayList<>(  );
        lsQuan.add( new Quan( "Bạc sỉu đá" ,"Categories Coffee","Description ......",R.drawable.high1));
        lsQuan.add( new Quan( "Đen nóng" ,"Categories Coffee","Description ......",R.drawable.high2));
        lsQuan.add( new Quan( "Campuchino nóng" ,"Categories Coffee","Description ......",R.drawable.high3));
        lsQuan.add( new Quan( "Latte nóng" ,"Categories Coffee","Description ......",R.drawable.high4));
        lsQuan.add( new Quan( "Trà đào cam xả" ,"Categories Tea","Description ......",R.drawable.high6));
        lsQuan.add( new Quan( "Trà vải khúc bạch" ,"Categories Tea","Description ......",R.drawable.high7));
        lsQuan.add( new Quan( "Trà ô long hạt sen" ,"Categories Tea","Description ......",R.drawable.hig5));
        lsQuan.add( new Quan( "Bánh mỳ xíu mại" ,"Categories Food","Description ......",R.drawable.high8));
        lsQuan.add( new Quan( "Bánh mỳ thịt nướng" ,"Categories Food","Description ......",R.drawable.high9));
        lsQuan.add( new Quan( "Bánh mouse" ,"Categories Food","Description ......",R.drawable.high10));

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lsQuan);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
    }
}
