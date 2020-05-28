package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Database db;
    List<Quan> lstquan;

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
        lstquan = new ArrayList<>(  );
        lstquan.add( new Quan( "Bạc sỉu đá" ,"Categories Coffee","Description quan",R.drawable.high1));
        lstquan.add( new Quan( "Đen nóng" ,"Categories Coffee","Description quan",R.drawable.high2));
        lstquan.add( new Quan( "Campuchino nóng" ,"Categories Coffee","Description quan",R.drawable.high3));
        lstquan.add( new Quan( "Latte nóng" ,"Categories Coffee","Description quan",R.drawable.high4));
        lstquan.add( new Quan( "Trà đào cam xả" ,"Categories Tea","Description quan",R.drawable.high6));
        lstquan.add( new Quan( "Trà vải khúc bạch" ,"Categories Tea","Description quan",R.drawable.high7));
        lstquan.add( new Quan( "Trà ô long hạt sen" ,"Categories Tea","Description quan",R.drawable.hig5));
        lstquan.add( new Quan( "Bánh mỳ xíu mại" ,"Categories Food","Description quan",R.drawable.high8));
        lstquan.add( new Quan( "Bánh mỳ thịt nướng" ,"Categories Food","Description quan",R.drawable.high9));
        lstquan.add( new Quan( "Bánh mouse" ,"Categories Food","Description quan",R.drawable.high10));

        RecyclerView myrc = (RecyclerView) findViewById( R.id.recyclerview_id );
        RecyclerViewAdapter myadapter  = new RecyclerViewAdapter( this,lstquan );
        myrc.setLayoutManager( new GridLayoutManager( this,3 ) );
        myrc.setAdapter( myadapter );
    }
}
