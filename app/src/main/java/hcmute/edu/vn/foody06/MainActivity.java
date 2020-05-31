package hcmute.edu.vn.foody06;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Database db;
    List<Quan> lsQuan;
    ImageView imageView;
    String url = "https://i.ibb.co/BcLXqxV/Ha-Noi-Banh-Trang-Sai-Gon-Xuan-La-25-Ngo36-Xuan-La-Tay-Ho-Ha-Noi.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.img_mon);
        loadImageFromURL(url);



        /*
        //Tạo database
        db = new Database(this,"foody.sqlite",null,1);

        //Tạo bảng theo tuần tự vì có quan hệ khóa ngoại IF NOT EXISTS
        db.QueryData("CREATE TABLE QUAN(Id INTEGER PRIMARY KEY NOT NULL UNIQUE, Ten STRING NOT NULL, DiaChi STRING NOT NULL, ThanhPho STRING NOT NULL, LoaiHinh STRING NOT NULL, GioMoCua TIME NOT NULL, GioDongCua TIME NOT NULL, Wifi STRING, MatKhauWifi STRING, SDT STRING) WITHOUT ROWID;");
        db.QueryData("CREATE TABLE DANHMUCTHUCDON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdQuan INTEGER REFERENCES QUAN (Id) NOT NULL, TenDanhMuc STRING NOT NULL) WITHOUT ROWID;");
        db.QueryData("CREATE TABLE MON (Id INTEGER PRIMARY KEY UNIQUE NOT NULL, IdDanhMuc INTEGER NOT NULL REFERENCES DanhMucThucDon (Id), Gia INTEGER NOT NULL, HinhAnh STRING) WITHOUT ROWID;");
        */



        //hien thi view
        /*
        lsQuan = new ArrayList<>(  );
        lsQuan.add( new Quan( "Quán cà phê" ,"Categories Coffee","Description ......",R.drawable.high1));
        lsQuan.add( new Quan( "Quán cà phê 2" ,"Categories Coffee","Description ......",R.drawable.high2));
        lsQuan.add( new Quan( "Quán cà phê 3" ,"Categories Coffee","Description ......",R.drawable.high3));
        lsQuan.add( new Quan( "Quán cà phê 4","Categories Coffee","Description ......",R.drawable.high4));
        lsQuan.add( new Quan( "Quán trà sữa " ,"Categories Tea","Description ......",R.drawable.high6));
        lsQuan.add( new Quan( "Quán trà sữa 2" ,"Categories Tea","Description ......",R.drawable.high7));
        lsQuan.add( new Quan( "Quán trà sữa 3" ,"Categories Tea","Description ......",R.drawable.hig5));
        lsQuan.add( new Quan( "Quán bánh mỳ" ,"Categories Food","Description ......",R.drawable.high8));
        lsQuan.add( new Quan( "Quán bánh mỳ 2" ,"Categories Food","Description ......",R.drawable.high9));
        lsQuan.add( new Quan( "Quán bánh mỳ 3" ,"Categories Food","Description ......",R.drawable.high10));


        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,lsQuan);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);
        */
    }

    private void loadImageFromURL(String url){
        Picasso.with(this).load(url).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
